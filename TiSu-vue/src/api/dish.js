import request from '../utils/request'

export function getDishes(params) {
  return request.get('/api/dishes', { params })
}

export function getDishDetail(id) {
  return request.get(`/api/dishes/${id}`)
}

export function createDish(data) {
  return request.post('/api/dishes', data)
}

export function updateDish(id, data) {
  return request.put(`/api/dishes/${id}`, data)
}

export function deleteDish(id) {
  return request.delete(`/api/dishes/${id}`)
}

export function getIngredients(dishId) {
  return request.get(`/api/dishes/${dishId}/ingredients`)
}

export function updateIngredients(dishId, data) {
  return request.put(`/api/dishes/${dishId}/ingredients`, data)
}
