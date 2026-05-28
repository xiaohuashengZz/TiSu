import request from '../utils/request'

export function getExercises(params) {
  return request.get('/api/exercises', { params })
}

export function createExercise(data) {
  return request.post('/api/exercises', data)
}
