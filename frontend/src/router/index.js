/**
 * Vue Router 路由配置
 * 定义系统中所有页面的路由规则
 */
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  { path: '/', name: 'Home', component: () => import('../views/Home.vue') },
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/Register.vue') },
  { path: '/knowledge', name: 'Knowledge', component: () => import('../views/Knowledge.vue') },
  { path: '/article/:id', name: 'ArticleDetail', component: () => import('../views/ArticleDetail.vue') },
  { path: '/community', name: 'Community', component: () => import('../views/Community.vue') },
  { path: '/post/:id', name: 'PostDetail', component: () => import('../views/PostDetail.vue') },
  { path: '/profile', name: 'Profile', component: () => import('../views/Profile.vue'), meta: { requireAuth: true } },
  { path: '/my-articles', name: 'MyArticles', component: () => import('../views/MyArticles.vue'), meta: { requireAuth: true } },
  { path: '/publish', name: 'Publish', component: () => import('../views/Publish.vue'), meta: { requireAuth: true } },
  { path: '/admin', name: 'Admin', component: () => import('../views/Admin.vue'), meta: { requireAuth: true, requireAdmin: true } }
]

const router = new VueRouter({
  mode: 'hash',  // 使用 hash 模式，兼容静态文件部署
  routes
})

// 路由守卫：检查登录状态和权限
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requireAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
