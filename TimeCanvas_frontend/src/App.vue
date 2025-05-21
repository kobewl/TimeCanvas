<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import type { User } from '@/api/index.ts';

const router = useRouter();
const isAuthenticated = ref(false);
const user = ref<User>({
  userName: ''
});

// 检查是否已登录
const checkAuth = () => {
  const token = localStorage.getItem('token');
  isAuthenticated.value = !!token;
  
  if (token) {
    const userInfo = localStorage.getItem('user');
    if (userInfo) {
      user.value = JSON.parse(userInfo);
      console.log('App.vue - 加载的用户信息:', user.value);
    }
  }
};

// 处理登出
const handleLogout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  isAuthenticated.value = false;
  router.push('/login');
};

// 显示用户菜单
const showUserMenu = ref(false);
const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value;
};

// 关闭用户菜单（点击外部区域）
const closeUserMenu = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  if (target && !target.closest('.user-profile')) {
    showUserMenu.value = false;
  }
};

// 计算用户头像显示
const userAvatar = computed(() => {
  return user.value.avatar || getInitialsAvatar(user.value.userName || '');
});

// 获取用户名首字母头像
const getInitialsAvatar = (username: string) => {
  if (!username) return '';
  
  return username.charAt(0).toUpperCase();
};

// 测试点击事件
const testLoginClick = () => {
  console.log('登录按钮被点击了!');
};

// 在组件挂载时检查登录状态
onMounted(() => {
  checkAuth();
  document.addEventListener('click', closeUserMenu);
});
</script>

<template>
  <div id="app" @click="closeUserMenu">
    <header class="app-header">
      <div class="container">
        <div class="logo">
          <img alt="智绘时光" class="logo-img" src="@/assets/logo.png" width="40" height="40" />
          <h1 class="site-title">智绘时光</h1>
        </div>
        
        <nav class="main-nav">
          <RouterLink to="/" class="nav-link">首页</RouterLink>
          <RouterLink to="/diary" class="nav-link">日记</RouterLink>
          <RouterLink to="/ai-diary" class="nav-link">AI写日记</RouterLink>
          <RouterLink to="/templates" class="nav-link">模板广场</RouterLink>
          <RouterLink to="/reports" class="nav-link">报告</RouterLink>
          <RouterLink to="/todos" class="nav-link">待办</RouterLink>
          <RouterLink to="/about" class="nav-link">关于</RouterLink>
        </nav>
        
        <div v-if="isAuthenticated" class="user-area">
          <div class="user-profile" @click.stop="toggleUserMenu">
            <div v-if="user.avatar" class="user-avatar">
              <img :src="user.avatar" alt="User avatar">
            </div>
            <div v-else class="user-avatar user-initials">
              {{ userAvatar }}
            </div>
            <span class="username">{{ user.userName }}</span>
            <span class="dropdown-icon">▼</span>
            
            <div v-if="showUserMenu" class="user-dropdown">
              <ul class="dropdown-menu">
                <li class="dropdown-item">
                  <RouterLink to="/profile" class="dropdown-link">个人资料</RouterLink>
                </li>
                <li class="dropdown-item">
                  <RouterLink to="/settings" class="dropdown-link">设置</RouterLink>
                </li>
                <li class="dropdown-divider"></li>
                <li class="dropdown-item">
                  <button @click="handleLogout" class="dropdown-button">退出登录</button>
                </li>
              </ul>
            </div>
          </div>
        </div>
        
        <div v-else class="auth-links">
          <RouterLink to="/login" class="auth-link" @click="testLoginClick">登录</RouterLink>
        </div>
      </div>
    </header>

    <main class="app-main">
      <RouterView />
    </main>
    
    <footer class="app-footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-section">
            <h3 class="footer-title">智绘时光</h3>
            <p class="footer-desc">基于AI的智能日记管理系统，让记录生活变得简单高效。</p>
          </div>
          
          <div class="footer-section">
            <h3 class="footer-title">功能导航</h3>
            <ul class="footer-links">
              <li><RouterLink to="/diary" class="footer-link">智能日记</RouterLink></li>
              <li><RouterLink to="/reports" class="footer-link">一键报告</RouterLink></li>
              <li><RouterLink to="/todos" class="footer-link">待办管理</RouterLink></li>
            </ul>
          </div>
          
          <div class="footer-section">
            <h3 class="footer-title">资源链接</h3>
            <ul class="footer-links">
              <li><a href="#" class="footer-link">使用帮助</a></li>
              <li><a href="#" class="footer-link">隐私政策</a></li>
              <li><a href="#" class="footer-link">关于我们</a></li>
            </ul>
          </div>
        </div>
        
        <div class="footer-bottom">
          <p>&copy; 2024 智绘时光 - 基于AI的智能日记管理系统</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<style>
/* 全局样式 */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: 'PingFang SC', 'Helvetica Neue', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: #f5f7fa;
  color: #2c3e50;
  line-height: 1.6;
  overflow-x: hidden; /* 防止水平滚动 */
  width: 100%;
  margin: 0;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh; /* 使应用至少占满整个视口高度 */
  width: 100%; /* 占据整个视口宽度 */
  max-width: 100%; /* 确保不会溢出 */
  overflow-x: hidden; /* 防止水平滚动 */
}

.container {
  width: 100%;
  max-width: 100%;
  margin: 0 auto; /* 居中对齐 */
  padding: 0; /* 移除内边距 */
}

/* 头部样式 */
.app-header {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
  width: 100%; /* 确保头部占满宽度 */
}

.app-header .container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  width: 100%;
  max-width: 1440px; /* 设置最大宽度 */
}

.logo {
  display: flex;
  align-items: center;
}

.logo-img {
  margin-right: 10px;
}

.site-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #333;
}

.main-nav {
  display: flex;
  gap: 20px;
  flex-wrap: wrap; /* 允许导航在窄屏上换行 */
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-weight: 500;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.nav-link:hover {
  background-color: #f0f0f0;
}

.router-link-active {
  color: #4caf50;
  background-color: rgba(76, 175, 80, 0.1);
}

/* 用户区域样式 */
.user-area {
  position: relative;
}

.user-profile {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-profile:hover {
  background-color: #f0f0f0;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #4caf50;
  margin-right: 8px;
  overflow: hidden;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-initials {
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
}

.username {
  font-weight: 500;
  margin-right: 5px;
}

.dropdown-icon {
  font-size: 10px;
  color: #666;
}

/* 用户下拉菜单 */
.user-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  width: 180px;
  margin-top: 8px;
  overflow: hidden;
  z-index: 10;
}

.dropdown-menu {
  list-style: none;
  padding: 0;
}

.dropdown-item {
  padding: 0;
  border-bottom: 1px solid #eee;
}

.dropdown-item:last-child {
  border-bottom: none;
}

.dropdown-link, .dropdown-button {
  display: block;
  padding: 12px 16px;
  text-decoration: none;
  color: #333;
  transition: background-color 0.3s;
  font-size: 14px;
  text-align: left;
  width: 100%;
  background: none;
  border: none;
  cursor: pointer;
}

.dropdown-link:hover, .dropdown-button:hover {
  background-color: #f5f5f5;
}

.dropdown-divider {
  height: 1px;
  background-color: #eee;
  margin: 6px 0;
}

/* 认证链接 */
.auth-links {
  display: flex;
  gap: 15px;
}

.auth-link {
  padding: 8px 16px;
  background-color: #4caf50;
  color: white;
  border-radius: 4px;
  text-decoration: none;
  font-weight: 500;
  transition: background-color 0.3s;
}

.auth-link:hover {
  background-color: #43a047;
}

/* 主体内容样式 */
.app-main {
  flex: 1; /* 让主体内容区域自适应填充剩余空间 */
  padding: 0; /* 去除内边距 */
  width: 100%; /* 确保主体内容占满容器宽度 */
  max-width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center; /* 中央对齐内容 */
}

/* 防止内容超出容器宽度 */
.app-main > * {
  max-width: 100%;
  width: 100%;
}

/* 底部样式 */
.app-footer {
  background-color: #333;
  color: #fff;
  padding: 40px 0 20px;
  width: 100%; /* 确保底部占满宽度 */
}

.footer-content {
  max-width: 1440px; /* 设置最大宽度 */
  margin: 0 auto; /* 居中 */
  display: flex;
  flex-wrap: wrap;
  gap: 30px;
  margin-bottom: 30px;
  padding: 0 20px; /* 添加水平内边距 */
}

.footer-section {
  flex: 1;
  min-width: 250px;
}

.footer-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 15px;
  color: #fff;
}

.footer-desc {
  color: #bbb;
  font-size: 14px;
  line-height: 1.5;
}

.footer-links {
  list-style: none;
  padding: 0;
}

.footer-links li {
  margin-bottom: 10px;
}

.footer-link {
  color: #bbb;
  text-decoration: none;
  transition: color 0.3s;
  font-size: 14px;
}

.footer-link:hover {
  color: #fff;
}

.footer-bottom {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid #444;
  color: #999;
  font-size: 14px;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .app-header .container {
    flex-wrap: wrap;
  }
  
  .main-nav {
    order: 3;
    width: 100%;
    justify-content: center;
    margin-top: 15px;
    overflow-x: auto;
    padding-bottom: 5px;
  }
  
  .footer-content {
    flex-direction: column;
    gap: 20px;
  }
}
</style>
