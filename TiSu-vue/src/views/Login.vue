<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <div class="logo-mark">
          <svg width="40" height="40" viewBox="0 0 32 32" fill="none">
            <circle cx="16" cy="16" r="14" fill="url(#loginGrad)" />
            <path d="M10 20 L14 12 L18 18 L22 10" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
            <defs><linearGradient id="loginGrad" x1="0" y1="0" x2="32" y2="32"><stop stop-color="#0066cc"/><stop offset="1" stop-color="#2997ff"/></linearGradient></defs>
          </svg>
        </div>
        <h1>体塑</h1>
        <p>你的私人健康塑形伙伴</p>
      </div>

      <el-tabs v-model="activeTab" class="login-tabs">
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" label-position="top">
            <el-form-item label="用户名">
              <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User" size="large" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" size="large" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="large" class="login-btn" @click="handleLogin">登 录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="注册账号" name="register">
          <el-form :model="registerForm" label-position="top">
            <el-form-item label="用户名">
              <el-input v-model="registerForm.username" placeholder="请设置用户名" prefix-icon="User" size="large" />
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="registerForm.email" placeholder="请输入邮箱" prefix-icon="Message" size="large" />
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="registerForm.password" type="password" placeholder="请设置密码" prefix-icon="Lock" size="large" show-password />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="large" class="login-btn" @click="handleRegister">立即注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <div class="login-divider"></div>
      <div class="login-footer">
        <span>每一步都是迈向更好自己的积累</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { login, register } from '../api/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const activeTab = ref('login')
const loginForm = reactive({ username: '', password: '' })
const registerForm = reactive({ username: '', email: '', password: '' })
const loading = ref(false)

const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) { ElMessage.warning('请输入用户名和密码'); return }
  loading.value = true
  try {
    const res = await login(loginForm)
    userStore.setLogin(res.data.token, { id: res.data.userId, nickname: res.data.nickname })
    await userStore.fetchProfile()
    ElMessage.success('欢迎回来')
    router.push('/dashboard')
  } catch (e) {
    // error handled by interceptor
  } finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  if (!registerForm.username || !registerForm.email || !registerForm.password) { ElMessage.warning('请填写所有字段'); return }
  loading.value = true
  try {
    await register(registerForm)
    ElMessage.success('注册成功，请登录')
    activeTab.value = 'login'
    loginForm.username = registerForm.username
    loginForm.password = ''
  } catch (e) {
    // error handled by interceptor
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--canvas);
  position: relative;
}

.login-card {
  width: 400px;
  background: var(--canvas);
  border-radius: var(--radius-lg);
  padding: 48px 40px 36px;
  box-shadow: 0 40px 100px rgba(0, 0, 0, 0.06);
  border: 1px solid var(--hairline);
  position: relative;
}

.login-header { text-align: center; margin-bottom: 32px; }

.logo-mark {
  display: inline-flex;
  width: 64px; height: 64px;
  align-items: center;
  justify-content: center;
  background: rgba(0, 102, 204, 0.06);
  border-radius: var(--radius-lg);
  margin-bottom: 16px;
}

.login-header h1 {
  font-family: var(--font-display);
  font-size: var(--text-display-md);
  color: var(--ink);
  font-weight: 600;
  letter-spacing: -0.374px;
}

.login-header p {
  font-family: var(--font-text);
  color: var(--ink-muted-48);
  font-size: var(--text-caption);
  margin-top: 4px;
  font-weight: 400;
}

.login-btn {
  width: 100%;
  height: 44px !important;
  font-size: var(--text-body) !important;
  font-weight: 400 !important;
  padding: 11px 22px !important;
}

.login-divider {
  height: 1px;
  background: var(--divider-soft);
  margin: 20px 0;
}

.login-footer {
  text-align: center;
  font-family: var(--font-text);
  font-size: var(--text-caption);
  color: var(--ink-muted-48);
  font-weight: 400;
}

:deep(.el-tabs__nav-wrap::after) { display: none; }

@media (max-width: 480px) {
  .login-card {
    width: calc(100% - 32px);
    padding: 32px 24px 24px;
  }
}
</style>
