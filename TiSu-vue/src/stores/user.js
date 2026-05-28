import { defineStore } from 'pinia'
import { getProfile, updateProfile as updateProfileApi, getSettings, updateSettings as updateSettingsApi } from '../api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: {
      id: null,
      username: '',
      nickname: '',
      avatar: '',
      email: ''
    },
    profile: {
      gender: '',
      age: null,
      height: null,
      currentWeight: null,
      targetWeight: null,
      bodyFat: null,
      goal: '',
      activityLevel: '',
      dailyCalorieTarget: null
    },
    settings: {
      aiApiKey: '',
      aiModel: 'gpt-4o',
      unitPreference: 'metric',
      notificationEnabled: 1
    }
  }),
  getters: {
    isLoggedIn: (state) => !!state.token,
    bmi: (state) => {
      if (!state.profile.height || !state.profile.currentWeight) return '--'
      const h = state.profile.height / 100
      return (state.profile.currentWeight / (h * h)).toFixed(1)
    }
  },
  actions: {
    setLogin(token, userInfo) {
      this.token = token
      this.userInfo = { ...this.userInfo, ...userInfo }
      localStorage.setItem('token', token)
    },
    logout() {
      this.token = ''
      this.userInfo = { id: null, username: '', nickname: '', avatar: '', email: '' }
      localStorage.removeItem('token')
    },
    async fetchProfile() {
      try {
        const res = await getProfile()
        const data = res.data
        this.userInfo = {
          id: data.id,
          username: data.username,
          nickname: data.nickname,
          avatar: data.avatar,
          email: data.email
        }
        this.profile = {
          gender: data.gender || '',
          age: data.age,
          height: data.height,
          currentWeight: data.currentWeight,
          targetWeight: data.targetWeight,
          bodyFat: data.bodyFat,
          goal: data.fitnessGoal || '',
          activityLevel: data.activityLevel || '',
          dailyCalorieTarget: data.dailyCalorieTarget
        }
      } catch (e) {
        console.error('获取用户信息失败', e)
      }
    },
    async updateProfile(data) {
      await updateProfileApi(data)
      Object.assign(this.profile, data)
    },
    async fetchSettings() {
      try {
        const res = await getSettings()
        this.settings = { ...this.settings, ...res.data }
      } catch (e) {
        console.error('获取设置失败', e)
      }
    },
    async updateSettings(data) {
      await updateSettingsApi(data)
      Object.assign(this.settings, data)
    }
  }
})
