import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/HomeView.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginView.vue'),
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/RegisterView.vue'),
  },
  {
    path: '/diary',
    name: 'Diary',
    component: () => import('../views/DiaryView.vue'),
  },
  {
    path: '/ai-diary',
    name: 'AiDiary',
    component: () => import('../views/AiDiaryView.vue'),
  },
  {
    path: '/template-square',
    name: 'TemplateSquare',
    component: () => import('../views/TemplateSquareView.vue'),
  },
  {
    path: '/todos',
    name: 'Todos',
    component: () => import('../views/TodosView.vue'),
  },
  {
    path: '/knowledge',
    name: 'Knowledge',
    component: () => import('../views/KnowledgeDocumentView.vue'),
  },
  {
    path: '/knowledge-management',
    name: 'KnowledgeManagement', 
    component: () => import('../views/KnowledgeManagementView.vue'),
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/ProfileView.vue'),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router; 