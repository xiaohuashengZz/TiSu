import request from '../utils/request'

export function getTrainingPlans() {
  return request.get('/api/training-plans')
}

export function getTrainingPlanDetail(id) {
  return request.get(`/api/training-plans/${id}`)
}

export function createTrainingPlan(data) {
  return request.post('/api/training-plans', data)
}

export function deleteTrainingPlan(id) {
  return request.delete(`/api/training-plans/${id}`)
}

export function getTrainingLogs(params) {
  return request.get('/api/training-logs', { params })
}

export function createTrainingLog(data) {
  return request.post('/api/training-logs', data)
}
