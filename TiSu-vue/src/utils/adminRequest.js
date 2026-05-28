import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const adminRequest = axios.create({
  baseURL: '',
  timeout: 15000
})

adminRequest.interceptors.request.use(
  config => {
    const token = localStorage.getItem('admin_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => Promise.reject(error)
)

adminRequest.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      if (res.code === 401) {
        localStorage.removeItem('admin_token')
        router.push('/admin/login')
      }
      return Promise.reject(new Error(res.message))
    }
    return res
  },
  error => {
    if (error.response) {
      const { status, data } = error.response
      if (status === 401) {
        localStorage.removeItem('admin_token')
        router.push('/admin/login')
        ElMessage.error('登录已过期，请重新登录')
      } else {
        ElMessage.error(data?.message || `请求错误 (${status})`)
      }
    } else {
      ElMessage.error('网络连接失败')
    }
    return Promise.reject(error)
  }
)

export default adminRequest
