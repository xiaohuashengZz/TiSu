import request from '../utils/request'

export function getDayPlan(date) {
  return request.get('/api/meal-plans', { params: { date } })
}

export function getWeekPlan(startDate) {
  return request.get('/api/meal-plans/week', { params: { startDate } })
}

export function getMonthPlan(year, month) {
  return request.get('/api/meal-plans/month', { params: { year, month } })
}

export function createMealPlan(data) {
  return request.post('/api/meal-plans', data)
}
