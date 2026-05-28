import request from '../utils/request'

export function getFoods(params) {
  return request.get('/api/foods', { params })
}

export function createFood(data) {
  return request.post('/api/foods', data)
}

export function updateFood(id, data) {
  return request.put(`/api/foods/${id}`, data)
}

export function deleteFood(id) {
  return request.delete(`/api/foods/${id}`)
}
