<template>
  <div class="diet-plan">
    <div class="page-header">
      <h2>饮食计划</h2>
      <div class="header-actions">
        <el-button-group>
          <el-button @click="changeMonth(-1)" circle><el-icon><ArrowLeft /></el-icon></el-button>
          <el-button disabled class="month-label">{{ currentMonth }}</el-button>
          <el-button @click="changeMonth(1)" circle><el-icon><ArrowRight /></el-icon></el-button>
        </el-button-group>
        <el-button type="primary" round><el-icon><Download /></el-icon>导出</el-button>
      </div>
    </div>

    <!-- 月统计 -->
    <el-row :gutter="16" style="margin-bottom: var(--space-md)">
      <el-col :xs="12" :sm="6" v-for="s in monthStats" :key="s.label">
        <div class="summary-card">
          <div class="summary-label">{{ s.label }}</div>
          <div class="summary-value" :style="{ color: s.color }">{{ s.value }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 月历 -->
    <el-card shadow="never" style="margin-bottom: var(--space-lg)">
      <template #header><span class="card-title">月历</span></template>
      <div class="calendar-header">
        <span v-for="d in ['日','一','二','三','四','五','六']" :key="d" class="weekday">{{ d }}</span>
      </div>
      <div class="calendar-grid">
        <div v-for="(day, i) in calendarDays" :key="i" class="calendar-cell" :class="{ empty: !day.date, today: day.isToday, selected: day.date === selectedDate }" @click="day.date && selectDay(day)">
          <template v-if="day.date">
            <div class="cell-date">{{ day.day }}</div>
            <div class="cell-cal" :style="{ color: getCalColor(day.calories) }">{{ day.calories || '—' }}</div>
          </template>
        </div>
      </div>
    </el-card>

    <!-- 周菜单详情 -->
    <div class="section-header">
      <span class="card-title">本周菜单</span>
      <el-button round size="small" @click="ElMessage.success('已复制周一到每天')"><el-icon><CopyDocument /></el-icon>复制周一</el-button>
    </div>
    <el-card v-for="day in weekPlan" :key="day.day" shadow="never" style="margin-bottom: var(--space-md)">
      <template #header>
        <div class="day-header">
          <span class="day-name">{{ day.day }}</span>
          <el-tag :type="day.totalCalories < 1500 ? 'success' : 'warning'" size="small" effect="plain">{{ day.totalCalories }} kcal</el-tag>
        </div>
      </template>
      <el-row :gutter="12">
        <el-col :xs="24" :sm="12" :md="6" v-for="meal in day.meals" :key="meal.type">
          <div class="meal-block">
            <div class="meal-label">
              <span class="meal-type-name">{{ meal.type }}</span>
              <span class="meal-kcal">{{ meal.dishes.reduce((s, d) => s + d.calories, 0) }} kcal</span>
            </div>
            <div v-for="dish in meal.dishes" :key="dish.name" class="dish-block">
              <div class="dish-header">
                <span class="dish-name">{{ dish.name }}</span>
                <span class="dish-cal">{{ dish.calories }} kcal</span>
              </div>
              <div v-for="ing in dish.ingredients" :key="ing.name" class="ingredient-row">
                <span>{{ ing.name }}</span>
                <span class="ingredient-amount">{{ ing.amount }}{{ ing.unit }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 日菜单抽屉 -->
    <el-drawer v-model="showDrawer" :title="drawerTitle" size="400px">
      <div v-if="selectedDay">
        <el-tag :type="selectedDay.totalCalories < 1500 ? 'success' : 'warning'" effect="plain" style="margin-bottom: var(--space-md)">{{ selectedDay.totalCalories }} kcal</el-tag>
        <div v-for="meal in selectedDay.meals" :key="meal.type" class="drawer-meal">
          <div class="drawer-meal-header">
            <span class="meal-type-name">{{ meal.type }}</span>
            <span class="meal-kcal">{{ meal.dishes.reduce((s, d) => s + d.calories, 0) }} kcal</span>
          </div>
          <div v-for="dish in meal.dishes" :key="dish.name" class="dish-block">
            <div class="dish-header">
              <span class="dish-name">{{ dish.name }}</span>
              <span class="dish-cal">{{ dish.calories }} kcal</span>
            </div>
            <div v-for="ing in dish.ingredients" :key="ing.name" class="ingredient-row">
              <span>{{ ing.name }}</span>
              <span class="ingredient-amount">{{ ing.amount }}{{ ing.unit }}</span>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { getWeekPlan, getMonthPlan } from '../../api/mealPlan'
import { ElMessage } from 'element-plus'

const monthOffset = ref(0)
const selectedDate = ref('')
const selectedDay = ref(null)
const showDrawer = ref(false)
const weekPlan = ref([])
const monthData = ref([])

const currentMonth = computed(() => {
  const n = new Date(); n.setMonth(n.getMonth() + monthOffset.value)
  return `${n.getFullYear()}/${n.getMonth() + 1}`
})

const drawerTitle = computed(() => {
  if (!selectedDate.value) return ''
  const d = new Date(selectedDate.value)
  return `${d.getMonth() + 1}月${d.getDate()}日 菜单`
})

const monthStats = computed(() => {
  const n = new Date(); n.setMonth(n.getMonth() + monthOffset.value)
  const total = new Date(n.getFullYear(), n.getMonth() + 1, 0).getDate()
  const firstWeekday = new Date(n.getFullYear(), n.getMonth(), 1).getDay()
  const cals = []
  for (let d = 0; d < total; d++) {
    cals.push(monthData.value[d]?.totalCalories || weekPlan.value[(firstWeekday + d) % 7]?.totalCalories || 0)
  }
  const sum = cals.reduce((s, c) => s + c, 0)
  const onTarget = cals.filter(c => c > 0 && c < 1500).length
  return [
    { label: '日均摄入', value: `${Math.round(sum / total).toLocaleString()} kcal`, color: '#0066cc' },
    { label: '月总计', value: `${sum.toLocaleString()} kcal`, color: '#1d1d1f' },
    { label: '达标天数', value: `${onTarget} / ${total} 天`, color: '#34c759' },
    { label: '最高', value: `${Math.max(...cals)} kcal`, color: '#ff9500' }
  ]
})

const calendarDays = computed(() => {
  const n = new Date(); n.setMonth(n.getMonth() + monthOffset.value)
  const y = n.getFullYear(), m = n.getMonth()
  const first = new Date(y, m, 1).getDay()
  const total = new Date(y, m + 1, 0).getDate()
  const today = new Date()
  const days = []
  for (let i = 0; i < first; i++) days.push({ date: null })
  for (let d = 1; d <= total; d++) {
    const ds = `${y}-${String(m + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`
    const dayPlan = monthData.value[d - 1] || weekPlan.value[(first + d - 1) % 7]
    days.push({
      date: ds, day: d,
      calories: dayPlan?.totalCalories || 0,
      plan: dayPlan,
      isToday: today.getFullYear() === y && today.getMonth() === m && today.getDate() === d
    })
  }
  return days
})

const getCalColor = (c) => {
  if (!c) return '#d2d2d7'
  if (c < 1300) return '#34c759'
  if (c < 1600) return '#0066cc'
  return '#ff9500'
}

const fetchWeekPlan = async () => {
  try {
    const n = new Date(); n.setMonth(n.getMonth() + monthOffset.value)
    const firstDay = new Date(n.getFullYear(), n.getMonth(), 1)
    const startDate = firstDay.toISOString().split('T')[0]
    const res = await getWeekPlan(startDate)
    weekPlan.value = res.data || []
  } catch (e) {
    console.error('获取周计划失败', e)
  }
}

const fetchMonthPlan = async () => {
  try {
    const n = new Date(); n.setMonth(n.getMonth() + monthOffset.value)
    const res = await getMonthPlan(n.getFullYear(), n.getMonth() + 1)
    monthData.value = res.data || []
  } catch (e) {
    console.error('获取月计划失败', e)
  }
}

const changeMonth = (d) => { monthOffset.value += d }

watch(monthOffset, () => {
  fetchWeekPlan()
  fetchMonthPlan()
})

const selectDay = (day) => {
  selectedDate.value = day.date
  selectedDay.value = day.plan
  showDrawer.value = true
}

onMounted(() => {
  fetchWeekPlan()
  fetchMonthPlan()
})
</script>

<style scoped>
.header-actions { display: flex; gap: var(--space-xs); align-items: center; flex-wrap: wrap; }
.month-label { min-width: 110px; }
.card-title { font-family: var(--font-text); font-size: var(--text-body-strong); font-weight: 600; color: var(--ink); letter-spacing: -0.374px; }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: var(--space-md); }

.summary-card { background: var(--canvas); border-radius: var(--radius-lg); padding: var(--space-lg); border: 1px solid var(--hairline); margin-bottom: var(--space-sm); }
.summary-label { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); text-transform: uppercase; letter-spacing: -0.12px; margin-bottom: 4px; }
.summary-value { font-family: var(--font-display); font-size: var(--text-lead); font-weight: 600; letter-spacing: 0.196px; }

/* 日历 */
.calendar-header { display: grid; grid-template-columns: repeat(7, 1fr); text-align: center; margin-bottom: 6px; }
.weekday { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); padding: var(--space-xs) 0; text-transform: uppercase; letter-spacing: -0.12px; font-weight: 600; }
.calendar-grid { display: grid; grid-template-columns: repeat(7, 1fr); gap: 4px; }
.calendar-cell { min-height: 72px; border-radius: var(--radius-sm); padding: var(--space-xs); text-align: center; cursor: pointer; transition: all var(--duration-fast) var(--ease-out); border: 1px solid transparent; }
.calendar-cell:not(.empty):hover { background: var(--accent-blue-light); border-color: rgba(0,102,204,0.15); }
.calendar-cell.today { background: var(--accent-blue-light); border-color: rgba(0,102,204,0.2); }
.calendar-cell.selected { background: rgba(0,102,204,0.1); border-color: var(--primary); }
.cell-date { font-family: var(--font-text); font-size: var(--text-caption); font-weight: 600; color: var(--ink); }
.cell-cal { font-family: var(--font-text); font-size: var(--text-fine-print); font-weight: 600; margin-top: 4px; }

/* 周菜单 */
.day-header { display: flex; justify-content: space-between; align-items: center; }
.day-name { font-family: var(--font-text); font-weight: 600; font-size: var(--text-body-strong); color: var(--ink); letter-spacing: -0.374px; }
.meal-block { background: var(--canvas-parchment); border-radius: var(--radius-lg); padding: var(--space-sm) var(--space-md); margin-bottom: var(--space-sm); }
.meal-label { display: flex; justify-content: space-between; margin-bottom: var(--space-xs); }
.meal-type-name { font-family: var(--font-text); font-size: var(--text-caption); font-weight: 600; color: var(--ink); }
.meal-kcal { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); }
.dish-block { margin-bottom: var(--space-sm); padding-bottom: var(--space-xs); border-bottom: 1px solid var(--divider-soft); }
.dish-block:last-child { border-bottom: none; margin-bottom: 0; padding-bottom: 0; }
.dish-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
.dish-name { font-family: var(--font-text); font-size: var(--text-caption); font-weight: 600; color: var(--ink); }
.dish-cal { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--primary); }
.ingredient-row { display: flex; justify-content: space-between; padding: 2px 0 2px 10px; font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); }
.ingredient-amount { color: var(--text-tertiary); }

/* 抽屉 */
.drawer-meal { background: var(--canvas-parchment); border-radius: var(--radius-lg); padding: var(--space-sm) var(--space-md); margin-bottom: var(--space-md); }
.drawer-meal-header { display: flex; justify-content: space-between; margin-bottom: var(--space-xs); }

@media (max-width: 768px) { .calendar-cell { min-height: 52px; padding: 4px; } .cell-cal { font-size: 11px; } }
</style>
