import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true }
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/diary',
      name: 'diary',
      component: () => import('../views/DiaryView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/ai-diary',
      name: 'ai-diary',
      component: () => import('../views/AiDiaryView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/templates',
      name: 'templates',
      component: () => import('../views/TemplateSquareView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('../views/ProfileView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/reports',
      name: 'reports',
      component: () => import('../views/ReportsView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/todos',
      name: 'todos',
      component: () => import('../views/TodosView.vue'),
      meta: { requiresAuth: true }
    }
  ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
  const isAuthenticated = localStorage.getItem('token');
  
  console.log('路由守卫:', {
    to: to.path,
    requiresAuth,
    isAuthenticated,
    tokenValue: localStorage.getItem('token'),
    user: localStorage.getItem('user')
  });
  
  if (requiresAuth && !isAuthenticated) {
    // 需要登录但未登录，重定向到登录页
    console.log('未登录，重定向到登录页');
    next('/login');
  } else if (to.path === '/login' && isAuthenticated) {
    // 已登录用户访问登录页，重定向到首页
    console.log('已登录，重定向到首页');
    next('/');
  } else {
    console.log('正常导航');
    next();
  }
});

export default router
