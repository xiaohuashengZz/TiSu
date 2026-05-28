import adminRequest from '../utils/adminRequest'

// 认证
export function adminLogin(data) {
  return adminRequest.post('/api/admin/auth/login', data)
}

export function adminLogout() {
  return adminRequest.post('/api/admin/auth/logout')
}

export function getAdminInfo() {
  return adminRequest.get('/api/admin/auth/info')
}

// 看板
export function getAdminStats() {
  return adminRequest.get('/api/admin/dashboard/stats')
}

export function getAdminActivities(params) {
  return adminRequest.get('/api/admin/dashboard/activities', { params })
}

export function getAdminOverview() {
  return adminRequest.get('/api/admin/dashboard/overview')
}

// 食物管理
export function getAdminFoods(params) {
  return adminRequest.get('/api/admin/foods', { params })
}

export function createAdminFood(data) {
  return adminRequest.post('/api/admin/foods', data)
}

export function updateAdminFood(id, data) {
  return adminRequest.put(`/api/admin/foods/${id}`, data)
}

export function deleteAdminFood(id) {
  return adminRequest.delete(`/api/admin/foods/${id}`)
}

// 菜品管理
export function getAdminDishes(params) {
  return adminRequest.get('/api/admin/dishes', { params })
}

export function createAdminDish(data) {
  return adminRequest.post('/api/admin/dishes', data)
}

export function updateAdminDish(id, data) {
  return adminRequest.put(`/api/admin/dishes/${id}`, data)
}

export function deleteAdminDish(id) {
  return adminRequest.delete(`/api/admin/dishes/${id}`)
}

// 动作管理
export function getAdminExercises(params) {
  return adminRequest.get('/api/admin/exercises', { params })
}

export function createAdminExercise(data) {
  return adminRequest.post('/api/admin/exercises', data)
}

export function updateAdminExercise(id, data) {
  return adminRequest.put(`/api/admin/exercises/${id}`, data)
}

export function deleteAdminExercise(id) {
  return adminRequest.delete(`/api/admin/exercises/${id}`)
}

// 用户管理
export function getAdminUsers(params) {
  return adminRequest.get('/api/admin/users', { params })
}

export function updateAdminUserStatus(id, status) {
  return adminRequest.put(`/api/admin/users/${id}/status`, { status })
}

export function deleteAdminUser(id) {
  return adminRequest.delete(`/api/admin/users/${id}`)
}

// 类型管理
export function getAdminTypes(params) {
  return adminRequest.get('/api/admin/types', { params })
}

export function createAdminType(data) {
  return adminRequest.post('/api/admin/types', data)
}

export function updateAdminType(id, data) {
  return adminRequest.put(`/api/admin/types/${id}`, data)
}

export function deleteAdminType(id) {
  return adminRequest.delete(`/api/admin/types/${id}`)
}
