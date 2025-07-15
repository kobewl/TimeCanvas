# TimeCanvas 前端项目

## 项目简介
本项目是“智能时光绘”系统的前端部分，采用 Vue 3 + TypeScript + Element Plus 技术栈，配合后端实现日记、AI日记、模板广场、待办、报表、个人中心等功能。

---

## 技术栈
- [Vue 3](https://cn.vuejs.org/)（Composition API）
- [TypeScript](https://www.typescriptlang.org/)
- [Element Plus](https://element-plus.org/)（UI 组件库）
- [Pinia](https://pinia.vuejs.org/)（状态管理）
- [Vue Router](https://router.vuejs.org/)（路由管理）
- [Vite](https://vitejs.dev/)（构建工具）

---

## 目录结构
```
TimeCanvas_frontend/
  ├─ public/                # 静态资源
  ├─ src/
  │  ├─ assets/            # 图片、样式等静态资源
  │  ├─ components/        # 公共组件
  │  ├─ views/             # 页面视图
  │  ├─ router/            # 路由配置
  │  ├─ store/             # 状态管理
  │  ├─ api/               # 接口请求
  │  ├─ utils/             # 工具函数
  │  ├─ App.vue            # 根组件
  │  └─ main.ts            # 入口文件
  ├─ package.json
  ├─ tsconfig.json
  ├─ vite.config.ts
  └─ README.md
```

---

## 启动方式
1. 安装依赖：`npm install`
2. 启动开发服务器：`npm run dev`
3. 访问本地地址（如 http://localhost:5173）即可预览

---

## 后续开发规划
1. 配置 Element Plus，完成全局样式和主题色设置
2. 搭建基础路由和页面（登录、注册、首页、日记、AI日记、模板广场、待办、报表、个人中心）
3. 编写 API 请求模块，联调后端接口
4. 完善各页面功能与交互
5. 优化响应式布局和移动端适配
6. 持续完善文档，方便用户理解和使用

---

## 说明
- 本项目所有页面和功能均会在本说明文档中持续更新。
- 如有任何疑问或建议，请随时提出！
