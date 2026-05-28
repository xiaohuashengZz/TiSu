<template>
  <div class="admin-dashboard">
    <div class="page-header">
      <h2>管理首页</h2>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" style="margin-bottom: var(--space-lg)">
      <el-col :xs="12" :sm="6" v-for="s in stats" :key="s.label">
        <div class="stat-card">
          <div class="stat-icon" :style="{ background: s.bg }">
            <el-icon :size="20" :color="s.color"><component :is="s.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ s.value }}</div>
            <div class="stat-label">{{ s.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <!-- 最近活动 -->
      <el-col :xs="24" :lg="14">
        <el-card shadow="never" class="section-card">
          <template #header><span class="card-title">最近活动</span></template>
          <div class="activity-list">
            <div v-for="a in activities" :key="a.id" class="activity-item">
              <div class="activity-dot" :style="{ background: a.color }"></div>
              <div class="activity-content">
                <span class="activity-text">{{ a.text }}</span>
                <span class="activity-time">{{ a.time }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 数据概览 -->
      <el-col :xs="24" :lg="10">
        <el-card shadow="never" class="section-card">
          <template #header><span class="card-title">数据概览</span></template>
          <div class="overview-list">
            <div v-for="o in overviews" :key="o.label" class="overview-item">
              <span class="overview-label">{{ o.label }}</span>
              <span class="overview-value">{{ o.value }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminStats, getAdminActivities, getAdminOverview } from '../../api/admin'

const stats = ref([
  { label: '注册用户', value: 0, icon: 'User', color: '#0066cc', bg: 'rgba(0,102,204,0.1)' },
  { label: '菜品总数', value: 0, icon: 'KnifeFork', color: '#34c759', bg: 'rgba(52,199,89,0.1)' },
  { label: '食物种类', value: 0, icon: 'Apple', color: '#ff9500', bg: 'rgba(255,149,0,0.1)' },
  { label: '训练动作', value: 0, icon: 'Bicycle', color: '#af52de', bg: 'rgba(175,82,222,0.1)' }
])

const activities = ref([])

const overviews = ref([])

const fetchStats = async () => {
  try {
    const res = await getAdminStats()
    stats.value = [
      { ...stats.value[0], value: res.data.userCount ?? res.data.userCount },
      { ...stats.value[1], value: res.data.dishCount ?? res.data.dishCount },
      { ...stats.value[2], value: res.data.foodCount ?? res.data.foodCount },
      { ...stats.value[3], value: res.data.exerciseCount ?? res.data.exerciseCount }
    ]
  } catch (e) {
    console.error('获取统计数据失败', e)
  }
}

const fetchActivities = async () => {
  try {
    const res = await getAdminActivities({ limit: 7 })
    activities.value = res.data
  } catch (e) {
    console.error('获取活动数据失败', e)
  }
}

const fetchOverview = async () => {
  try {
    const res = await getAdminOverview()
    overviews.value = res.data
  } catch (e) {
    console.error('获取概览数据失败', e)
  }
}

onMounted(() => {
  fetchStats()
  fetchActivities()
  fetchOverview()
})
</script>

<style scoped>
.stat-card {
  background: var(--canvas); border: 1px solid var(--hairline); border-radius: var(--radius-lg);
  padding: var(--space-lg); display: flex; align-items: center; gap: var(--space-md);
}
.stat-icon {
  width: 44px; height: 44px; border-radius: var(--radius-sm);
  display: flex; align-items: center; justify-content: center; flex-shrink: 0;
}
.stat-value { font-family: var(--font-display); font-size: var(--text-lead); font-weight: 600; color: var(--ink); letter-spacing: 0.196px; }
.stat-label { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); margin-top: 2px; }

.section-card { background: var(--canvas); border: 1px solid var(--hairline); border-radius: var(--radius-lg); }
.card-title { font-family: var(--font-text); font-size: var(--text-body-strong); font-weight: 600; color: var(--ink); letter-spacing: -0.374px; }

.activity-list { display: flex; flex-direction: column; }
.activity-item { display: flex; align-items: flex-start; gap: var(--space-sm); padding: var(--space-sm) 0; border-bottom: 1px solid var(--divider-soft); }
.activity-item:last-child { border-bottom: none; }
.activity-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; margin-top: 6px; }
.activity-content { display: flex; flex-direction: column; }
.activity-text { font-family: var(--font-text); font-size: var(--text-caption); color: var(--ink); line-height: 1.43; }
.activity-time { font-family: var(--font-text); font-size: var(--text-fine-print); color: var(--ink-muted-48); margin-top: 2px; }

.overview-list { display: flex; flex-direction: column; }
.overview-item { display: flex; justify-content: space-between; align-items: center; padding: var(--space-sm) 0; border-bottom: 1px solid var(--divider-soft); }
.overview-item:last-child { border-bottom: none; }
.overview-label { font-family: var(--font-text); font-size: var(--text-caption); color: var(--ink-muted-48); }
.overview-value { font-family: var(--font-text); font-size: var(--text-caption); font-weight: 600; color: var(--ink); }
</style>
