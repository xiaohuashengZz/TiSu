import { createRouter, createWebHistory } from 'vue-router'
import AppLayout from '../components/AppLayout.vue'
import AdminLayout from '../components/AdminLayout.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: AppLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '数据看板', icon: 'DataBoard' }
      },
      {
        path: 'diet/foods',
        name: 'FoodLibrary',
        component: () => import('../views/diet/FoodLibrary.vue'),
        meta: { title: '食物库', icon: 'Apple' }
      },
      {
        path: 'diet/dishes',
        name: 'DishManagement',
        component: () => import('../views/diet/DishManagement.vue'),
        meta: { title: '菜品管理', icon: 'KnifeFork' }
      },
      {
        path: 'diet/plan',
        name: 'DietPlan',
        component: () => import('../views/diet/DietPlan.vue'),
        meta: { title: '饮食计划', icon: 'Calendar' }
      },
      {
        path: 'training/exercises',
        name: 'ExerciseLibrary',
        component: () => import('../views/training/ExerciseLibrary.vue'),
        meta: { title: '动作库', icon: 'Bicycle' }
      },
      {
        path: 'training/plans',
        name: 'TrainingPlans',
        component: () => import('../views/training/TrainingPlans.vue'),
        meta: { title: '训练计划', icon: 'List' }
      },
      {
        path: 'training/calendar',
        name: 'TrainingCalendar',
        component: () => import('../views/training/TrainingCalendar.vue'),
        meta: { title: '训练日历', icon: 'Calendar' }
      },
      {
        path: 'record/weight',
        name: 'WeightRecord',
        component: () => import('../views/record/WeightRecord.vue'),
        meta: { title: '体重记录', icon: 'TrendCharts' }
      },
      {
        path: 'record/body',
        name: 'BodyRecord',
        component: () => import('../views/record/BodyRecord.vue'),
        meta: { title: '围度记录', icon: 'Aim' }
      },
      {
        path: 'ai/analysis',
        name: 'AiAnalysis',
        component: () => import('../views/ai/AiAnalysis.vue'),
        meta: { title: 'AI 分析', icon: 'Cpu' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('../views/settings/Settings.vue'),
        meta: { title: '系统设置', icon: 'Setting' }
      }
    ]
  },
  {
    path: '/admin',
    component: AdminLayout,
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('../views/admin/AdminDashboard.vue'),
        meta: { title: '管理首页' }
      },
      {
        path: 'foods',
        name: 'AdminFoods',
        component: () => import('../views/admin/AdminFoods.vue'),
        meta: { title: '食物管理' }
      },
      {
        path: 'dishes',
        name: 'AdminDishes',
        component: () => import('../views/admin/AdminDishes.vue'),
        meta: { title: '菜品管理' }
      },
      {
        path: 'exercises',
        name: 'AdminExercises',
        component: () => import('../views/admin/AdminExercises.vue'),
        meta: { title: '动作管理' }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('../views/admin/AdminUsers.vue'),
        meta: { title: '用户管理' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title || '体塑'} - 体塑`

  const token = localStorage.getItem('token')
  const adminToken = localStorage.getItem('admin_token')

  // 管理端路由需要管理员认证
  if (to.path.startsWith('/admin')) {
    if (!adminToken) {
      window.location.href = '/login'
      return
    }
    next()
    return
  }

  // 用户端路由（登录页除外）需要用户认证
  if (to.path !== '/login' && !token) {
    next('/login')
    return
  }

  // 已登录用户访问登录页跳转到首页
  if (to.path === '/login' && token) {
    next('/dashboard')
    return
  }

  next()
})

export default router
