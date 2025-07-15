<script setup lang="ts">
// App根组件，集成Element Plus布局和路由视图
</script>

<template>
  <div class="app-root">
    <el-menu
      :default-active="$route.path"
      mode="horizontal"
      router
      background-color="#409eff"
      text-color="#fff"
      active-text-color="#ffd04b"
      class="nav-menu"
    >
      <el-menu-item index="/">首页</el-menu-item>
      <el-menu-item index="/diary">日记</el-menu-item>
      <el-menu-item index="/ai-diary">AI日记</el-menu-item>
      <el-menu-item index="/template-square">模板广场</el-menu-item>
      <el-menu-item index="/todos">待办</el-menu-item>
      <el-menu-item index="/reports">报表</el-menu-item>
      <el-menu-item index="/profile">个人中心</el-menu-item>
      <el-menu-item index="/login">登录</el-menu-item>
      <el-menu-item index="/register">注册</el-menu-item>
    </el-menu>
    <div class="main-content">
      <router-view />
    </div>
    <footer class="footer-bar">
      © 2025 智能时光绘 | Powered by Vue3 + Element Plus | 作者：wangliang
    </footer>
  </div>
</template>

<style scoped>
.app-root {
  min-height: 100vh;
  background: #f4f6fa;
  display: flex;
  flex-direction: column;
}
.nav-menu {
  width: 100vw;
  min-width: 320px;
  border-bottom: none;
  justify-content: center;
  display: flex;
  box-shadow: 0 2px 8px rgba(0,0,0,0.03);
}
.main-content {
  width: 100vw;
  margin: 0;
  padding: 0;
  background: #fff;
  border-radius: 0;
  box-shadow: none;
  min-height: 600px;
  box-sizing: border-box;
  flex: 1;
}
.footer-bar {
  width: 100vw;
  background: #f4f6fa;
  color: #888;
  text-align: center;
  font-size: 15px;
  padding: 18px 0 12px 0;
  letter-spacing: 1px;
  border-top: 1px solid #e6e6e6;
}
:global(body), :global(#app) {
  margin: 0;
  padding: 0;
}
</style>
