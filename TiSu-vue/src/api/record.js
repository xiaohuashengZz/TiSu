import request from '../utils/request'

export function getWeightLogs(params) {
  return request.get('/api/weight-logs', { params })
}

export function createWeightLog(data) {
  return request.post('/api/weight-logs', data)
}

export function getBodyMeasurements(params) {
  return request.get('/api/body-measurements', { params })
}

export function createBodyMeasurement(data) {
  return request.post('/api/body-measurements', data)
}
