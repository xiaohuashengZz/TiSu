<template>
  <div class="body-record">
    <div class="page-header">
      <h2>体围记录</h2>
      <el-button type="primary" round @click="showAddDialog = true"><el-icon><Plus /></el-icon>记录体围</el-button>
    </div>

    <el-card shadow="never">
      <template #header><span class="card-title">体围趋势</span></template>
      <div ref="chartRef" class="chart-box"></div>
    </el-card>

    <el-card shadow="never" style="margin-top: 16px">
      <template #header><span class="card-title">历史记录</span></template>
      <el-table :data="measurements" stripe>
        <el-table-column prop="date" label="日期" width="130" />
        <el-table-column prop="chest" label="胸围" width="90" sortable />
        <el-table-column prop="waist" label="腰围" width="90" sortable />
        <el-table-column prop="hip" label="臀围" width="90" sortable />
        <el-table-column prop="arm" label="臂围" width="90" sortable />
        <el-table-column prop="thigh" label="大腿" width="90" sortable />
        <el-table-column label="" width="70"><template #default="{ row }"><el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button></template></el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="showAddDialog" title="记录体围" width="480px">
      <el-form :model="newRecord" label-width="70px">
        <el-form-item label="日期"><el-date-picker v-model="newRecord.date" type="date" style="width:100%" /></el-form-item>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="胸围"><el-input-number v-model="newRecord.chest" :min="50" :max="200" :precision="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="腰围"><el-input-number v-model="newRecord.waist" :min="40" :max="200" :precision="1" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="臀围"><el-input-number v-model="newRecord.hip" :min="50" :max="200" :precision="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="臂围"><el-input-number v-model="newRecord.arm" :min="15" :max="60" :precision="1" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="大腿"><el-input-number v-model="newRecord.thigh" :min="30" :max="100" :precision="1" /></el-form-item>
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
import { getBodyMeasurements, createBodyMeasurement } from '../../api/record'
import { ElMessage, ElMessageBox } from 'element-plus'

const chartRef = ref(null)
const showAddDialog = ref(false)
const measurements = ref([])
const newRecord = reactive({ date: '', chest: 96, waist: 80, hip: 97, arm: 31, thigh: 53 })

let chart = null

const fetchMeasurements = async () => {
  try {
    const res = await getBodyMeasurements()
    const data = res.data.list || res.data || []
    measurements.value = [...data].reverse()
    const sorted = [...data]
    if (chart && sorted.length > 0) {
      chart.setOption({
        xAxis: { data: sorted.map(d => d.date.slice(5)) },
        series: ['chest', 'waist', 'hip', 'arm', 'thigh'].map((k) => ({
          data: sorted.map(d => d[k])
        }))
      })
    }
  } catch (e) {
    console.error('获取体围记录失败', e)
  }
}

const handleSave = async () => {
  try {
    await createBodyMeasurement({ date: newRecord.date, chest: newRecord.chest, waist: newRecord.waist, hip: newRecord.hip, arm: newRecord.arm, thigh: newRecord.thigh })
    ElMessage.success('已记录')
    showAddDialog.value = false
    fetchMeasurements()
  } catch (e) {
    ElMessage.error('记录失败')
  }
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除这条记录吗？', '', { type: 'warning' })
    .then(() => { measurements.value = measurements.value.filter(m => m.date !== row.date); ElMessage.success('已删除') }).catch(() => {})
}

onMounted(async () => {
  chart = echarts.init(chartRef.value)
  const colors = ['#0066cc', '#34c759', '#ff9500', '#ff2d55', '#af52de']
  chart.setOption({
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(255,255,255,0.95)', borderColor: 'rgba(0,0,0,0.04)', borderWidth: 0.5, extraCssText: 'border-radius:8px;box-shadow:0 8px 30px rgba(0,0,0,0.08)' },
    legend: { data: ['胸围', '腰围', '臀围', '臂围', '大腿'], bottom: 0, textStyle: { color: '#6e6e73' } },
    grid: { left: 40, right: 16, top: 16, bottom: 40 },
    xAxis: { type: 'category', data: [], axisLine: { show: false }, axisTick: { show: false }, axisLabel: { color: '#aeaeb2', fontSize: 12 } },
    yAxis: { type: 'value', min: 30, max: 110, splitLine: { lineStyle: { color: 'rgba(0,0,0,0.04)' } }, axisLabel: { color: '#aeaeb2', fontSize: 12 } },
    series: ['chest', 'waist', 'hip', 'arm', 'thigh'].map((_, i) => ({
      name: ['胸围', '腰围', '臀围', '臂围', '大腿'][i], type: 'line',
      data: [], smooth: 0.4,
      lineStyle: { color: colors[i], width: 2 }, itemStyle: { color: colors[i] }, symbol: 'circle', symbolSize: 4
    }))
  })
  window.addEventListener('resize', () => chart.resize())
  await fetchMeasurements()
})
</script>

<style scoped>
.card-title { font-family: var(--font-text); font-size: var(--text-body-strong); font-weight: 600; color: var(--ink); letter-spacing: -0.374px; }
.chart-box { height: 300px; }
@media (max-width: 768px) { .chart-box { height: 200px; } }
</style>
