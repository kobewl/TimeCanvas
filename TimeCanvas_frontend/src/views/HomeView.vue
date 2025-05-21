<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import PageHeader from '@/components/PageHeader.vue';
import ContentCard from '@/components/ContentCard.vue';
import DiaryView from './DiaryView.vue';
import ReportsView from './ReportsView.vue';
import TodosView from './TodosView.vue';
import { Document, EditPen, Histogram, List, MagicStick } from '@element-plus/icons-vue';

const activeTab = ref('diary'); // 默认选中的标签页
const router = useRouter();

// 跳转到AI日记页面
const goToAiDiary = () => {
  router.push('/ai-diary');
};

// 跳转到模板广场
const goToTemplates = () => {
  router.push('/templates');
};

// 功能卡片数据
const featuredCards = [
  {
    title: 'AI智能写日记',
    description: '只需简单描述你的一天，AI将帮你生成详细生动的日记内容。',
    icon: 'MagicStick',
    tag: '热门',
    tagType: 'danger',
    buttonText: '立即体验',
    buttonType: 'primary',
    action: goToAiDiary
  },
  {
    title: '我的日记',
    description: '查看、编辑和管理你的所有日记，使用模板快速创建新日记。',
    icon: 'EditPen',
    tag: '简单',
    tagType: 'success',
    buttonText: '进入',
    buttonType: 'success',
    route: '/diary'
  },
  {
    title: '一键报告',
    description: '根据你的日记自动生成周报、月报，洞察你的情绪变化和活动规律。',
    icon: 'Histogram',
    tag: '统计',
    tagType: 'warning',
    buttonText: '查看',
    buttonType: 'warning',
    route: '/reports'
  },
  {
    title: '待办管理',
    description: '从日记中自动提取待办事项，帮助你高效管理任务和计划。',
    icon: 'List',
    tag: '计划',
    tagType: 'info',
    buttonText: '管理',
    buttonType: 'info',
    route: '/todos'
  },
  {
    title: '模板广场',
    description: '探索和使用精美的日记模板，让你的记录更加轻松高效。',
    icon: 'Document',
    tag: '推荐',
    tagType: 'primary',
    buttonText: '浏览',
    buttonType: 'primary',
    action: goToTemplates
  }
];
</script>

<template>
  <div class="dashboard-container">
    <PageHeader
      title="智绘时光 - 让记录更智能"
      description="基于AI的智能日记管理系统，让记录生活变得简单高效"
      icon="House"
      :showDivider="false"
    />

    <!-- 功能卡片区域 -->
    <div class="feature-cards-section">
      <h2 class="section-title">功能导航</h2>
      <el-row :gutter="20">
        <el-col v-for="(card, index) in featuredCards" :key="index"
                :xs="24" :sm="12" :md="8">
          <ContentCard
            :title="card.title"
            :icon="card.icon"
            :tag="card.tag"
            :tagType="card.tagType"
            hoverEffect
            class="feature-card"
          >
            <p class="card-description">{{ card.description }}</p>
            <template #footer>
              <el-button 
                v-if="card.route" 
                :type="card.buttonType" 
                @click="router.push(card.route)"
                class="full-width-button"
              >
                {{ card.buttonText }}
              </el-button>
              <el-button 
                v-else 
                :type="card.buttonType" 
                @click="card.action"
                class="full-width-button"
              >
                {{ card.buttonText }}
              </el-button>
            </template>
          </ContentCard>
        </el-col>
      </el-row>
    </div>

    <!-- 内容标签页 -->
    <div class="content-section">
      <h2 class="section-title">快速访问</h2>
      <el-tabs v-model="activeTab" class="dashboard-tabs" type="border-card">
        <el-tab-pane label="智能日记" name="diary">
          <template #label>
            <el-icon><EditPen /></el-icon>
            <span>智能日记</span>
          </template>
          <div class="tab-content">
            <DiaryView />
          </div>
        </el-tab-pane>
        <el-tab-pane label="一键报告" name="reports">
          <template #label>
            <el-icon><Histogram /></el-icon>
            <span>一键报告</span>
          </template>
          <div class="tab-content">
            <ReportsView />
          </div>
        </el-tab-pane>
        <el-tab-pane label="待办管理" name="todos">
          <template #label>
            <el-icon><List /></el-icon>
            <span>待办管理</span>
          </template>
          <div class="tab-content">
            <TodosView />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<style scoped>
.dashboard-container {
  width: 100%;
  padding: 20px;
  max-width: 1440px;
  margin: 0 auto;
}

.section-title {
  font-size: 1.5rem;
  color: var(--el-text-color-primary);
  margin-bottom: 20px;
  font-weight: 600;
  position: relative;
  padding-left: 15px;
  border-left: 4px solid var(--el-color-primary);
}

.feature-cards-section {
  margin-bottom: 40px;
}

.feature-card {
  height: 100%;
}

.card-description {
  color: var(--el-text-color-secondary);
  line-height: 1.6;
  margin-bottom: 20px;
  min-height: 80px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.full-width-button {
  width: 100%;
}

.content-section {
  margin-bottom: 40px;
}

.dashboard-tabs {
  border-radius: 8px;
  box-shadow: var(--el-box-shadow-light);
}

.tab-content {
  padding: 20px 0;
  min-height: 400px;
  width: 100%;
}

:deep(.el-tabs__nav) {
  background-color: var(--el-fill-color-light);
}

:deep(.el-tabs__item) {
  display: flex;
  align-items: center;
  gap: 5px;
  height: 50px;
  font-size: 16px;
}

:deep(.el-tabs__item.is-active) {
  color: var(--el-color-primary);
  font-weight: 600;
}

:deep(.el-tabs__content) {
  padding: 0;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .dashboard-container {
    padding: 15px;
  }
  
  .section-title {
    font-size: 1.3rem;
  }
  
  .card-description {
    min-height: 60px;
  }
  
  :deep(.el-tabs__item) {
    height: 45px;
    font-size: 14px;
  }
}
</style>
