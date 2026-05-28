<template>
  <div class="exercise-library">
    <div class="page-header">
      <h2>动作库</h2>
      <div class="header-actions">
        <el-input v-model="searchText" placeholder="搜索动作..." prefix-icon="Search" clearable class="search-input" />
        <el-button type="primary" round><el-icon><Plus /></el-icon>添加</el-button>
      </div>
    </div>

    <div class="group-filter">
      <el-radio-group v-model="selectedGroup">
        <el-radio-button value="全部">全部</el-radio-button>
        <el-radio-button v-for="g in muscleGroups" :key="g" :value="g">{{ g }}</el-radio-button>
      </el-radio-group>
    </div>

    <el-row :gutter="16">
      <el-col :xs="12" :sm="8" :md="6" v-for="ex in filteredExercises" :key="ex.id">
        <el-card class="exercise-card" shadow="never">
          <div class="card-tags">
            <el-tag size="small" effect="plain">{{ ex.muscleGroup }}</el-tag>
            <el-tag size="small">{{ ex.difficulty }}</el-tag>
          </div>
          <h3>{{ ex.name }}</h3>
          <p class="desc">{{ ex.description }}</p>
          <div class="sets-info">
            <span>{{ ex.sets }} 组 × {{ ex.reps }}{{ ex.muscleGroup === '有氧' ? ' 分钟' : ' 次' }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { getExercises } from '../../api/exercise'
import { getTypes } from '../../api/type'

const searchText = ref('')
const selectedGroup = ref('全部')
const exerciseList = ref([])
const muscleGroups = ref([])

const filteredExercises = computed(() => exerciseList.value.filter(e => {
  const s = !searchText.value || e.name.includes(searchText.value)
  const g = selectedGroup.value === '全部' || e.muscleGroup === selectedGroup.value
  return s && g
}))

const fetchExercises = async () => {
  try {
    const params = {}
    if (selectedGroup.value !== '全部') params.muscleGroup = selectedGroup.value
    if (searchText.value) params.keyword = searchText.value
    const res = await getExercises(params)
    exerciseList.value = res.data.list || res.data
  } catch (e) {
    console.error('获取动作列表失败', e)
  }
}

const fetchMuscleGroups = async () => {
  try {
    const res = await getTypes('muscle_group')
    muscleGroups.value = res.data || []
  } catch (e) {
    console.error('获取肌群分类失败', e)
  }
}

watch([searchText, selectedGroup], () => fetchExercises())

onMounted(() => {
  fetchMuscleGroups()
  fetchExercises()
})
</script>

<style scoped>
.header-actions { display: flex; gap: 12px; flex-wrap: wrap; }
.search-input { width: 240px; }
.group-filter { margin-bottom: 20px; overflow-x: auto; }
.exercise-card { margin-bottom: 16px; transition: all var(--duration-normal) var(--ease-out); }
.exercise-card:hover { transform: translateY(-2px); }
.card-tags { display: flex; gap: 6px; margin-bottom: 12px; }
.exercise-card h3 { font-family: var(--font-text); font-size: var(--text-body-strong); color: var(--ink); margin-bottom: 8px; font-weight: 600; letter-spacing: -0.374px; }
.desc { font-family: var(--font-text); font-size: var(--text-caption); color: var(--ink-muted-48); line-height: 1.43; min-height: 40px; margin-bottom: 12px; }
.sets-info { font-family: var(--font-text); font-size: var(--text-caption); color: var(--primary); font-weight: 600; padding-top: 12px; border-top: 1px solid var(--divider-soft); }
@media (max-width: 768px) { .search-input { width: 100%; } }
</style>
