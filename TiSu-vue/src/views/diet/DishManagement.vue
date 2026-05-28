<template>
  <div class="dish-management">
    <div class="page-header">
      <h2>菜品管理</h2>
      <el-button type="primary" round @click="openCreate"><el-icon><Plus /></el-icon>创建菜品</el-button>
    </div>

    <!-- 筛选栏 -->
    <el-card shadow="never" style="margin-bottom: var(--space-md)">
      <div class="filter-bar">
        <el-radio-group v-model="sourceFilter" size="small">
          <el-radio-button value="all">全部</el-radio-button>
          <el-radio-button value="official">官方推荐</el-radio-button>
          <el-radio-button value="user">我的菜品</el-radio-button>
        </el-radio-group>
        <el-select v-model="categoryFilter" size="small" style="width: 120px">
          <el-option label="全部分类" value="all" />
          <el-option label="早餐" value="早餐" />
          <el-option label="午餐" value="午餐" />
          <el-option label="晚餐" value="晚餐" />
          <el-option label="加餐" value="加餐" />
          <el-option label="通用" value="通用" />
        </el-select>
        <el-input v-model="keyword" placeholder="搜索菜品..." clearable size="small" style="width: 200px" />
      </div>
    </el-card>

    <!-- 菜品列表 -->
    <el-row :gutter="16" class="dish-grid">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="dish in dishes" :key="dish.id" class="dish-col">
        <el-card class="dish-card" shadow="never">
          <div class="dish-tags">
            <el-tag :type="dish.source === 'official' ? 'primary' : 'success'" size="small" effect="plain">{{ dish.source === 'official' ? '官方推荐' : '我的菜品' }}</el-tag>
            <el-tag size="small">{{ dish.category }}</el-tag>
          </div>
          <h3>{{ dish.name }}</h3>
          <p class="dish-desc">{{ dish.description }}</p>
          <div class="dish-cal">{{ dish.calories }} kcal</div>
          <div class="dish-ingredients">
            <div v-for="ing in dish.ingredients" :key="ing.name" class="ing-row">
              <span>{{ ing.name }}</span>
              <span class="ing-amount">{{ ing.amount }}{{ ing.unit }}</span>
            </div>
          </div>
          <div class="dish-actions" v-if="dish.source === 'user'">
            <el-button type="primary" link size="small" @click="openEdit(dish)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(dish)">删除</el-button>
          </div>
          <div class="dish-actions-placeholder" v-else></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分页 -->
    <div class="pagination-bar">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[8, 12, 24]"
        layout="total, sizes, prev, pager, next"
        background
        small
      />
    </div>

    <!-- 创建/编辑对话框 -->
    <el-dialog v-model="showDialog" :title="isEditing ? '编辑菜品' : '创建菜品'" width="560px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-form-item label="菜品名称">
          <el-input v-model="form.name" placeholder="如：香煎鸡胸肉" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="简要描述菜品特点" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" style="width: 100%">
            <el-option label="早餐" value="早餐" />
            <el-option label="午餐" value="午餐" />
            <el-option label="晚餐" value="晚餐" />
            <el-option label="加餐" value="加餐" />
            <el-option label="通用" value="通用" />
          </el-select>
        </el-form-item>
        <el-form-item label="食材">
          <div class="ingredients-form">
            <div v-for="(ing, i) in form.ingredients" :key="i" class="ing-form-row">
              <el-select v-model="ing.name" filterable placeholder="选择食材" style="flex: 1">
                <el-option v-for="f in allFoods" :key="f.id" :label="f.name" :value="f.name" />
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
import { getDishes, createDish, updateDish, deleteDish } from '../../api/dish'
import { getFoods } from '../../api/food'
import { getTypes } from '../../api/type'
import { ElMessage, ElMessageBox } from 'element-plus'

const dishes = ref([])
const total = ref(0)
const allFoods = ref([])
const categories = ref([])
const sourceFilter = ref('all')
const categoryFilter = ref('all')
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(8)
const showDialog = ref(false)
const isEditing = ref(false)
let editId = null

const form = reactive({
  name: '',
  description: '',
  category: '午餐',
  ingredients: []
})

const fetchDishes = async () => {
  try {
    const params = { page: currentPage.value, pageSize: pageSize.value }
    if (sourceFilter.value !== 'all') params.source = sourceFilter.value
    if (categoryFilter.value !== 'all') params.category = categoryFilter.value
    if (keyword.value) params.keyword = keyword.value
    const res = await getDishes(params)
    dishes.value = res.data.list || res.data
    total.value = res.data.total || dishes.value.length
  } catch (e) {
    console.error('获取菜品列表失败', e)
  }
}

const fetchAllFoods = async () => {
  try {
    const res = await getFoods({ pageSize: 9999 })
    allFoods.value = res.data.list || res.data
  } catch (e) {
    console.error('获取食物列表失败', e)
  }
}

const fetchCategories = async () => {
  try {
    const res = await getTypes('dish_category')
    categories.value = res.data || []
  } catch (e) {
    console.error('获取分类失败', e)
  }
}

watch([sourceFilter, categoryFilter, keyword], () => { currentPage.value = 1; fetchDishes() })
watch([currentPage, pageSize], () => fetchDishes())

const resetForm = () => {
  form.name = ''
  form.description = ''
  form.category = '午餐'
  form.ingredients = []
  editId = null
}

const openCreate = () => {
  isEditing.value = false
  resetForm()
  showDialog.value = true
}

const openEdit = (dish) => {
  isEditing.value = true
  editId = dish.id
  form.name = dish.name
  form.description = dish.description
  form.category = dish.category
  form.ingredients = dish.ingredients.map(i => ({ ...i }))
  showDialog.value = true
}

const handleSave = async () => {
  if (!form.name.trim()) return ElMessage.warning('请输入菜品名称')
  if (!form.ingredients.length) return ElMessage.warning('请至少添加一种食材')
  if (form.ingredients.some(i => !i.name)) return ElMessage.warning('请选择食材')

  const totalCal = form.ingredients.reduce((s, ing) => {
    const food = allFoods.value.find(f => f.name === ing.name)
    return s + (food ? Math.round(food.calories * ing.amount / 100) : 0)
  }, 0)

  try {
    const payload = { name: form.name, description: form.description, category: form.category, calories: totalCal, ingredients: form.ingredients.map(i => ({ ...i })) }
    if (isEditing.value) {
      await updateDish(editId, payload)
      ElMessage.success('已保存')
    } else {
      await createDish({ ...payload, source: 'user' })
      ElMessage.success('已创建')
    }
    showDialog.value = false
    fetchDishes()
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = (dish) => {
  ElMessageBox.confirm(`确定删除「${dish.name}」吗？`, '', { type: 'warning' })
    .then(async () => {
      try {
        await deleteDish(dish.id)
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
  fetchAllFoods()
  fetchDishes()
})
</script>

<style scoped>
.filter-bar { display: flex; gap: var(--space-sm); align-items: center; flex-wrap: wrap; }
.dish-grid { align-items: stretch; }
.dish-col { display: flex; margin-bottom: var(--space-md); }
.dish-card { width: 100%; display: flex; flex-direction: column; background: var(--canvas); border: 1px solid var(--hairline); border-radius: var(--radius-lg); padding: var(--space-lg); transition: all var(--duration-normal) var(--ease-out); }
.dish-card:hover { transform: translateY(-2px); }
.dish-tags { display: flex; gap: 6px; margin-bottom: var(--space-sm); }
.dish-card h3 { font-family: var(--font-text); font-size: var(--text-body-strong); color: var(--ink); margin-bottom: 6px; font-weight: 600; letter-spacing: -0.374px; }
.dish-desc {
  font-family: var(--font-text); font-size: var(--text-caption); color: var(--ink-muted-48);
  margin-bottom: var(--space-sm); line-height: 1.43;
  height: 40px; overflow: hidden;
  display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
}
.dish-cal { font-family: var(--font-text); font-size: var(--text-caption); font-weight: 600; color: var(--primary); margin-bottom: var(--space-sm); padding-bottom: var(--space-sm); border-bottom: 1px solid var(--divider-soft); }
.dish-ingredients { flex: 1; margin-bottom: var(--space-sm); }
.ing-row { display: flex; justify-content: space-between; padding: 2px 0; font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); }
.ing-amount { color: var(--text-tertiary); }
.dish-actions { display: flex; gap: var(--space-xs); padding-top: var(--space-sm); border-top: 1px solid var(--divider-soft); }
.dish-actions-placeholder { height: 1px; }
.pagination-bar { display: flex; justify-content: flex-end; margin-top: var(--space-sm); }

/* 表单内食材行 */
.ingredients-form { width: 100%; }
.ing-form-row { display: flex; align-items: center; gap: var(--space-xs); margin-bottom: var(--space-xs); }
.ing-unit { font-family: var(--font-text); font-size: var(--text-caption); color: var(--ink-muted-48); min-width: 20px; }
</style>
