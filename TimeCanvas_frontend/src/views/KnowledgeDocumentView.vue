<template>
  <div class="knowledge-document">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="header-content">
        <div class="logo">
          <h1>AI学习文档</h1>
        </div>
        <nav class="nav">
          <router-link to="/" class="nav-item">首页</router-link>
          <router-link to="/knowledge" class="nav-item active">学习文档</router-link>
          <router-link to="/knowledge-management" class="nav-item">知识管理</router-link>
          <router-link to="/ai-diary" class="nav-item">AI问答</router-link>
        </nav>
        <div class="search-box">
          <el-input 
            v-model="searchQuery" 
            placeholder="搜索文档..." 
            class="search-input"
            @keyup.enter="handleSearch"
          >
            <template #suffix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </div>
      </div>
    </header>

    <div class="main-container">
      <!-- 左侧导航栏 -->
      <aside class="sidebar">
        <div class="sidebar-content">
          <!-- 文档分类 -->
          <div class="category-section">
            <h3 class="category-title">Python基础教程</h3>
            <ul class="category-list">
              <li v-for="item in pythonBasics" :key="item.id" 
                  :class="{ active: selectedDoc?.id === item.id }"
                  @click="selectDocument(item)">
                <span class="chapter-number">第{{ item.chapter }}节：</span>
                <span class="chapter-title">{{ item.title }}</span>
              </li>
            </ul>
          </div>

          <div class="category-section">
            <h3 class="category-title">AI基础知识</h3>
            <ul class="category-list">
              <li v-for="item in aiBasics" :key="item.id"
                  :class="{ active: selectedDoc?.id === item.id }"
                  @click="selectDocument(item)">
                <span class="chapter-number">第{{ item.chapter }}节：</span>
                <span class="chapter-title">{{ item.title }}</span>
              </li>
            </ul>
          </div>

          <div class="category-section">
            <h3 class="category-title">智能体开发基础</h3>
            <ul class="category-list">
              <li v-for="item in agentDev" :key="item.id"
                  :class="{ active: selectedDoc?.id === item.id }"
                  @click="selectDocument(item)">
                <span class="chapter-number">第{{ item.chapter }}节：</span>
                <span class="chapter-title">{{ item.title }}</span>
              </li>
            </ul>
          </div>
        </div>
      </aside>

      <!-- 主要内容区域 -->
      <main class="content">
        <div class="content-wrapper">
          <!-- 面包屑导航 -->
          <nav class="breadcrumb">
            <span>AI学习文档</span>
            <span class="separator">></span>
            <span v-if="selectedDoc">{{ selectedDoc.category }}</span>
            <span class="separator" v-if="selectedDoc">></span>
            <span v-if="selectedDoc" class="current">{{ selectedDoc.title }}</span>
          </nav>

          <!-- 文档标题 -->
          <div class="document-header">
            <h1 class="document-title">{{ selectedDoc?.title || '晚点的AI学习文档' }}</h1>
            <div class="document-meta">
              <span class="update-time">更新时间：{{ selectedDoc?.updateTime || '2024-03-15' }}</span>
              <div class="actions">
                <el-button size="small" type="primary" @click="editDocument">编辑</el-button>
                <el-button size="small" @click="shareDocument">分享</el-button>
                <el-button size="small" @click="exportDocument">导出</el-button>
              </div>
            </div>
          </div>

          <!-- 文档内容 -->
          <div class="document-content">
            <div v-if="selectedDoc" class="content-text" v-html="selectedDoc.content"></div>
            <div v-else class="default-content">
              <h2>晚点的AI学习文档</h2>
              <p>这是一篇高质量的技术AI的AI学习文档，内容讲述学习方法等，请选择AI的本书。用工具的例子，对Pytorch框架，基于LangChain4，LangGraph的智能体开发。</p>
              
              <h3>文档分为三部分：</h3>
              
              <h4>第一部分：AI基础知识</h4>
              <p>从大概念，智能体的基本概念分析，到理解示例工具，日常工具目的用，再到学习方法实行开发步骤，这部分让你明白AI基础的认识，加强深刻了解相关权威，及怎样用自研开发。</p>
              
              <h4>第二部分：Python基础教程</h4>
              <p>从零开始学习Python编程，包含变量、数据类型、控制语句、函数、面向对象等方面内容。每个知识点都有详细的程序，让你轻松上手编程开发环境。</p>
              
              <h4>第三部分：智能体开发基础</h4>
              <p>基于LangChain和LangGraph框架，教你开发自己的AI智能体。从简单的对话机器人到复杂的多步决策智能体，一步步深入学习人工智能的世界。</p>
              
              <p>这套文档结合大兼容性实战，不是空中楼阁的理论，而是能让你真正建立完整你项目的实践化教学。学会这套文档，你基建能开发自己的人工智能应用，快速建立一个具有实战能力。</p>
              
              <p>您可以从任何章节开始学习，直接跳过对应开发者。内容中描述的文档：</p>
              
              <div class="qr-code">
                <p>添加微信，万无一失文档</p>
                <div class="qr-placeholder">
                  <el-icon size="100"><QrCode /></el-icon>
                  <p>扫码添加微信</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 导航按钮 -->
          <div class="navigation" v-if="selectedDoc">
            <el-button @click="previousDoc" :disabled="!hasPrevious">
              <el-icon><ArrowLeft /></el-icon>
              上一页
            </el-button>
            <el-button @click="nextDoc" :disabled="!hasNext" type="primary">
              下一页
              <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { Search, QrCode, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'

// 响应式数据
const searchQuery = ref('')
const selectedDoc = ref<any>(null)

// 文档数据
const pythonBasics = ref([
  { id: 1, chapter: '1', title: '安装Python', category: 'Python基础教程', content: '<h2>Python环境搭建</h2><p>本章介绍如何安装Python开发环境...</p>', updateTime: '2024-03-15' },
  { id: 2, chapter: '2', title: '变量开发工具', category: 'Python基础教程', content: '<h2>Python变量</h2><p>变量是程序的基础...</p>', updateTime: '2024-03-14' },
  { id: 3, chapter: '3', title: '数据类型', category: 'Python基础教程', content: '<h2>Python数据类型</h2><p>Python的基本数据类型包括...</p>', updateTime: '2024-03-13' },
  { id: 4, chapter: '4', title: '基本语法', category: 'Python基础教程', content: '<h2>Python基本语法</h2><p>Python的语法简洁明了...</p>', updateTime: '2024-03-12' },
  { id: 5, chapter: '5', title: '列表', category: 'Python基础教程', content: '<h2>Python列表</h2><p>列表是Python中最常用的数据结构...</p>', updateTime: '2024-03-11' },
  { id: 6, chapter: '6', title: '元组', category: 'Python基础教程', content: '<h2>Python元组</h2><p>元组是不可变的序列...</p>', updateTime: '2024-03-10' },
  { id: 7, chapter: '7', title: '字典', category: 'Python基础教程', content: '<h2>Python字典</h2><p>字典是键值对的集合...</p>', updateTime: '2024-03-09' },
  { id: 8, chapter: '8', title: '集合', category: 'Python基础教程', content: '<h2>Python集合</h2><p>集合是无序不重复元素的集...</p>', updateTime: '2024-03-08' },
  { id: 9, chapter: '9', title: '列表', category: 'Python基础教程', content: '<h2>Python函数</h2><p>函数是代码组织的基本单位...</p>', updateTime: '2024-03-07' },
  { id: 10, chapter: '10', title: '类', category: 'Python基础教程', content: '<h2>Python类</h2><p>面向对象编程的核心概念...</p>', updateTime: '2024-03-06' },
  { id: 11, chapter: '11', title: '模块化', category: 'Python基础教程', content: '<h2>Python模块</h2><p>模块化编程提高代码复用性...</p>', updateTime: '2024-03-05' },
  { id: 12, chapter: '12', title: 'pip工具', category: 'Python基础教程', content: '<h2>包管理工具pip</h2><p>pip是Python的包管理工具...</p>', updateTime: '2024-03-04' },
  { id: 13, chapter: '13', title: '应用打包', category: 'Python基础教程', content: '<h2>应用程序打包</h2><p>将Python程序打包成可执行文件...</p>', updateTime: '2024-03-03' },
  { id: 14, chapter: '14', title: '虚拟环境', category: 'Python基础教程', content: '<h2>虚拟环境管理</h2><p>使用虚拟环境隔离项目依赖...</p>', updateTime: '2024-03-02' },
  { id: 15, chapter: '15', title: 'Pytorch模块', category: 'Python基础教程', content: '<h2>PyTorch深度学习框架</h2><p>PyTorch是流行的深度学习框架...</p>', updateTime: '2024-03-01' }
])

const aiBasics = ref([
  { id: 16, chapter: '1', title: '什么是人工智能', category: 'AI基础知识', content: '<h2>人工智能概述</h2><p>人工智能是计算机科学的一个分支...</p>', updateTime: '2024-03-15' },
  { id: 17, chapter: '2', title: '什么是机器学习', category: 'AI基础知识', content: '<h2>机器学习基础</h2><p>机器学习是实现人工智能的一种方法...</p>', updateTime: '2024-03-14' },
  { id: 18, chapter: '3', title: '通用人工智能', category: 'AI基础知识', content: '<h2>通用人工智能(AGI)</h2><p>AGI是能够理解、学习和执行任何智能任务的系统...</p>', updateTime: '2024-03-13' },
  { id: 19, chapter: '4', title: 'AI与技术发展', category: 'AI基础知识', content: '<h2>AI技术发展历程</h2><p>从符号AI到深度学习的发展历程...</p>', updateTime: '2024-03-12' }
])

const agentDev = ref([
  { id: 20, chapter: '1', title: '智能体开发工具', category: '智能体开发基础', content: '<h2>智能体开发工具链</h2><p>介绍开发智能体所需的工具和框架...</p>', updateTime: '2024-03-15' },
  { id: 21, chapter: '2', title: '包增设项目', category: '智能体开发基础', content: '<h2>项目结构设计</h2><p>如何设计一个可扩展的智能体项目...</p>', updateTime: '2024-03-14' },
  { id: 22, chapter: '3', title: '接入大模型', category: '智能体开发基础', content: '<h2>集成大语言模型</h2><p>接入OpenAI、Claude等大模型API...</p>', updateTime: '2024-03-13' },
  { id: 23, chapter: '4', title: '链 (chain)', category: '智能体开发基础', content: '<h2>LangChain基础</h2><p>理解链的概念和使用方法...</p>', updateTime: '2024-03-12' },
  { id: 24, chapter: '5', title: '超示例记忆', category: '智能体开发基础', content: '<h2>智能体记忆管理</h2><p>实现长期记忆和短期记忆...</p>', updateTime: '2024-03-11' },
  { id: 25, chapter: '6', title: '记忆 (memory)', category: '智能体开发基础', content: '<h2>内存管理机制</h2><p>不同类型的记忆系统实现...</p>', updateTime: '2024-03-10' },
  { id: 26, chapter: '7', title: '智能体 (agent)', category: '智能体开发基础', content: '<h2>智能体架构设计</h2><p>构建自主决策的智能体...</p>', updateTime: '2024-03-09' },
  { id: 27, chapter: '8', title: '回调处理', category: '智能体开发基础', content: '<h2>回调机制</h2><p>处理异步操作和事件驱动...</p>', updateTime: '2024-03-08' },
  { id: 28, chapter: '9', title: '内置工具', category: '智能体开发基础', content: '<h2>内置工具使用</h2><p>利用预构建的工具扩展能力...</p>', updateTime: '2024-03-07' },
  { id: 29, chapter: '10', title: '自定义工具', category: '智能体开发基础', content: '<h2>自定义工具开发</h2><p>创建专用的工具函数...</p>', updateTime: '2024-03-06' },
  { id: 30, chapter: '11', title: '状态 (state)', category: '智能体开发基础', content: '<h2>状态管理</h2><p>维护和更新智能体状态...</p>', updateTime: '2024-03-05' },
  { id: 31, chapter: '12', title: 'LangGraph基础', category: '智能体开发基础', content: '<h2>LangGraph框架</h2><p>基于图的工作流编排...</p>', updateTime: '2024-03-04' },
  { id: 32, chapter: '13', title: '状态 (state)', category: '智能体开发基础', content: '<h2>复杂状态处理</h2><p>多层次状态管理策略...</p>', updateTime: '2024-03-03' },
  { id: 33, chapter: '14', title: '节点创建', category: '智能体开发基础', content: '<h2>节点设计模式</h2><p>创建可复用的处理节点...</p>', updateTime: '2024-03-02' }
])

// 计算属性
const allDocs = computed(() => [...pythonBasics.value, ...aiBasics.value, ...agentDev.value])
const currentIndex = computed(() => allDocs.value.findIndex(doc => doc.id === selectedDoc.value?.id))
const hasPrevious = computed(() => currentIndex.value > 0)
const hasNext = computed(() => currentIndex.value < allDocs.value.length - 1)

// 方法
const selectDocument = (doc: any) => {
  selectedDoc.value = doc
}

const handleSearch = () => {
  console.log('搜索:', searchQuery.value)
}

const editDocument = () => {
  console.log('编辑文档')
}

const shareDocument = () => {
  console.log('分享文档')
}

const exportDocument = () => {
  console.log('导出文档')
}

const previousDoc = () => {
  if (hasPrevious.value) {
    selectedDoc.value = allDocs.value[currentIndex.value - 1]
  }
}

const nextDoc = () => {
  if (hasNext.value) {
    selectedDoc.value = allDocs.value[currentIndex.value + 1]
  }
}

// 生命周期
onMounted(() => {
  // 默认选择第一个文档
  if (pythonBasics.value.length > 0) {
    selectedDoc.value = pythonBasics.value[0]
  }
})
</script>

<style scoped>
.knowledge-document {
  min-height: 100vh;
  background: #1a1a1a;
  color: #e4e4e4;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', sans-serif;
}

/* 顶部导航栏 */
.header {
  background: #2d2d2d;
  border-bottom: 1px solid #404040;
  padding: 0 20px;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 60px;
}

.logo h1 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #ffffff;
}

.nav {
  display: flex;
  gap: 30px;
}

.nav-item {
  color: #b3b3b3;
  text-decoration: none;
  padding: 8px 12px;
  border-radius: 6px;
  transition: all 0.2s;
  font-weight: 500;
}

.nav-item:hover,
.nav-item.active {
  color: #ffffff;
  background: #404040;
}

.search-box {
  width: 300px;
}

.search-input {
  background: #404040;
}

.search-input :deep(.el-input__wrapper) {
  background: #404040;
  box-shadow: none;
  border: 1px solid #555;
}

.search-input :deep(.el-input__inner) {
  color: #e4e4e4;
}

.search-input :deep(.el-input__inner)::placeholder {
  color: #888;
}

/* 主容器 */
.main-container {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  min-height: calc(100vh - 60px);
}

/* 侧边栏 */
.sidebar {
  width: 280px;
  background: #262626;
  border-right: 1px solid #404040;
  overflow-y: auto;
  position: sticky;
  top: 60px;
  height: calc(100vh - 60px);
}

.sidebar-content {
  padding: 20px 0;
}

.category-section {
  margin-bottom: 30px;
}

.category-title {
  font-size: 14px;
  font-weight: 600;
  color: #ffffff;
  margin: 0 0 15px 20px;
  padding-bottom: 8px;
  border-bottom: 1px solid #404040;
}

.category-list {
  list-style: none;
  margin: 0;
  padding: 0;
}

.category-list li {
  padding: 8px 20px;
  cursor: pointer;
  transition: all 0.2s;
  border-left: 3px solid transparent;
}

.category-list li:hover {
  background: #333333;
  border-left-color: #666;
}

.category-list li.active {
  background: #444444;
  border-left-color: #6366f1;
  color: #ffffff;
}

.chapter-number {
  font-size: 12px;
  color: #888;
  margin-right: 8px;
}

.chapter-title {
  font-size: 13px;
  color: #b3b3b3;
}

.category-list li.active .chapter-title {
  color: #ffffff;
}

/* 主内容区 */
.content {
  flex: 1;
  background: #1a1a1a;
  overflow-y: auto;
}

.content-wrapper {
  padding: 30px 40px;
  max-width: 900px;
}

/* 面包屑 */
.breadcrumb {
  font-size: 14px;
  color: #888;
  margin-bottom: 30px;
}

.separator {
  margin: 0 8px;
  color: #666;
}

.current {
  color: #b3b3b3;
}

/* 文档头部 */
.document-header {
  margin-bottom: 40px;
}

.document-title {
  font-size: 32px;
  font-weight: 700;
  color: #ffffff;
  margin: 0 0 15px 0;
  line-height: 1.3;
}

.document-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #404040;
}

.update-time {
  font-size: 14px;
  color: #888;
}

.actions {
  display: flex;
  gap: 10px;
}

/* 文档内容 */
.document-content {
  line-height: 1.8;
  font-size: 16px;
}

.content-text,
.default-content {
  color: #e4e4e4;
}

.content-text h2,
.default-content h2 {
  font-size: 24px;
  color: #ffffff;
  margin: 30px 0 20px 0;
  font-weight: 600;
}

.content-text h3,
.default-content h3 {
  font-size: 20px;
  color: #ffffff;
  margin: 25px 0 15px 0;
  font-weight: 600;
}

.content-text h4,
.default-content h4 {
  font-size: 18px;
  color: #ffffff;
  margin: 20px 0 12px 0;
  font-weight: 600;
}

.content-text p,
.default-content p {
  margin: 15px 0;
  color: #b3b3b3;
}

/* 二维码区域 */
.qr-code {
  text-align: center;
  margin: 40px 0;
  padding: 30px;
  background: #262626;
  border-radius: 12px;
  border: 1px solid #404040;
}

.qr-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  color: #888;
}

/* 导航按钮 */
.navigation {
  display: flex;
  justify-content: space-between;
  margin-top: 50px;
  padding-top: 30px;
  border-top: 1px solid #404040;
}

/* Element Plus 组件样式覆盖 */
:deep(.el-button) {
  background: #404040;
  border-color: #555;
  color: #e4e4e4;
}

:deep(.el-button:hover) {
  background: #4a4a4a;
  border-color: #666;
}

:deep(.el-button--primary) {
  background: #6366f1;
  border-color: #6366f1;
}

:deep(.el-button--primary:hover) {
  background: #7c3aed;
  border-color: #7c3aed;
}

/* 滚动条样式 */
.sidebar::-webkit-scrollbar,
.content::-webkit-scrollbar {
  width: 6px;
}

.sidebar::-webkit-scrollbar-track,
.content::-webkit-scrollbar-track {
  background: #2d2d2d;
}

.sidebar::-webkit-scrollbar-thumb,
.content::-webkit-scrollbar-thumb {
  background: #555;
  border-radius: 3px;
}

.sidebar::-webkit-scrollbar-thumb:hover,
.content::-webkit-scrollbar-thumb:hover {
  background: #666;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .sidebar {
    width: 240px;
  }
  
  .content-wrapper {
    padding: 20px 30px;
  }
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    height: auto;
    padding: 15px 0;
    gap: 15px;
  }
  
  .nav {
    gap: 15px;
    flex-wrap: wrap;
  }
  
  .search-box {
    width: 100%;
    max-width: 300px;
  }
  
  .main-container {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
    height: auto;
    position: relative;
    max-height: 300px;
  }
  
  .content-wrapper {
    padding: 20px;
  }
  
  .document-title {
    font-size: 24px;
  }
}
</style>