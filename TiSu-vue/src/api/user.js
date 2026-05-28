import request from '../utils/request'

export function getProfile() {
  return request.get('/api/user/profile')
}

export function updateProfile(data) {
  return request.put('/api/user/profile', data)
}

export function getSettings() {
  return request.get('/api/user/settings')
}

export function updateSettings(data) {
  return request.put('/api/user/settings', data)
}
