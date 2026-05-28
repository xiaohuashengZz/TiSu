<template>
  <div class="admin-exercises">
    <div class="page-header">
      <h2>动作管理</h2>
      <el-button type="primary" round @click="openCreate"><el-icon><Plus /></el-icon>添加动作</el-button>
    </div>

    <!-- 筛选栏 -->
    <el-card shadow="never" style="margin-bottom: var(--space-md)">
      <div class="filter-bar">
        <el-radio-group v-model="muscleFilter" size="small">
          <el-radio-button value="all">全部</el-radio-button>
          <el-radio-button v-for="m in muscleGroups" :key="m" :value="m">{{ m }}</el-radio-button>
        </el-radio-group>
        <el-input v-model="keyword" placeholder="搜索动作..." clearable size="small" style="width: 200px" />
      </div>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table :data="exercises" stripe style="width: 100%">
        <el-table-column prop="name" label="动作名称" min-width="120" />
        <el-table-column prop="muscleGroup" label="目标肌群" width="100" />
        <el-table-column prop="difficulty" label="难度" width="80">
          <template #default="{ row }">
            <el-tag :type="row.difficulty === '初级' ? 'success' : row.difficulty === '中级' ? 'warning' : 'danger'" size="small" effect="plain">
              {{ row.difficulty }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sets" label="组数" width="70" />
        <el-table-column prop="reps" label="次数" width="70" />
        <el-table-column prop="description" label="动作描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-bar">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          background
          small
        />
      </div>
    </el-card>

    <!-- 创建/编辑对话框 -->
    <el-dialog v-model="showDialog" :title="isEditing ? '编辑动作' : '添加动作'" width="560px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="动作名称">
          <el-input v-model="form.name" placeholder="如：卧推" />
        </el-form-item>
        <el-form-item label="目标肌群">
          <el-select v-model="form.muscleGroup" filterable allow-create style="width: 100%">
            <el-option v-for="m in muscleGroups" :key="m" :label="m" :value="m" />
          </el-select>
        </el-form-item>
        <el-form-item label="难度">
          <el-radio-group v-model="form.difficulty">
            <el-radio value="初级">初级</el-radio>
            <el-radio value="中级">中级</el-radio>
            <el-radio value="高级">高级</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="组数">
              <el-input-number v-model="form.sets" :min="1" :max="20" :precision="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="次数">
              <el-input-number v-model="form.reps" :min="1" :max="999" :precision="0" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="动作要领描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false" round>取消</el-button>
        <el-button type="primary" @click="handleSave" round>{{ isEditing ? '保存' : '添加' }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { getAdminExercises, createAdminExercise, updateAdminExercise, deleteAdminExercise } from '../../api/admin'
import { getTypes } from '../../api/type'
import { ElMessage, ElMessageBox } from 'element-plus'

const exercises = ref([])
const total = ref(0)
const muscleGroups = ref([])
const difficulties = ref([])
const muscleFilter = ref('all')
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const showDialog = ref(false)
const isEditing = ref(false)
let editId = null

const form = reactive({
  name: '', muscleGroup: '胸', difficulty: '初级', sets: 3, reps: 10, description: ''
})

const fetchTypes = async () => {
  try {
    const [mgRes, diffRes] = await Promise.all([
      getTypes('muscle_group'),
      getTypes('difficulty')
    ])
    muscleGroups.value = mgRes.data
    difficulties.value = diffRes.data
  } catch (e) {
    console.error('获取类型数据失败', e)
  }
}

const fetchExercises = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    if (muscleFilter.value !== 'all') params.muscleGroup = muscleFilter.value
    if (keyword.value) params.keyword = keyword.value
    const res = await getAdminExercises(params)
    exercises.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error('获取动作列表失败', e)
  }
}

watch([muscleFilter, keyword, pageSize], () => {
  currentPage.value = 1
  fetchExercises()
})

watch(currentPage, () => {
  fetchExercises()
})

const resetForm = () => {
  form.name = ''; form.muscleGroup = '胸'; form.difficulty = '初级'
  form.sets = 3; form.reps = 10; form.description = ''; editId = null
}

const openCreate = () => { isEditing.value = false; resetForm(); showDialog.value = true }

const openEdit = (ex) => {
  isEditing.value = true; editId = ex.id
  Object.assign(form, { name: ex.name, muscleGroup: ex.muscleGroup, difficulty: ex.difficulty, sets: ex.sets, reps: ex.reps, description: ex.description })
  showDialog.value = true
}

const handleSave = async () => {
  if (!form.name.trim()) return ElMessage.warning('请输入动作名称')
  try {
    if (isEditing.value) {
      await updateAdminExercise(editId, { ...form })
      ElMessage.success('已保存')
    } else {
      await createAdminExercise({ ...form })
      ElMessage.success('已添加')
    }
    showDialog.value = false
    fetchExercises()
  } catch (e) {
    ElMessage.error(isEditing.value ? '保存失败' : '添加失败')
  }
}

const handleDelete = (ex) => {
  ElMessageBox.confirm(`确定删除「${ex.name}」吗？`, '', { type: 'warning' })
    .then(async () => {
      try {
        await deleteAdminExercise(ex.id)
        ElMessage.success('已删除')
        fetchExercises()
      } catch (e) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

onMounted(() => {
  fetchTypes()
  fetchExercises()
})
</script>

<style scoped>
.filter-bar { display: flex; gap: var(--space-sm); align-items: center; flex-wrap: wrap; }
.pagination-bar { display: flex; justify-content: flex-end; margin-top: var(--space-md); }
</style>
