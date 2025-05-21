<template>
  <div class="template-square-container">
    <PageHeader
      title="模板广场"
      description="在这里发现并使用精美的日记模板，让你的记录更加轻松高效。"
      icon="Document"
    />

    <div v-if="loading" class="loading-state">
      <el-skeleton :rows="6" animated />
    </div>
    
    <div v-else-if="templates.length === 0" class="empty-section">
      <EmptyState
        description="暂无可用模板"
        actionText="刷新"
        actionIcon="Refresh"
        @action="fetchTemplates"
      />
    </div>
    
    <div v-else>
      <el-tabs type="card" class="template-tabs">
        <el-tab-pane label="全部模板">
          <el-row :gutter="20">
            <el-col 
              v-for="template in templates"
              :key="template.id"
              :xs="24" :sm="12" :md="8" :lg="6"
            >
              <ContentCard
                :title="template.name"
                icon="Document"
                tag="日记模版"
                tagType="info"
                hoverEffect
                class="template-card"
              >
                <div class="template-content-preview">
                  <p>{{ template.content.substring(0, 150) }}{{ template.content.length > 150 ? '...' : '' }}</p>
                </div>
                
                <template #footer>
                  <el-button type="primary" @click="useTemplate(template)" class="use-template-btn">
                    <el-icon><Edit /></el-icon> 使用模板
                  </el-button>
                  <el-button type="info" plain @click="previewTemplate(template)">
                    <el-icon><View /></el-icon> 预览
                  </el-button>
                </template>
              </ContentCard>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <!-- 模板预览对话框 -->
    <el-dialog
      v-model="previewDialogVisible"
      :title="selectedTemplate?.name || '模板预览'"
      width="60%"
      destroy-on-close
    >
      <div class="template-preview-content">
        <div class="preview-section">
          <h3>模板内容</h3>
          <div class="content-box">
            <p v-for="(line, index) in templateContentLines" :key="index">
              {{ line }}
            </p>
          </div>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="previewDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="useSelectedTemplate">
            使用此模板
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import * as API from '@/api/index.ts';
import type { DiaryTemplate } from '@/api/index.ts';
import { ElMessage } from 'element-plus';
import { Edit, View, Document, Refresh } from '@element-plus/icons-vue';
import PageHeader from '@/components/PageHeader.vue';
import ContentCard from '@/components/ContentCard.vue';
import EmptyState from '@/components/EmptyState.vue';

const router = useRouter();
const templates = ref<DiaryTemplate[]>([]);
const loading = ref(true);

// 模板预览相关
const previewDialogVisible = ref(false);
const selectedTemplate = ref<DiaryTemplate | null>(null);

// 计算属性：模板内容分行
const templateContentLines = computed(() => {
  if (!selectedTemplate.value) return [];
  return selectedTemplate.value.content.split('\n').filter(line => line.trim() !== '');
});

// 获取模板列表
const fetchTemplates = async () => {
  try {
    loading.value = true;
    console.log('正在获取模板列表...');
    const result = await API.getTemplates();
    console.log('获取到模板列表:', result);
    templates.value = result;
  } catch (error) {
    console.error('获取模板失败:', error);
    ElMessage.error('获取模板失败，请稍后再试');
  } finally {
    loading.value = false;
  }
};

// 使用模板创建日记
const useTemplate = (template: DiaryTemplate) => {
  // 将模板内容保存到sessionStorage，然后在日记页面创建新日记时读取
  sessionStorage.setItem('draftDiary', JSON.stringify({
    title: `使用模板：${template.name}`,
    content: template.content
  }));
  
  ElMessage.success('已选择模板，即将跳转到日记页面');
  
  // 跳转到日记页面，并传递参数指示创建新日记
  router.push({ path: '/diary', query: { createFromDraft: 'true' } });
};

// 预览模板
const previewTemplate = (template: DiaryTemplate) => {
  selectedTemplate.value = template;
  previewDialogVisible.value = true;
};

// 使用当前选中的模板
const useSelectedTemplate = () => {
  if (selectedTemplate.value) {
    useTemplate(selectedTemplate.value);
    previewDialogVisible.value = false;
  }
};

// 初始化时获取模板
onMounted(() => {
  fetchTemplates();
});
</script>

<style scoped>
.template-square-container {
  max-width: 1280px;
  width: 100%;
  margin: 0 auto;
  padding: 20px;
}

.template-tabs {
  margin-top: 20px;
}

.template-card {
  height: 100%;
  margin-bottom: 20px;
}

.template-content-preview {
  color: var(--el-text-color-secondary);
  line-height: 1.6;
  margin-bottom: 15px;
  min-height: 80px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.use-template-btn {
  margin-right: 10px;
}

.loading-state {
  text-align: center;
  padding: 40px 0;
}

.empty-section {
  margin: 40px 0;
}

/* 模板预览对话框样式 */
.template-preview-content {
  max-height: 60vh;
  overflow-y: auto;
}

.preview-section {
  margin-bottom: 20px;
}

.preview-section h3 {
  font-size: 16px;
  color: var(--el-text-color-primary);
  margin-bottom: 10px;
  font-weight: 600;
}

.content-box {
  background-color: var(--el-fill-color-light);
  border-radius: 4px;
  padding: 16px;
  min-height: 200px;
  white-space: pre-line;
  color: var(--el-text-color-regular);
  line-height: 1.8;
}
</style> 