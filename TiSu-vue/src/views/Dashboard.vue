<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :xs="12" :sm="12" :md="6" v-for="(stat, i) in stats" :key="i">
        <div class="stat-card" :class="`accent-${stat.accent}`">
          <div class="stat-top">
            <div class="stat-icon-ring" :style="{ background: stat.iconBg }">
              <el-icon :size="20" :style="{ color: stat.iconColor }"><component :is="stat.icon" /></el-icon>
            </div>
          </div>
          <div class="label">{{ stat.label }}</div>
          <div class="value">{{ stat.value }}<span class="unit">{{ stat.unit }}</span></div>
          <div class="stat-progress">
            <el-progress :percentage="stat.pct" :stroke-width="4" :show-text="false" :color="stat.iconColor" />
          </div>
          <div class="stat-target">{{ stat.target }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16">
      <el-col :xs="24" :md="16">
        <el-card class="chart-card" shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">体重趋势</span>
              <el-radio-group v-model="chartPeriod" size="small">
                <el-radio-button value="week">周</el-radio-button>
                <el-radio-button value="month">月</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="weightChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="8">
        <el-card class="chart-card" shadow="never">
          <template #header><span class="card-title">今日营养</span></template>
          <div ref="nutritionChartRef" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 今日菜单 & 今日训练 -->
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :xs="24" :md="12">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">今日饮食</span>
              <el-button type="primary" link @click="$router.push('/diet/plan')">查看全部</el-button>
            </div>
          </template>
          <div v-for="meal in todayMeals" :key="meal.type" class="meal-item">
            <div class="meal-top">
              <span class="meal-type">{{ meal.type }}</span>
              <span class="meal-cal">{{ meal.calories }} kcal</span>
            </div>
            <div class="meal-foods">{{ meal.foods.join(' · ') }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="12">
        <el-card shadow="never">
          <template #header>
            <div class="card-header">
              <span class="card-title">今日训练</span>
              <el-button type="primary" link @click="$router.push('/training/calendar')">查看日历</el-button>
            </div>
          </template>
          <div class="training-info">
            <div class="training-plan-name">{{ todayTraining.plan }}</div>
            <div v-for="ex in todayTraining.exercises" :key="ex.name" class="training-ex">
              <div class="ex-left">
                <span class="ex-dot"></span>
                <span class="ex-name">{{ ex.name }}</span>
              </div>
              <span class="ex-detail">{{ ex.sets }} × {{ ex.reps }} · {{ ex.weight }}</span>
            </div>
            <el-button type="primary" size="small" style="margin-top: 20px" @click="ElMessage.success('已标记完成')">
              标记完成
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getDashboard } from '../api/dashboard'
import { ElMessage } from 'element-plus'

const chartPeriod = ref('month')
const weightChartRef = ref(null)
const nutritionChartRef = ref(null)

const stats = ref([])
const todayMeals = ref([])
const todayTraining = ref({ plan: '', exercises: [] })

const initCharts = (weightLogs, nutrition) => {
  const weightChart = echarts.init(weightChartRef.value)
  weightChart.setOption({
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(255,255,255,0.95)', borderColor: 'rgba(0,0,0,0.04)', borderWidth: 0.5, textStyle: { color: '#1d1d1f', fontSize: 13 }, extraCssText: 'border-radius:8px;box-shadow:0 8px 30px rgba(0,0,0,0.08)' },
    grid: { left: 40, right: 16, top: 16, bottom: 28 },
    xAxis: { type: 'category', data: weightLogs.map(d => d.date.slice(5)), axisLine: { show: false }, axisTick: { show: false }, axisLabel: { color: '#aeaeb2', fontSize: 12 } },
    yAxis: { type: 'value', min: 70, max: 80, splitLine: { lineStyle: { color: 'rgba(0,0,0,0.04)' } }, axisLabel: { color: '#aeaeb2', fontSize: 12 } },
    series: [
      { name: '体重', type: 'line', data: weightLogs.map(d => d.weight), smooth: 0.4,
        lineStyle: { color: '#0066cc', width: 2.5 },
        areaStyle: { color: new echarts.graphic.LinearGradient(0,0,0,1,[{offset:0,color:'rgba(0,102,204,0.12)'},{offset:1,color:'rgba(0,102,204,0)'}]) },
        itemStyle: { color: '#0066cc' }, symbol: 'circle', symbolSize: 5 },
      { name: '体脂率', type: 'line', data: weightLogs.map(d => d.bodyFat), smooth: 0.4,
        lineStyle: { color: '#ff2d55', width: 1.5, type: 'dashed' },
        itemStyle: { color: '#ff2d55' }, symbol: 'circle', symbolSize: 4 }
    ]
  })

  const nutritionChart = echarts.init(nutritionChartRef.value)
  nutritionChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c}g ({d}%)', backgroundColor: 'rgba(255,255,255,0.95)', borderColor: 'rgba(0,0,0,0.04)', borderWidth: 0.5, extraCssText: 'border-radius:8px;box-shadow:0 8px 30px rgba(0,0,0,0.08)' },
    legend: { bottom: 0, textStyle: { color: '#6e6e73', fontSize: 12 } },
    series: [{ type: 'pie', radius: ['48%', '70%'], label: { show: false },
      data: nutrition || [
        { value: 120, name: '蛋白质', itemStyle: { color: '#0066cc' } },
        { value: 150, name: '碳水', itemStyle: { color: '#34c759' } },
        { value: 45, name: '脂肪', itemStyle: { color: '#ff9500' } },
        { value: 20, name: '膳食纤维', itemStyle: { color: '#d2d2d7' } }
      ]
    }]
  })

  window.addEventListener('resize', () => { weightChart.resize(); nutritionChart.resize() })
}

onMounted(async () => {
  try {
    const res = await getDashboard()
    const data = res.data
    if (data.stats) stats.value = data.stats
    if (data.todayMeals) todayMeals.value = data.todayMeals
    if (data.todayTraining) todayTraining.value = data.todayTraining
    initCharts(data.weightLogs || [], data.nutrition)
  } catch (e) {
    console.error('获取仪表盘数据失败', e)
    initCharts([], null)
  }
})
</script>

<style scoped>
.stat-row { margin-bottom: 16px; }

.stat-card {
  background: var(--canvas);
  border-radius: var(--radius-lg);
  border: 1px solid var(--hairline);
  padding: var(--space-lg);
  margin-bottom: 16px;
  transition: all var(--duration-normal) var(--ease-out);
}

.stat-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  transform: translateY(-1px);
}

.stat-top { margin-bottom: 14px; }

.stat-icon-ring {
  width: 40px; height: 40px;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-card .label {
  font-family: var(--font-text);
  font-size: var(--text-fine-print);
  color: var(--ink-muted-48);
  text-transform: uppercase;
  letter-spacing: -0.12px;
  margin-bottom: 6px;
}
.stat-card .value {
  font-family: var(--font-display);
  font-size: 30px;
  font-weight: 600;
  color: var(--ink);
  letter-spacing: -1px;
  line-height: 1;
}
.stat-card .unit {
  font-family: var(--font-text);
  font-size: var(--text-caption);
  color: var(--ink-muted-48);
  font-weight: 400;
  margin-left: 3px;
}
.stat-progress { margin: 12px 0 6px; }
.stat-target {
  font-family: var(--font-text);
  font-size: 10px;
  color: var(--ink-muted-48);
}

.chart-card { margin-bottom: 16px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.card-title {
  font-family: var(--font-text);
  font-size: var(--text-body-strong);
  font-weight: 600;
  color: var(--ink);
  letter-spacing: -0.374px;
}
.chart-box { height: 280px; }

.meal-item { padding: 14px 0; border-bottom: 1px solid var(--divider-soft); }
.meal-item:last-child { border-bottom: none; }
.meal-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
.meal-type {
  font-family: var(--font-text);
  font-size: var(--text-caption);
  font-weight: 600;
  color: var(--ink);
}
.meal-cal {
  font-family: var(--font-text);
  font-size: var(--text-caption);
  color: var(--ink-muted-48);
}
.meal-foods {
  font-family: var(--font-text);
  font-size: var(--text-caption);
  color: var(--text-secondary);
}

.training-plan-name {
  font-family: var(--font-display);
  font-size: var(--text-lead);
  font-weight: 600;
  color: var(--ink);
  margin-bottom: 16px;
  letter-spacing: 0.196px;
}
.training-ex { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid var(--divider-soft); }
.training-ex:last-child { border-bottom: none; }
.ex-left { display: flex; align-items: center; gap: 10px; }
.ex-dot { width: 6px; height: 6px; border-radius: 50%; background: var(--primary); }
.ex-name {
  font-family: var(--font-text);
  font-weight: 600;
  color: var(--ink);
  font-size: var(--text-caption);
}
.ex-detail {
  font-family: var(--font-text);
  color: var(--ink-muted-48);
  font-size: var(--text-caption);
}

@media (max-width: 768px) {
  .chart-box { height: 200px; }
  .stat-card .value { font-size: 24px; }
}
</style>
