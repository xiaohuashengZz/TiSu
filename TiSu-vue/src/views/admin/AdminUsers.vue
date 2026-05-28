<template>
  <div class="admin-users">
    <div class="page-header">
      <h2>用户管理</h2>
    </div>

    <!-- 筛选栏 -->
    <el-card shadow="never" style="margin-bottom: var(--space-md)">
      <div class="filter-bar">
        <el-radio-group v-model="statusFilter" size="small">
          <el-radio-button value="all">全部</el-radio-button>
          <el-radio-button value="active">正常</el-radio-button>
          <el-radio-button value="disabled">已禁用</el-radio-button>
        </el-radio-group>
        <el-input v-model="keyword" placeholder="搜索用户..." clearable size="small" style="width: 200px" />
      </div>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table :data="users" stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="nickname" label="昵称" min-width="120" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="goal" label="健身目标" width="100" />
        <el-table-column prop="registerDate" label="注册时间" width="120" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'danger'" size="small" effect="plain">
              {{ row.status === 'active' ? '正常' : '已禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button :type="row.status === 'active' ? 'warning' : 'success'" link size="small" @click="toggleStatus(row)">
              {{ row.status === 'active' ? '禁用' : '启用' }}
            </el-button>
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
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { getAdminUsers, updateAdminUserStatus, deleteAdminUser } from '../../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const users = ref([])
const total = ref(0)
const statusFilter = ref('all')
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(10)

const fetchUsers = async () => {
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    if (statusFilter.value !== 'all') params.status = statusFilter.value
    if (keyword.value) params.keyword = keyword.value
    const res = await getAdminUsers(params)
    users.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error('获取用户列表失败', e)
  }
}

watch([statusFilter, keyword, pageSize], () => {
  currentPage.value = 1
  fetchUsers()
})

watch(currentPage, () => {
  fetchUsers()
})

const toggleStatus = (user) => {
  const newStatus = user.status === 'active' ? 'disabled' : 'active'
  const action = newStatus === 'disabled' ? '禁用' : '启用'
  ElMessageBox.confirm(`确定${action}用户「${user.nickname}」吗？`, '', { type: 'warning' })
    .then(async () => {
      try {
        await updateAdminUserStatus(user.id, newStatus)
        ElMessage.success(`已${action}`)
        fetchUsers()
      } catch (e) {
        ElMessage.error(`${action}失败`)
      }
    })
    .catch(() => {})
}

const handleDelete = (user) => {
  ElMessageBox.confirm(`确定删除用户「${user.nickname}」吗？此操作不可恢复。`, '', { type: 'warning' })
    .then(async () => {
      try {
        await deleteAdminUser(user.id)
        ElMessage.success('已删除')
        fetchUsers()
      } catch (e) {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.filter-bar { display: flex; gap: var(--space-sm); align-items: center; flex-wrap: wrap; }
.pagination-bar { display: flex; justify-content: flex-end; margin-top: var(--space-md); }
</style>
