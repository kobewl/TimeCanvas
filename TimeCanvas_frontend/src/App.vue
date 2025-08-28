<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const isDarkMode = ref(true)

const toggleTheme = () => {
  isDarkMode.value = !isDarkMode.value
}

const isActive = (path: string) => {
  return route.path === path
}

const navigate = (path: string) => {
  router.push(path)
}

const navItems = computed(() => {
  return [
    { path: '/', label: '首页', icon: '🏠' },
    { path: '/diary', label: '日记', icon: '📖' },
    { path: '/ai-diary', label: 'AI日记', icon: '🤖' },
    { path: '/template-square', label: '模板广场', icon: '🎨' },
    { path: '/todos', label: '待办', icon: '✅' },
    { path: '/knowledge', label: '知识文档', icon: '📚' },
    { path: '/knowledge-management', label: '知识管理', icon: '📋' },
    { path: '/profile', label: '个人中心', icon: '⚙️' },
  ]
})

const isKnowledgePage = computed(() => {
  return route.path === '/knowledge'
})
</script>

<template>
  <div class="app-root" :class="{ 'dark-mode': isDarkMode, 'light-mode': !isDarkMode }">
    <!-- 顶部导航栏 -->
    <header class="app-header">
      <div class="header-content">
        <div class="logo-section">
          <div class="logo">
            <span class="logo-icon">🤖</span>
            <span class="logo-text">TimeCanvas</span>
          </div>
        </div>
        
        <nav class="nav-section">
          <a 
            v-for="item in navItems" 
            :key="item.path"
            @click="navigate(item.path)"
            :class="['nav-item', { active: isActive(item.path) }]"
          >
            <span class="nav-icon">{{ item.icon }}</span>
            <span class="nav-text">{{ item.label }}</span>
          </a>
        </nav>
        
        <div class="header-actions">
          <button class="theme-toggle" @click="toggleTheme">
            <span v-if="isDarkMode">🌙</span>
            <span v-else">☀️</span>
          </button>
          <button class="user-menu">
            <span class="user-avatar">👤</span>
          </button>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <router-view />
    </main>

    <!-- 底部信息 -->
    <footer class="app-footer" v-if="!isKnowledgePage">
      <div class="footer-content">
        <p>© 2025 TimeCanvas | 智能时光绘 | Powered by Vue3 + Element Plus</p>
        <p>作者：wangliang | 一个基于AI的智能日记与生活管理系统</p>
      </div>
    </footer>
  </div>
</template>



<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen', 'Ubuntu', 'Cantarell', sans-serif;
  line-height: 1.6;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 应用根容器 */
.app-root {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
}

/* 暗色主题 */
.app-root.dark-mode {
  background: #0f0f0f;
  color: #e4e4e4;
}

/* 亮色主题 */
.app-root.light-mode {
  background: #ffffff;
  color: #333333;
}

/* 顶部导航栏 */
.app-header {
  position: sticky;
  top: 0;
  z-index: 1000;
  backdrop-filter: blur(12px);
  border-bottom: 1px solid;
  transition: all 0.3s ease;
}

.dark-mode .app-header {
  background: rgba(26, 26, 26, 0.95);
  border-bottom-color: #333;
}

.light-mode .app-header {
  background: rgba(255, 255, 255, 0.95);
  border-bottom-color: #e0e0e0;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 24px;
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* Logo 区域 */
.logo-section {
  flex: 0 0 auto;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 700;
  font-size: 20px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.logo-icon {
  font-size: 24px;
}

.dark-mode .logo-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.light-mode .logo-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* 导航区域 */
.nav-section {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  justify-content: center;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  border-radius: 12px;
  text-decoration: none;
  font-weight: 500;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  overflow: hidden;
}

.nav-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.1), transparent);
  transition: left 0.5s ease;
}

.nav-item:hover::before {
  left: 100%;
}

.dark-mode .nav-item {
  color: #b3b3b3;
}

.dark-mode .nav-item:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #ffffff;
  transform: translateY(-1px);
}

.dark-mode .nav-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.light-mode .nav-item {
  color: #666666;
}

.light-mode .nav-item:hover {
  background: rgba(0, 0, 0, 0.05);
  color: #333333;
  transform: translateY(-1px);
}

.light-mode .nav-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #ffffff;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.nav-icon {
  font-size: 16px;
}

.nav-text {
  font-size: 14px;
}

/* 头部操作区 */
.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 0 0 auto;
}

.theme-toggle,
.user-menu {
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  transition: all 0.2s ease;
}

.dark-mode .theme-toggle,
.dark-mode .user-menu {
  background: rgba(255, 255, 255, 0.1);
  color: #e4e4e4;
}

.dark-mode .theme-toggle:hover,
.dark-mode .user-menu:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: scale(1.05);
}

.light-mode .theme-toggle,
.light-mode .user-menu {
  background: rgba(0, 0, 0, 0.05);
  color: #666666;
}

.light-mode .theme-toggle:hover,
.light-mode .user-menu:hover {
  background: rgba(0, 0, 0, 0.1);
  transform: scale(1.05);
}

/* 主要内容区域 */
.main-content {
  flex: 1;
  min-height: calc(100vh - 64px);
}

/* 底部 */
.app-footer {
  padding: 24px;
  text-align: center;
  font-size: 14px;
  border-top: 1px solid;
  transition: all 0.3s ease;
}

.dark-mode .app-footer {
  background: #1a1a1a;
  border-top-color: #333;
  color: #888;
}

.light-mode .app-footer {
  background: #f8f9fa;
  border-top-color: #e0e0e0;
  color: #666;
}

.footer-content p {
  margin: 4px 0;
}

.footer-content p:first-child {
  font-weight: 600;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .header-content {
    padding: 0 20px;
  }
  
  .nav-section {
    gap: 4px;
  }
  
  .nav-item {
    padding: 8px 12px;
  }
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
  }
  
  .nav-section {
    display: none; /* 移动端隐藏导航，可以后续添加汉堡包菜单 */
  }
  
  .logo {
    font-size: 18px;
  }
  
  .logo-icon {
    font-size: 20px;
  }
}

@media (max-width: 480px) {
  .header-content {
    height: 56px;
    padding: 0 12px;
  }
  
  .theme-toggle,
  .user-menu {
    width: 36px;
    height: 36px;
    font-size: 16px;
  }
}

/* 滚动条美化 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

.dark-mode ::-webkit-scrollbar-thumb {
  background: #404040;
  border-radius: 4px;
}

.dark-mode ::-webkit-scrollbar-thumb:hover {
  background: #555555;
}

.light-mode ::-webkit-scrollbar-thumb {
  background: #cccccc;
  border-radius: 4px;
}

.light-mode ::-webkit-scrollbar-thumb:hover {
  background: #aaaaaa;
}

/* 全局动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.fade-enter-active {
  animation: fadeIn 0.3s ease-out;
}

/* Element Plus 组件样式覆盖 */
.dark-mode .el-button {
  background: #404040 !important;
  border-color: #555 !important;
  color: #e4e4e4 !important;
}

.dark-mode .el-button:hover {
  background: #4a4a4a !important;
  border-color: #666 !important;
}

.dark-mode .el-button--primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  border-color: transparent !important;
}

.dark-mode .el-button--primary:hover {
  background: linear-gradient(135deg, #7c3aed 0%, #8b5cf6 100%) !important;
}
</style>
