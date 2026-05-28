<template>
  <div class="food-library">
    <div class="page-header">
      <h2>食物库</h2>
      <el-button type="primary" round @click="openCreate"><el-icon><Plus /></el-icon>添加食物</el-button>
    </div>

    <!-- 筛选栏 -->
    <el-card shadow="never" style="margin-bottom: var(--space-md)">
      <div class="filter-bar">
        <el-radio-group v-model="sourceFilter" size="small">
          <el-radio-button value="all">全部</el-radio-button>
          <el-radio-button value="official">官方食物</el-radio-button>
          <el-radio-button value="user">我的食物</el-radio-button>
        </el-radio-group>
        <el-select v-model="categoryFilter" size="small" style="width: 120px">
          <el-option label="全部分类" value="all" />
          <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
        </el-select>
        <el-input v-model="keyword" placeholder="搜索食物..." clearable size="small" style="width: 200px" />
      </div>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table :data="foods" stripe style="width: 100%">
        <el-table-column prop="name" label="名称" min-width="120" />
        <el-table-column prop="category" label="分类" width="90">
          <template #default="{ row }"><el-tag size="small" effect="plain">{{ row.category }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="source" label="来源" width="100">
          <template #default="{ row }">
            <el-tag :type="row.source === 'official' ? 'primary' : 'success'" size="small" effect="plain">
              {{ row.source === 'official' ? '官方食物' : '我的食物' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="70" />
        <el-table-column prop="calories" label="热量(kcal)" width="100" sortable />
        <el-table-column prop="protein" label="蛋白质(g)" width="100" sortable />
        <el-table-column prop="carbs" label="碳水(g)" width="90" sortable />
        <el-table-column prop="fat" label="脂肪(g)" width="90" sortable />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <template v-if="row.source === 'user'">
              <el-button type="primary" link size="small" @click="openEdit(row)">编辑</el-button>
              <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
            </template>
            <span v-else class="no-action">—</span>
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
    <el-dialog v-model="showDialog" :title="isEditing ? '编辑食物' : '添加食物'" width="560px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name" placeholder="如：鸡胸肉" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" filterable allow-create style="width: 100%">
            <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
          </el-select>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="热量">
              <el-input-number v-model="form.calories" :min="0" :max="9999" :precision="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位">
              <el-input v-model="form.unit" placeholder="如：100g" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="蛋白质">
              <el-input-number v-model="form.protein" :min="0" :max="999" :precision="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="碳水">
              <el-input-number v-model="form.carbs" :min="0" :max="999" :precision="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="脂肪">
              <el-input-number v-model="form.fat" :min="0" :max="999" :precision="1" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纤维">
              <el-input-number v-model="form.fiber" :min="0" :max="999" :precision="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
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
import { getFoods, createFood, updateFood, deleteFood } from '../../api/food'
import { getTypes } from '../../api/type'
import { ElMessage, ElMessageBox } from 'element-plus'

const foods = ref([])
const total = ref(0)
const categories = ref([])
const sourceFilter = ref('all')
const categoryFilter = ref('all')
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const showDialog = ref(false)
const isEditing = ref(false)
let editId = null

const form = reactive({
  name: '', category: '肉类', calories: 0, protein: 0, carbs: 0, fat: 0, fiber: 0, unit: '100g'
})

const fetchFoods = async () => {
  try {
    const params = { page: currentPage.value, pageSize: pageSize.value }
    if (sourceFilter.value !== 'all') params.source = sourceFilter.value
    if (categoryFilter.value !== 'all') params.category = categoryFilter.value
    if (keyword.value) params.keyword = keyword.value
    const res = await getFoods(params)
    foods.value = res.data.list || res.data
    total.value = res.data.total || foods.value.length
  } catch (e) {
    console.error('获取食物列表失败', e)
  }
}

const fetchCategories = async () => {
  try {
    const res = await getTypes('food_category')
    categories.value = res.data || []
  } catch (e) {
    console.error('获取分类失败', e)
  }
}

watch([sourceFilter, categoryFilter, keyword], () => { currentPage.value = 1; fetchFoods() })
watch([currentPage, pageSize], () => fetchFoods())

const resetForm = () => {
  form.name = ''; form.category = '肉类'; form.calories = 0
  form.protein = 0; form.carbs = 0; form.fat = 0; form.fiber = 0; form.unit = '100g'
  editId = null
}

const openCreate = () => { isEditing.value = false; resetForm(); showDialog.value = true }

const openEdit = (food) => {
  isEditing.value = true; editId = food.id
  Object.assign(form, { name: food.name, category: food.category, calories: food.calories, protein: food.protein, carbs: food.carbs, fat: food.fat, fiber: food.fiber, unit: food.unit })
  showDialog.value = true
}

const handleSave = async () => {
  if (!form.name.trim()) return ElMessage.warning('请输入食物名称')
  try {
    if (isEditing.value) {
      await updateFood(editId, { ...form })
      ElMessage.success('已保存')
    } else {
      await createFood({ ...form, source: 'user' })
      ElMessage.success('已添加')
    }
    showDialog.value = false
    fetchFoods()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = (food) => {
  ElMessageBox.confirm(`确定删除「${food.name}」吗？`, '', { type: 'warning' })
    .then(async () => {
      try {
        await deleteFood(food.id)
        ElMessage.success('已删除')
        fetchFoods()
      } catch (e) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

onMounted(() => {
  fetchCategories()
  fetchFoods()
})
</script>

<style scoped>
.filter-bar { display: flex; gap: var(--space-sm); align-items: center; flex-wrap: wrap; }
.pagination-bar { display: flex; justify-content: flex-end; margin-top: var(--space-md); }
.no-action { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); }
</style>
