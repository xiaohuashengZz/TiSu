<template>
  <div class="training-calendar">
    <div class="page-header">
      <h2>训练日历</h2>
      <el-button-group>
        <el-button @click="changeMonth(-1)" circle><el-icon><ArrowLeft /></el-icon></el-button>
        <el-button disabled class="month-label">{{ currentMonth }}</el-button>
        <el-button @click="changeMonth(1)" circle><el-icon><ArrowRight /></el-icon></el-button>
      </el-button-group>
    </div>

    <el-card shadow="never">
      <div class="calendar-header">
        <span v-for="d in ['日','一','二','三','四','五','六']" :key="d" class="weekday">{{ d }}</span>
      </div>
      <div class="calendar-grid">
        <div v-for="(day, i) in calendarDays" :key="i" class="calendar-cell" :class="{ empty: !day.date, today: day.isToday, completed: day.completed, rest: day.isRest }">
          <template v-if="day.date">
            <div class="cell-date">{{ day.day }}</div>
            <div class="cell-plan" v-if="day.plan">{{ day.plan }}</div>
            <el-icon v-if="day.completed" class="check" color="#34c759" :size="16"><CircleCheck /></el-icon>
            <div class="cell-dur" v-if="day.duration">{{ day.duration }}分钟</div>
          </template>
        </div>
      </div>
    </el-card>

    <el-row :gutter="16" style="margin-top: 20px">
      <el-col :xs="24" :sm="8" v-for="s in trainStats" :key="s.label">
        <div class="summary-card">
          <div class="summary-label">{{ s.label }}</div>
          <div class="summary-value">{{ s.value }}</div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { getTrainingLogs } from '../../api/training'

const monthOffset = ref(0)
const trainingCalendar = ref({})
const trainStats = ref([
  { label: '训练次数', value: '本月 0 次' },
  { label: '总时长', value: '0 分钟' },
  { label: '完成率', value: '0%' }
])

const currentMonth = computed(() => { const n = new Date(); n.setMonth(n.getMonth() + monthOffset.value); return `${n.getFullYear()}/${n.getMonth() + 1}` })

const calendarDays = computed(() => {
  const n = new Date(); n.setMonth(n.getMonth() + monthOffset.value)
  const y = n.getFullYear(), m = n.getMonth()
  const first = new Date(y, m, 1).getDay(), total = new Date(y, m + 1, 0).getDate(), today = new Date()
  const days = []
  for (let i = 0; i < first; i++) days.push({ date: null })
  for (let d = 1; d <= total; d++) {
    const ds = `${y}-${String(m+1).padStart(2,'0')}-${String(d).padStart(2,'0')}`
    const c = trainingCalendar.value[ds]
    days.push({ date: ds, day: d, plan: c?.plan || '', completed: c?.completed || false, duration: c?.duration || 0, isRest: c?.plan === '休息日', isToday: today.getFullYear()===y && today.getMonth()===m && today.getDate()===d })
  }
  return days
})

const fetchTrainingLogs = async () => {
  try {
    const n = new Date(); n.setMonth(n.getMonth() + monthOffset.value)
    const year = n.getFullYear(), month = n.getMonth() + 1
    const res = await getTrainingLogs({ year, month })
    const logs = res.data.list || res.data || []
    const calendar = {}
    let totalSessions = 0, totalDuration = 0, completedCount = 0
    logs.forEach(log => {
      const dateKey = log.date
      calendar[dateKey] = { plan: log.planName || log.plan || '', completed: log.completed || false, duration: log.duration || 0 }
      totalSessions++
      totalDuration += log.duration || 0
      if (log.completed) completedCount++
    })
    trainingCalendar.value = calendar
    trainStats.value = [
      { label: '训练次数', value: `本月 ${totalSessions} 次` },
      { label: '总时长', value: `${totalDuration} 分钟` },
      { label: '完成率', value: `${totalSessions ? Math.round(completedCount / totalSessions * 100) : 0}%` }
    ]
  } catch (e) {
    console.error('获取训练日志失败', e)
  }
}

const changeMonth = (d) => { monthOffset.value += d }

watch(monthOffset, () => fetchTrainingLogs())

onMounted(() => fetchTrainingLogs())
</script>

<style scoped>
.month-label { min-width: 110px; }
.calendar-header { display: grid; grid-template-columns: repeat(7, 1fr); text-align: center; margin-bottom: 6px; }
.weekday { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); padding: 8px 0; text-transform: uppercase; letter-spacing: -0.12px; font-weight: 600; }
.calendar-grid { display: grid; grid-template-columns: repeat(7, 1fr); gap: 4px; }
.calendar-cell { min-height: 82px; border-radius: var(--radius-sm); padding: 8px; position: relative; transition: all var(--duration-fast) var(--ease-out); border: 1px solid transparent; }
.calendar-cell:not(.empty):hover { background: rgba(0,102,204,0.04); }
.calendar-cell.today { background: var(--accent-blue-light); border-color: rgba(0,102,204,0.15); }
.calendar-cell.completed { background: rgba(52,199,89,0.04); }
.calendar-cell.rest { background: var(--canvas-parchment); }
.cell-date { font-family: var(--font-text); font-size: var(--text-caption); font-weight: 600; color: var(--ink); }
.cell-plan { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--text-secondary); margin-top: 3px; }
.cell-dur { font-family: var(--font-text); font-size: 10px; color: var(--ink-muted-48); margin-top: 2px; }
.check { position: absolute; top: 6px; right: 6px; }
.summary-card { background: var(--canvas); border-radius: var(--radius-lg); padding: var(--space-lg); border: 1px solid var(--hairline); margin-bottom: 12px; }
.summary-label { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); text-transform: uppercase; letter-spacing: -0.12px; margin-bottom: 6px; }
.summary-value { font-family: var(--font-display); font-size: var(--text-lead); font-weight: 600; color: var(--ink); }
@media (max-width: 768px) { .calendar-cell { min-height: 58px; padding: 4px; } .cell-plan { font-size: 9px; } }
</style>
