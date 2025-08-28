<template>
  <div class="home-page">
    <!-- 英雄区域 -->
    <section class="hero-section">
      <div class="hero-container">
        <div class="hero-content">
          <h1 class="hero-title">
            <span class="gradient-text">TimeCanvas</span>
            <br>
            智能时光绘
          </h1>
          <p class="hero-subtitle">
            基于AI的智能日记与生活管理系统<br>
            让您的每一天都充满意义
          </p>
          <div class="hero-buttons">
            <el-button type="primary" size="large" @click="navigate('/ai-diary')">
              <el-icon><Edit /></el-icon>
              开始写日记
            </el-button>
            <el-button size="large" @click="navigate('/knowledge')">
              <el-icon><Document /></el-icon>
              浏览知识库
            </el-button>
          </div>
        </div>
        <div class="hero-visual">
          <div class="floating-cards">
            <div class="card card-1">
              <div class="card-icon">📝</div>
              <div class="card-text">AI日记生成</div>
            </div>
            <div class="card card-2">
              <div class="card-icon">🧠</div>
              <div class="card-text">智能知识管理</div>
            </div>
            <div class="card card-3">
              <div class="card-icon">📊</div>
              <div class="card-text">数据分析</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 功能特性区域 -->
    <section class="features-section">
      <div class="container">
        <h2 class="section-title">核心功能</h2>
        <div class="features-grid">
          <div class="feature-card" v-for="feature in features" :key="feature.id">
            <div class="feature-icon">{{ feature.icon }}</div>
            <h3 class="feature-title">{{ feature.title }}</h3>
            <p class="feature-description">{{ feature.description }}</p>
            <el-button text @click="navigate(feature.path)">了解更多 →</el-button>
          </div>
        </div>
      </div>
    </section>

    <!-- 统计数据区域 -->
    <section class="stats-section">
      <div class="container">
        <div class="stats-grid">
          <div class="stat-item" v-for="stat in stats" :key="stat.label">
            <div class="stat-number">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- CTA区域 -->
    <section class="cta-section">
      <div class="container">
        <div class="cta-content">
          <h2 class="cta-title">开始您的智能生活管理之旅</h2>
          <p class="cta-subtitle">加入我们，体验AI驱动的个人成长</p>
          <div class="cta-buttons">
            <el-button type="primary" size="large" @click="navigate('/register')">
              立即注册
            </el-button>
            <el-button size="large" @click="navigate('/login')">
              已有账号？登录
            </el-button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Edit, Document } from '@element-plus/icons-vue'

const router = useRouter()

const navigate = (path: string) => {
  router.push(path)
}

const features = ref([
  {
    id: 1,
    icon: '🤖',
    title: 'AI日记生成',
    description: '一句话生成完整日记，AI自动润色，让记录变得轻松有趣',
    path: '/ai-diary'
  },
  {
    id: 2,
    icon: '📚',
    title: '知识文档',
    description: '智能知识管理，语义搜索，构建个人知识图谱',
    path: '/knowledge'
  },
  {
    id: 3,
    icon: '📋',
    title: '知识管理',
    description: '现代化的知识库管理系统，支持分类、标签、全文搜索',
    path: '/knowledge-management'
  },
  {
    id: 4,
    icon: '🎨',
    title: '模板广场',
    description: '丰富的日记模板，满足不同场景需求',
    path: '/template-square'
  },
  {
    id: 5,
    icon: '📖',
    title: '日记管理',
    description: '传统日记编写，支持富文本，回顾人生轨迹',
    path: '/diary'
  },
  {
    id: 6,
    icon: '⚙️',
    title: '个人中心',
    description: '个性化设置，数据统计，管理个人信息',
    path: '/profile'
  }
])

const stats = ref([
  { value: '10,000+', label: '用户数量' },
  { value: '500,000+', label: '日记条目' },
  { value: '1,000,000+', label: '知识条目' },
  { value: '99.9%', label: '服务可用性' }
])
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #0f0f0f 0%, #1a1a1a 100%);
  color: #e4e4e4;
}

/* 英雄区域 */
.hero-section {
  padding: 80px 0 100px;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
  position: relative;
  overflow: hidden;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(102, 126, 234, 0.1) 0%, transparent 50%);
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.hero-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
  position: relative;
  z-index: 2;
}

.hero-content {
  text-align: left;
}

.hero-title {
  font-size: 4rem;
  font-weight: 800;
  line-height: 1.1;
  margin-bottom: 30px;
}

.gradient-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-subtitle {
  font-size: 1.25rem;
  color: #b3b3b3;
  line-height: 1.6;
  margin-bottom: 40px;
}

.hero-buttons {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
}

.hero-buttons .el-button {
  height: 50px;
  padding: 0 30px;
  font-size: 16px;
  border-radius: 12px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.hero-buttons .el-button--primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.hero-buttons .el-button--primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 35px rgba(102, 126, 234, 0.4);
}

/* 浮动卡片 */
.hero-visual {
  position: relative;
  height: 400px;
}

.floating-cards {
  position: relative;
  height: 100%;
}

.card {
  position: absolute;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 20px;
  padding: 25px;
  text-align: center;
  transition: all 0.3s ease;
  animation: float 6s ease-in-out infinite;
}

.card:hover {
  transform: scale(1.05);
  background: rgba(255, 255, 255, 0.08);
}

.card-1 {
  top: 20px;
  left: 50px;
  animation-delay: 0s;
}

.card-2 {
  top: 120px;
  right: 30px;
  animation-delay: 2s;
}

.card-3 {
  bottom: 40px;
  left: 30px;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-15px); }
}

.card-icon {
  font-size: 2rem;
  margin-bottom: 10px;
}

.card-text {
  font-size: 14px;
  font-weight: 600;
  color: #e4e4e4;
}

/* 功能特性区域 */
.features-section {
  padding: 100px 0;
  background: #0f0f0f;
}

.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

.section-title {
  font-size: 2.5rem;
  font-weight: 700;
  text-align: center;
  margin-bottom: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 30px;
}

.feature-card {
  background: linear-gradient(145deg, #1a1a1a 0%, #2d2d2d 100%);
  border: 1px solid #333;
  border-radius: 20px;
  padding: 40px 30px;
  text-align: center;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(102, 126, 234, 0.1), transparent);
  transition: left 0.5s ease;
}

.feature-card:hover::before {
  left: 100%;
}

.feature-card:hover {
  transform: translateY(-10px);
  border-color: #667eea;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 20px;
  display: block;
}

.feature-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 15px;
  color: #ffffff;
}

.feature-description {
  color: #b3b3b3;
  line-height: 1.6;
  margin-bottom: 25px;
}

/* 统计数据区域 */
.stats-section {
  padding: 80px 0;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
  text-align: center;
}

.stat-item {
  padding: 20px;
}

.stat-number {
  font-size: 3rem;
  font-weight: 800;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 1.1rem;
  color: #b3b3b3;
  font-weight: 500;
}

/* CTA区域 */
.cta-section {
  padding: 100px 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  text-align: center;
}

.cta-content {
  max-width: 600px;
  margin: 0 auto;
}

.cta-title {
  font-size: 2.5rem;
  font-weight: 700;
  color: #ffffff;
  margin-bottom: 20px;
}

.cta-subtitle {
  font-size: 1.2rem;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 40px;
  line-height: 1.6;
}

.cta-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.cta-buttons .el-button {
  height: 50px;
  padding: 0 30px;
  font-size: 16px;
  border-radius: 12px;
  font-weight: 600;
}

.cta-buttons .el-button--primary {
  background: #ffffff;
  color: #667eea;
  border: none;
}

.cta-buttons .el-button--primary:hover {
  background: #f0f0f0;
  transform: translateY(-2px);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .hero-container {
    grid-template-columns: 1fr;
    gap: 40px;
    text-align: center;
  }
  
  .hero-title {
    font-size: 3rem;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 60px 0 80px;
  }
  
  .hero-title {
    font-size: 2.5rem;
  }
  
  .hero-subtitle {
    font-size: 1.1rem;
  }
  
  .features-grid {
    grid-template-columns: 1fr;
  }
  
  .section-title {
    font-size: 2rem;
  }
  
  .cta-title {
    font-size: 2rem;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .hero-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .hero-buttons .el-button {
    width: 100%;
    max-width: 280px;
  }
  
  .cta-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .cta-buttons .el-button {
    width: 100%;
    max-width: 280px;
  }
}
</style> 