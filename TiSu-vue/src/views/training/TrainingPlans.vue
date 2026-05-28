<template>
  <div class="training-plans">
    <div class="page-header">
      <h2>训练方案</h2>
      <el-button type="primary" round><el-icon><Plus /></el-icon>创建方案</el-button>
    </div>

    <el-row :gutter="16">
      <el-col :xs="24" :md="12" v-for="plan in plans" :key="plan.id">
        <el-card shadow="never" style="margin-bottom: 16px">
          <template #header>
            <div class="plan-header">
              <div>
                <h3>{{ plan.name }}</h3>
                <div class="plan-meta">
                  <el-tag size="small" effect="plain">{{ plan.level }}</el-tag>
                  <span class="duration">{{ plan.duration }} 天</span>
                </div>
              </div>
              <el-button type="primary" size="small" round @click="selectedPlan = plan">详情</el-button>
            </div>
          </template>
          <p class="plan-desc">{{ plan.description }}</p>
          <div class="plan-days">
            <div v-for="day in plan.days" :key="day.day" class="day-badge" :class="{ rest: !day.exercises.length }">
              <span class="day-num">第{{ day.day }}天</span>
              <span class="day-label">{{ day.label }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-drawer v-model="selectedPlan" :title="selectedPlan?.name" size="460px">
      <div v-if="selectedPlan">
        <p style="color: var(--text-secondary); margin-bottom: 24px; line-height: 1.6">{{ selectedPlan.description }}</p>
        <el-timeline>
          <el-timeline-item v-for="day in selectedPlan.days" :key="day.day" :type="day.exercises.length ? 'primary' : 'info'" :hollow="!day.exercises.length">
            <h4>{{ day.label }}</h4>
            <div v-if="day.exercises.length" style="margin-top: 8px">
              <div v-for="ex in day.exercises" :key="ex.name" style="display:flex;justify-content:space-between;padding:4px 0;font-size:14px">
                <span>{{ ex.name }}</span><span style="color:var(--text-tertiary)">{{ ex.sets }}×{{ ex.reps }} {{ ex.weight }}</span>
              </div>
            </div>
            <p v-else style="color: var(--text-quaternary); font-size: 13px">休息日</p>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTrainingPlans } from '../../api/training'

const plans = ref([])
const selectedPlan = ref(null)

const fetchPlans = async () => {
  try {
    const res = await getTrainingPlans()
    plans.value = res.data.list || res.data
  } catch (e) {
    console.error('获取训练方案失败', e)
  }
}

onMounted(() => fetchPlans())
</script>

<style scoped>
.plan-header { display: flex; justify-content: space-between; align-items: flex-start; }
.plan-header h3 { font-family: var(--font-text); font-size: var(--text-body-strong); color: var(--ink); margin-bottom: 8px; font-weight: 600; letter-spacing: -0.374px; }
.plan-meta { display: flex; align-items: center; gap: 8px; }
.duration { font-family: var(--font-text); font-size: var(--text-caption); color: var(--ink-muted-48); }
.plan-desc { font-family: var(--font-text); font-size: var(--text-caption); color: var(--text-secondary); margin-bottom: 16px; line-height: 1.43; }
.plan-days { display: flex; flex-wrap: wrap; gap: 8px; }
.day-badge { background: var(--accent-blue-light); border-radius: var(--radius-sm); padding: 8px 14px; text-align: center; min-width: 80px; }
.day-badge.rest { background: var(--canvas-parchment); }
.day-num { display: block; font-family: var(--font-text); font-size: 10px; color: var(--ink-muted-48); }
.day-label { display: block; font-family: var(--font-text); font-size: var(--text-caption); font-weight: 600; color: var(--ink); margin-top: 2px; }
</style>
