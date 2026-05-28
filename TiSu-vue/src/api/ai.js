import request from '../utils/request'

export function analyze(data) {
  return request.post('/api/ai/analyze', data)
}

export function getReports() {
  return request.get('/api/ai/reports')
}

export function testConnection(data) {
  return request.post('/api/ai/test-connection', data)
}
