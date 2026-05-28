<template>
  <div class="admin-dishes">
    <div class="page-header">
      <h2>菜品管理</h2>
      <el-button type="primary" round @click="openCreate"><el-icon><Plus /></el-icon>创建菜品</el-button>
    </div>

    <!-- 筛选栏 -->
    <el-card shadow="never" style="margin-bottom: var(--space-md)">
      <div class="filter-bar">
        <el-select v-model="categoryFilter" size="small" style="width: 120px">
          <el-option label="全部分类" value="all" />
          <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
        </el-select>
        <el-input v-model="keyword" placeholder="搜索菜品..." clearable size="small" style="width: 200px" />
      </div>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table :data="dishes" stripe style="width: 100%">
        <el-table-column prop="name" label="菜品名称" min-width="140" />
        <el-table-column prop="category" label="分类" width="80" />
        <el-table-column prop="calories" label="热量(kcal)" width="100" />
        <el-table-column prop="source" label="来源" width="100">
          <template #default="{ row }">
            <el-tag :type="row.source === 'official' ? 'primary' : 'success'" size="small" effect="plain">
              {{ row.source === 'official' ? '官方推荐' : '用户创建' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="食材" min-width="200">
          <template #default="{ row }">
            <span class="ingredient-text">{{ row.ingredients.map(i => `${i.name}${i.amount}${i.unit}`).join('、') }}</span>
          </template>
        </el-table-column>
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
    <el-dialog v-model="showDialog" :title="isEditing ? '编辑菜品' : '创建菜品'" width="600px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="菜品名称">
          <el-input v-model="form.name" placeholder="如：香煎鸡胸肉" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="简要描述菜品特点" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" style="width: 100%">
            <el-option v-for="c in categories" :key="c" :label="c" :value="c" />
          </el-select>
        </el-form-item>
        <el-form-item label="食材">
          <div class="ingredients-form">
            <div v-for="(ing, i) in form.ingredients" :key="i" class="ing-form-row">
              <el-select v-model="ing.name" filterable placeholder="选择食材" style="flex: 1">
                <el-option v-for="f in foodList" :key="f.id" :label="f.name" :value="f.name" />
              </el-select>
              <el-input-number v-model="ing.amount" :min="1" :max="9999" :precision="0" style="width: 110px" />
              <span class="ing-unit">{{ ing.unit || 'g' }}</span>
              <el-button type="danger" link @click="form.ingredients.splice(i, 1)"><el-icon><Delete /></el-icon></el-button>
            </div>
            <el-button type="primary" link @click="form.ingredients.push({ name: '', amount: 100, unit: 'g' })">
              <el-icon><Plus /></el-icon>添加食材
            </el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false" round>取消</el-button>
        <el-button type="primary" @click="handleSave" round>{{ isEditing ? '保存' : '创建' }}</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { getAdminDishes, createAdminDish, updateAdminDish, deleteAdminDish, getAdminFoods } from '../../api/admin'
import { getTypes } from '../../api/type'
import { ElMessage, ElMessageBox } from 'element-plus'

const dishes = ref([])
const total = ref(0)
const foodList = ref([])
const categories = ref([])
const categoryFilter = ref('all')
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const showDialog = ref(false)
const isEditing = ref(false)
let editId = null

const form = reactive({
  name: '', description: '', category: '午餐', ingredients: []
})

const fetchCategories = async () => {
  try {
    const res = await getTypes('dish_category')
    categories.value = res.data
  } catch (e) {
    console.error('获取菜品分类失败', e)
  }
}

const fetchFoods = async () => {
  try {
    const res = await getAdminFoods({ page: 1, size: 9999 })
    foodList.value = res.data.records
  } catch (e) {
    console.error('获取食物列表失败', e)
  }
}

const fetchDishes = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    if (categoryFilter.value !== 'all') params.category = categoryFilter.value
    if (keyword.value) params.keyword = keyword.value
    const res = await getAdminDishes(params)
    dishes.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error('获取菜品列表失败', e)
  }
}

watch([categoryFilter, keyword, pageSize], () => {
  currentPage.value = 1
  fetchDishes()
})

watch(currentPage, () => {
  fetchDishes()
})

const resetForm = () => {
  form.name = ''; form.description = ''; form.category = '午餐'; form.ingredients = []; editId = null
}

const openCreate = () => { isEditing.value = false; resetForm(); showDialog.value = true }

const openEdit = (dish) => {
  isEditing.value = true; editId = dish.id
  form.name = dish.name; form.description = dish.description; form.category = dish.category
  form.ingredients = dish.ingredients.map(i => ({ ...i }))
  showDialog.value = true
}

const handleSave = async () => {
  if (!form.name.trim()) return ElMessage.warning('请输入菜品名称')
  if (!form.ingredients.length) return ElMessage.warning('请至少添加一种食材')
  if (form.ingredients.some(i => !i.name)) return ElMessage.warning('请选择食材')

  const totalCal = form.ingredients.reduce((s, ing) => {
    const food = foodList.value.find(f => f.name === ing.name)
    return s + (food ? Math.round(food.calories * ing.amount / 100) : 0)
  }, 0)

  const dishData = {
    name: form.name,
    description: form.description,
    category: form.category,
    calories: totalCal,
    ingredients: form.ingredients.map(i => ({ ...i }))
  }

  try {
    if (isEditing.value) {
      await updateAdminDish(editId, dishData)
      ElMessage.success('已保存')
    } else {
      await createAdminDish({ ...dishData, source: 'official' })
      ElMessage.success('已创建')
    }
    showDialog.value = false
    fetchDishes()
  } catch (e) {
    ElMessage.error(isEditing.value ? '保存失败' : '创建失败')
  }
}

const handleDelete = (dish) => {
  ElMessageBox.confirm(`确定删除「${dish.name}」吗？`, '', { type: 'warning' })
    .then(async () => {
      try {
        await deleteAdminDish(dish.id)
        ElMessage.success('已删除')
        fetchDishes()
      } catch (e) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

onMounted(() => {
  fetchCategories()
  fetchFoods()
  fetchDishes()
})
</script>

<style scoped>
.filter-bar { display: flex; gap: var(--space-sm); align-items: center; flex-wrap: wrap; }
.pagination-bar { display: flex; justify-content: flex-end; margin-top: var(--space-md); }
.ingredient-text { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); }
.ingredients-form { width: 100%; }
.ing-form-row { display: flex; align-items: center; gap: var(--space-xs); margin-bottom: var(--space-xs); }
.ing-unit { font-family: var(--font-text); font-size: var(--text-caption); color: var(--ink-muted-48); min-width: 20px; }
</style>
