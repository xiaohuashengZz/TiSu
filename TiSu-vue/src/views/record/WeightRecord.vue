<template>
  <div class="weight-record">
    <div class="page-header">
      <h2>体重记录</h2>
      <el-button type="primary" round @click="showAddDialog = true"><el-icon><Plus /></el-icon>记录体重</el-button>
    </div>

    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">体重与体脂趋势</span>
          <el-radio-group v-model="chartType" size="small">
            <el-radio-button value="weight">体重</el-radio-button>
            <el-radio-button value="fat">体脂</el-radio-button>
            <el-radio-button value="both">全部</el-radio-button>
          </el-radio-group>
        </div>
      </template>
      <div ref="chartRef" class="chart-box"></div>
    </el-card>

    <el-row :gutter="16" style="margin: 16px 0">
      <el-col :xs="12" :sm="6" v-for="s in weightStats" :key="s.label">
        <div class="summary-card">
          <div class="summary-label">{{ s.label }}</div>
          <div class="summary-value" :style="{ color: s.color }">{{ s.value }}</div>
        </div>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <template #header><span class="card-title">历史记录</span></template>
      <el-table :data="logs" stripe>
        <el-table-column prop="date" label="日期" width="130" />
        <el-table-column prop="weight" label="体重 (kg)" width="120" sortable />
        <el-table-column prop="bodyFat" label="体脂 (%)" width="120" sortable />
        <el-table-column label="BMI" width="80"><template #default="{ row }">{{ (row.weight / (1.75*1.75)).toFixed(1) }}</template></el-table-column>
        <el-table-column label="变化" width="100">
          <template #default="{ row, $index }">
            <span v-if="$index > 0" :style="{ color: row.weight < logs[$index-1].weight ? '#34c759' : '#ff2d55', fontWeight: 600 }">
              {{ (row.weight - logs[$index-1].weight).toFixed(1) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="" width="70"><template #default="{ row }"><el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button></template></el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showAddDialog" title="记录体重" width="400px">
      <el-form label-width="80px">
        <el-form-item label="日期"><el-date-picker v-model="newRecord.date" type="date" style="width:100%" /></el-form-item>
        <el-form-item label="体重"><el-input-number v-model="newRecord.weight" :min="30" :max="200" :precision="1" :step="0.1" style="width:100%" /></el-form-item>
        <el-form-item label="体脂"><el-input-number v-model="newRecord.bodyFat" :min="3" :max="50" :precision="1" :step="0.1" style="width:100%" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false" round>取消</el-button>
        <el-button type="primary" @click="handleSave" round>保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import * as echarts from 'echarts'
import { getWeightLogs, createWeightLog } from '../../api/record'
import { ElMessage, ElMessageBox } from 'element-plus'

const chartType = ref('both')
const chartRef = ref(null)
const showAddDialog = ref(false)
const logs = ref([])
const newRecord = reactive({ date: '', weight: 72, bodyFat: 18.5 })

const weightStats = ref([
  { label: '起始', value: '-- kg', color: '#1d1d1f' },
  { label: '当前', value: '-- kg', color: '#0066cc' },
  { label: '已减', value: '-- kg', color: '#34c759' },
  { label: '距目标', value: '-- kg', color: '#ff9500' }
])

let chart = null

const fetchLogs = async () => {
  try {
    const res = await getWeightLogs()
    const data = res.data.list || res.data || []
    logs.value = [...data].reverse()
    const sorted = [...data]
    if (sorted.length > 0) {
      const first = sorted[0].weight
      const last = sorted[sorted.length - 1].weight
      const diff = (first - last).toFixed(1)
      weightStats.value = [
        { label: '起始', value: `${first} kg`, color: '#1d1d1f' },
        { label: '当前', value: `${last} kg`, color: '#0066cc' },
        { label: '已减', value: `${diff} kg`, color: '#34c759' },
        { label: '距目标', value: `${Math.max(0, last - 65).toFixed(1)} kg`, color: '#ff9500' }
      ]
    }
    if (chart) {
      chart.setOption({
        xAxis: { data: sorted.map(d => d.date.slice(5)) },
        series: [
          { data: sorted.map(d => d.weight) },
          { data: sorted.map(d => d.bodyFat) }
        ]
      })
    }
  } catch (e) {
    console.error('获取体重记录失败', e)
  }
}

const handleSave = async () => {
  try {
    await createWeightLog({ date: newRecord.date, weight: newRecord.weight, bodyFat: newRecord.bodyFat })
    ElMessage.success('已记录')
    showAddDialog.value = false
    fetchLogs()
  } catch (e) {
    ElMessage.error('记录失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除这条记录吗？', '', { type: 'warning' })
    .then(() => { logs.value = logs.value.filter(l => l.date !== row.date); ElMessage.success('已删除') }).catch(() => {})
}

onMounted(async () => {
  chart = echarts.init(chartRef.value)
  chart.setOption({
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(255,255,255,0.95)', borderColor: 'rgba(0,0,0,0.04)', borderWidth: 0.5, extraCssText: 'border-radius:8px;box-shadow:0 8px 30px rgba(0,0,0,0.08)' },
    legend: { data: ['体重', '体脂率'], bottom: 0, textStyle: { color: '#6e6e73' } },
    grid: { left: 40, right: 40, top: 16, bottom: 40 },
    xAxis: { type: 'category', data: [], axisLine: { show: false }, axisTick: { show: false }, axisLabel: { color: '#aeaeb2', fontSize: 12 } },
    yAxis: [
      { type: 'value', name: 'kg', min: 70, max: 80, splitLine: { lineStyle: { color: 'rgba(0,0,0,0.04)' } }, axisLabel: { color: '#aeaeb2', fontSize: 12 } },
      { type: 'value', name: '%', min: 15, max: 25, splitLine: { show: false }, axisLabel: { color: '#aeaeb2', fontSize: 12 } }
    ],
    series: [
      { name: '体重', type: 'line', data: [], smooth: 0.4, lineStyle: { color: '#0066cc', width: 2.5 }, areaStyle: { color: new echarts.graphic.LinearGradient(0,0,0,1,[{offset:0,color:'rgba(0,102,204,0.1)'},{offset:1,color:'rgba(0,102,204,0)'}]) }, itemStyle: { color: '#0066cc' }, symbol: 'circle', symbolSize: 5 },
      { name: '体脂率', type: 'line', yAxisIndex: 1, data: [], smooth: 0.4, lineStyle: { color: '#ff2d55', width: 1.5, type: 'dashed' }, itemStyle: { color: '#ff2d55' }, symbol: 'circle', symbolSize: 4 }
    ]
  })
  window.addEventListener('resize', () => chart.resize())
  await fetchLogs()
})
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
.card-title { font-family: var(--font-text); font-size: var(--text-body-strong); font-weight: 600; color: var(--ink); letter-spacing: -0.374px; }
.chart-box { height: 300px; }
.summary-card { background: var(--canvas); border-radius: var(--radius-lg); padding: var(--space-lg); border: 1px solid var(--hairline); margin-bottom: 12px; }
.summary-label { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); text-transform: uppercase; letter-spacing: -0.12px; margin-bottom: 4px; }
.summary-value { font-family: var(--font-display); font-size: var(--text-lead); font-weight: 600; }
@media (max-width: 768px) { .chart-box { height: 200px; } }
</style>
