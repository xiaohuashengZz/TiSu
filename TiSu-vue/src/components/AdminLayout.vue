<template>
  <el-container class="admin-layout">
    <transition name="fade">
      <div v-if="showMobileMenu" class="mobile-overlay" @click="showMobileMenu = false"></div>
    </transition>

    <el-aside :width="sidebarWidth" :class="['sidebar', { 'mobile-open': showMobileMenu }]">
      <div class="logo" @click="router.push('/admin/dashboard')">
        <div class="logo-mark">
          <svg width="28" height="28" viewBox="0 0 32 32" fill="none">
            <circle cx="16" cy="16" r="14" fill="url(#adminLogoGrad)" />
            <path d="M10 22 L16 10 L22 22 Z" stroke="white" stroke-width="2" stroke-linejoin="round" fill="none"/>
            <defs><linearGradient id="adminLogoGrad" x1="0" y1="0" x2="32" y2="32"><stop stop-color="#ff9500"/><stop offset="1" stop-color="#ff2d55"/></linearGradient></defs>
          </svg>
        </div>
        <span v-show="!isCollapse" class="logo-text">管理后台</span>
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
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataBoard /></el-icon>
          <span>管理首页</span>
        </el-menu-item>
        <el-menu-item index="/admin/foods">
          <el-icon><Apple /></el-icon>
          <span>食物管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/dishes">
          <el-icon><KnifeFork /></el-icon>
          <span>菜品管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/exercises">
          <el-icon><Bicycle /></el-icon>
          <span>动作管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>

      <div v-show="!isCollapse" class="sidebar-footer">
        <el-button round size="small" @click="router.push('/dashboard')" class="back-btn">
          <el-icon><Back /></el-icon>返回用户端
        </el-button>
      </div>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon class="menu-btn" @click="toggleSidebar" size="20">
            <component :is="isMobile ? (showMobileMenu ? 'Close' : 'Menu') : (isCollapse ? 'Expand' : 'Fold')" />
          </el-icon>
          <span class="page-title">{{ route.meta.title }}</span>
        </div>
        <div class="header-right">
          <el-tag type="warning" effect="plain" size="small">管理员</el-tag>
          <el-avatar :size="32" icon="UserFilled" />
        </div>
      </el-header>

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

const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)
const showMobileMenu = ref(false)
const isMobile = ref(false)

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

onMounted(() => { checkMobile(); window.addEventListener('resize', checkMobile) })
onUnmounted(() => { window.removeEventListener('resize', checkMobile) })
</script>

<style scoped>
.admin-layout { height: 100vh; background: var(--canvas-parchment); }
.sidebar {
  background: var(--surface-black);
  transition: width var(--duration-slow) var(--ease-out);
  overflow: hidden; display: flex; flex-direction: column; z-index: 100; border-right: none;
}
.logo {
  height: 44px; display: flex; align-items: center; gap: 12px;
  cursor: pointer; padding: 0 20px; flex-shrink: 0;
}
.logo-mark {
  flex-shrink: 0; width: 36px; height: 36px;
  display: flex; align-items: center; justify-content: center;
  background: rgba(255, 255, 255, 0.06); border-radius: var(--radius-sm);
}
.logo-text {
  font-family: var(--font-display); font-size: var(--text-tagline);
  font-weight: 600; color: var(--body-on-dark); letter-spacing: 0.231px;
}
.sidebar-menu { flex: 1; overflow-y: auto; padding: 8px 10px; }
.sidebar-menu:not(.el-menu--collapse) { width: 260px; }
.sidebar-menu .el-menu-item,
.sidebar-menu :deep(.el-sub-menu__title) {
  border-radius: var(--radius-sm) !important; margin: 2px 0; height: 40px !important;
  line-height: 40px !important; font-family: var(--font-text) !important;
  font-size: var(--text-nav-link) !important; font-weight: 400 !important;
  letter-spacing: -0.12px; transition: all var(--duration-fast) var(--ease-out);
}
.sidebar-menu .el-menu-item:hover,
.sidebar-menu :deep(.el-sub-menu__title:hover) { background: rgba(255, 255, 255, 0.06) !important; }
.sidebar-menu .el-menu-item.is-active {
  background: rgba(255, 255, 255, 0.1) !important;
  color: var(--body-on-dark) !important; font-weight: 600;
}
.sidebar-footer { padding: 16px; flex-shrink: 0; }
.back-btn { width: 100%; background: rgba(255,255,255,0.06) !important; border: none !important; color: rgba(255,255,255,0.5) !important; }
.back-btn:hover { background: rgba(255,255,255,0.1) !important; color: var(--body-on-dark) !important; }

.header {
  background: rgba(245, 245, 247, 0.8); backdrop-filter: saturate(180%) blur(20px);
  -webkit-backdrop-filter: saturate(180%) blur(20px);
  display: flex; align-items: center; justify-content: space-between;
  padding: 0 var(--space-lg); border-bottom: 0.5px solid var(--divider-soft);
  height: 52px !important; z-index: 10;
}
.header-left { display: flex; align-items: center; gap: var(--space-md); }
.header-right { display: flex; align-items: center; gap: var(--space-md); }
.menu-btn {
  cursor: pointer; color: var(--ink-muted-48); transition: color var(--duration-fast);
  width: 44px; height: 44px; display: flex; align-items: center; justify-content: center;
  border-radius: var(--radius-sm);
}
.menu-btn:hover { color: var(--ink); background: rgba(0, 0, 0, 0.04); }
.page-title {
  font-family: var(--font-text); font-size: var(--text-body-strong);
  font-weight: 600; color: var(--ink); letter-spacing: -0.374px;
}
.main-content { background: var(--canvas-parchment); padding: var(--space-xl); overflow-y: auto; }

.fade-enter-active, .fade-leave-active { transition: opacity var(--duration-normal) var(--ease-out); }
.fade-enter-from, .fade-leave-to { opacity: 0; }
.page-enter-active, .page-leave-active {
  transition: opacity var(--duration-normal) var(--ease-out), transform var(--duration-normal) var(--ease-out);
}
.page-enter-from { opacity: 0; transform: translateY(8px); }
.page-leave-to { opacity: 0; transform: translateY(-4px); }

.mobile-overlay { position: fixed; inset: 0; background: rgba(0, 0, 0, 0.3); backdrop-filter: blur(4px); z-index: 99; }
@media (max-width: 768px) {
  .sidebar { position: fixed; top: 0; left: -260px; bottom: 0; transition: left var(--duration-slow) var(--ease-out); }
  .sidebar.mobile-open { left: 0; }
  .main-content { padding: var(--space-md); }
  .header { padding: 0 var(--space-md); }
}
</style>
