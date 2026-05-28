<template>
  <el-container class="app-layout">
    <!-- 移动端遮罩 -->
    <transition name="fade">
      <div v-if="showMobileMenu" class="mobile-overlay" @click="showMobileMenu = false"></div>
    </transition>

    <!-- 侧边栏 -->
    <el-aside :width="sidebarWidth" :class="['sidebar', { 'mobile-open': showMobileMenu }]">
      <div class="logo" @click="router.push('/dashboard')">
        <div class="logo-mark">
          <svg width="28" height="28" viewBox="0 0 32 32" fill="none">
            <circle cx="16" cy="16" r="14" fill="url(#logoGrad)" />
            <path d="M10 20 L14 12 L18 18 L22 10" stroke="white" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
            <defs><linearGradient id="logoGrad" x1="0" y1="0" x2="32" y2="32"><stop stop-color="#0066cc"/><stop offset="1" stop-color="#2997ff"/></linearGradient></defs>
          </svg>
        </div>
        <span v-show="!isCollapse" class="logo-text">TiSu</span>
      </div>

      <el-menu
        :default-active="route.path"
        :collapse="isCollapse"
        router
        background-color="transparent"
        text-color="rgba(255,255,255,0.5)"
        active-text-color="#ffffff"
        class="sidebar-menu"
        @select="showMobileMenu = false"
      >
        <el-menu-item index="/dashboard">
          <el-icon><DataBoard /></el-icon>
          <span>总览</span>
        </el-menu-item>

        <el-sub-menu index="diet">
          <template #title>
            <el-icon><Apple /></el-icon>
            <span>营养饮食</span>
          </template>
          <el-menu-item index="/diet/foods">食物库</el-menu-item>
          <el-menu-item index="/diet/dishes">菜品管理</el-menu-item>
          <el-menu-item index="/diet/plan">饮食计划</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="training">
          <template #title>
            <el-icon><Bicycle /></el-icon>
            <span>训练计划</span>
          </template>
          <el-menu-item index="/training/exercises">动作库</el-menu-item>
          <el-menu-item index="/training/plans">训练方案</el-menu-item>
          <el-menu-item index="/training/calendar">训练日历</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="record">
          <template #title>
            <el-icon><TrendCharts /></el-icon>
            <span>进度记录</span>
          </template>
          <el-menu-item index="/record/weight">体重记录</el-menu-item>
          <el-menu-item index="/record/body">体围记录</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/ai/analysis">
          <el-icon><Cpu /></el-icon>
          <span>AI 分析</span>
        </el-menu-item>

        <el-menu-item index="/settings">
          <el-icon><Setting /></el-icon>
          <span>设置</span>
        </el-menu-item>
      </el-menu>

      <!-- 侧边栏底部 -->
      <div v-show="!isCollapse" class="sidebar-footer">
        <div class="footer-tip">
          <span class="tip-icon">💧</span>
          <div>
            <div class="tip-title">每日提示</div>
            <div class="tip-desc">记得多喝水 — 每天 8 杯水</div>
          </div>
        </div>
      </div>
    </el-aside>

    <el-container>
      <!-- 顶栏 -->
      <el-header class="header">
        <div class="header-left">
          <el-icon class="menu-btn" @click="toggleSidebar" size="20">
            <component :is="isMobile ? (showMobileMenu ? 'Close' : 'Menu') : (isCollapse ? 'Expand' : 'Fold')" />
          </el-icon>
          <div class="greeting">
            <span class="greeting-text">{{ greeting }}</span>
            <span class="greeting-name">{{ userStore.userInfo.nickname }}</span>
          </div>
        </div>
        <div class="header-right">
          <div class="header-icon-btn" @click="router.push('/settings')">
            <el-icon size="18"><Bell /></el-icon>
          </div>
          <el-dropdown>
            <div class="user-pill">
              <el-avatar :size="28" icon="UserFilled" class="user-avatar" />
              <span class="user-name">{{ userStore.userInfo.nickname }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/settings')">设置</el-dropdown-item>
                <el-dropdown-item @click="router.push('/admin')">管理后台</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容 -->
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="page" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isCollapse = ref(false)
const showMobileMenu = ref(false)
const isMobile = ref(false)

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 6) return '夜深了'
  if (h < 12) return '早上好'
  if (h < 18) return '下午好'
  return '晚上好'
})

const sidebarWidth = computed(() => {
  if (isMobile.value) return '260px'
  return isCollapse.value ? '72px' : '260px'
})

const toggleSidebar = () => {
  if (isMobile.value) showMobileMenu.value = !showMobileMenu.value
  else isCollapse.value = !isCollapse.value
}

const checkMobile = () => {
  isMobile.value = window.innerWidth < 768
  if (isMobile.value) isCollapse.value = false
}

const handleLogout = () => {
  userStore.logout()
  router.push('/login')
}

onMounted(() => {
  checkMobile()
  window.addEventListener('resize', checkMobile)
  userStore.fetchProfile()
})
onUnmounted(() => { window.removeEventListener('resize', checkMobile) })
</script>

<style scoped>
.app-layout { height: 100vh; background: var(--canvas-parchment); }

/* ===== Sidebar: global-nav surface ===== */
.sidebar {
  background: var(--surface-black);
  transition: width var(--duration-slow) var(--ease-out);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  z-index: 100;
  border-right: none;
}

.logo {
  height: 44px;
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 0 20px;
  flex-shrink: 0;
}

.logo-mark {
  flex-shrink: 0;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.06);
  border-radius: var(--radius-sm);
}

.logo-text {
  font-family: var(--font-display);
  font-size: var(--text-tagline);
  font-weight: 600;
  color: var(--body-on-dark);
  letter-spacing: 0.231px;
}

.sidebar-menu {
  flex: 1;
  overflow-y: auto;
  padding: 8px 10px;
}

.sidebar-menu:not(.el-menu--collapse) { width: 260px; }

.sidebar-menu .el-menu-item,
.sidebar-menu :deep(.el-sub-menu__title) {
  border-radius: var(--radius-sm) !important;
  margin: 2px 0;
  height: 40px !important;
  line-height: 40px !important;
  font-family: var(--font-text) !important;
  font-size: var(--text-nav-link) !important;
  font-weight: 400 !important;
  letter-spacing: -0.12px;
  transition: all var(--duration-fast) var(--ease-out);
}

.sidebar-menu .el-menu-item:hover,
.sidebar-menu :deep(.el-sub-menu__title:hover) {
  background: rgba(255, 255, 255, 0.06) !important;
}

.sidebar-menu .el-menu-item.is-active {
  background: rgba(255, 255, 255, 0.1) !important;
  color: var(--body-on-dark) !important;
  font-weight: 600;
}

.sidebar-footer {
  padding: 16px;
  flex-shrink: 0;
}

.footer-tip {
  display: flex;
  gap: 10px;
  align-items: flex-start;
  padding: 14px;
  background: rgba(255, 255, 255, 0.04);
  border-radius: var(--radius-sm);
  border: 0.5px solid rgba(255, 255, 255, 0.06);
}

.tip-icon { font-size: 18px; flex-shrink: 0; }
.tip-title {
  font-family: var(--font-text);
  font-size: var(--text-fine-print);
  color: rgba(255,255,255,0.7);
  font-weight: 600;
  margin-bottom: 2px;
}
.tip-desc {
  font-family: var(--font-text);
  font-size: 10px;
  color: rgba(255,255,255,0.35);
}

/* ===== Header: sub-nav-frosted surface ===== */
.header {
  background: rgba(245, 245, 247, 0.8);
  backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 var(--space-lg);
  border-bottom: 0.5px solid var(--divider-soft);
  height: 52px !important;
  z-index: 10;
}

.header-left { display: flex; align-items: center; gap: var(--space-md); }
.header-right { display: flex; align-items: center; gap: var(--space-md); }

.menu-btn {
  cursor: pointer;
  color: var(--ink-muted-48);
  transition: color var(--duration-fast);
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-sm);
}

.menu-btn:hover { color: var(--ink); background: rgba(0, 0, 0, 0.04); }

.greeting { display: flex; align-items: baseline; gap: 8px; }
.greeting-text {
  font-family: var(--font-text);
  font-size: var(--text-caption);
  color: var(--ink-muted-48);
  font-weight: 400;
}
.greeting-name {
  font-family: var(--font-text);
  font-size: var(--text-caption);
  color: var(--ink);
  font-weight: 600;
}

.header-icon-btn {
  width: 44px;
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--radius-full);
  cursor: pointer;
  color: var(--ink-muted-48);
  transition: all var(--duration-fast);
}

.header-icon-btn:hover { background: rgba(0, 0, 0, 0.04); color: var(--ink); }

.user-pill {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 12px 4px 4px;
  border-radius: var(--radius-pill);
  transition: background var(--duration-fast);
}

.user-pill:hover { background: rgba(0, 0, 0, 0.04); }
.user-avatar { background: var(--primary); }
.user-name {
  font-family: var(--font-text);
  font-size: var(--text-caption);
  color: var(--ink);
  font-weight: 400;
}

/* ===== Main Content ===== */
.main-content {
  background: var(--canvas-parchment);
  padding: var(--space-xl);
  overflow-y: auto;
}

/* ===== Animations ===== */
.fade-enter-active, .fade-leave-active { transition: opacity var(--duration-normal) var(--ease-out); }
.fade-enter-from, .fade-leave-to { opacity: 0; }

.page-enter-active, .page-leave-active {
  transition: opacity var(--duration-normal) var(--ease-out), transform var(--duration-normal) var(--ease-out);
}
.page-enter-from { opacity: 0; transform: translateY(8px); }
.page-leave-to { opacity: 0; transform: translateY(-4px); }

/* ===== Responsive ===== */
.mobile-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(4px);
  z-index: 99;
}

@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    top: 0;
    left: -260px;
    bottom: 0;
    transition: left var(--duration-slow) var(--ease-out);
  }
  .sidebar.mobile-open { left: 0; }
  .main-content { padding: var(--space-md); }
  .greeting-text, .user-name { display: none; }
  .header { padding: 0 var(--space-md); }
}

@media (min-width: 769px) and (max-width: 1024px) {
  .main-content { padding: var(--space-lg); }
}
</style>
