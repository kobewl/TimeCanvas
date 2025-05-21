<template>
  <div class="diary-container">
    <el-row justify="center">
      <el-col :span="24">
        <h1 class="page-title">我的日记</h1>
      </el-col>
    </el-row>
    
    <div class="content-wrapper">
      <!-- 左侧边栏：日记列表 -->
      <div class="sidebar">
        <div class="sidebar-header">
          <h2>日记列表</h2>
          <el-button type="primary" size="small" @click="createNewDiary(false)">
            <el-icon><Plus /></el-icon>
            新建
          </el-button>
        </div>
        
        <el-skeleton v-if="loading" :rows="5" animated />
        
        <div v-else-if="diaries.length === 0" class="empty-state">
          <el-empty description="还没有日记，开始创建一篇吧！">
            <el-button type="primary" @click="createNewDiary(false)">写第一篇日记</el-button>
          </el-empty>
        </div>
        
        <el-scrollbar height="500px" v-else>
          <div class="diary-list">
            <div 
              v-for="diary in diaries" 
              :key="diary.id" 
              class="diary-item"
              :class="{ 'active': selectedDiary?.id === diary.id }"
              @click="selectDiary(diary)"
            >
              <div class="diary-meta">
                <el-tag size="small" effect="plain">{{ formatDate(diary.createTime).split(' ')[0] }}</el-tag>
                <span class="diary-time">{{ formatDate(diary.createTime).split(' ')[1] }}</span>
              </div>
              <h3 class="diary-title">{{ diary.title }}</h3>
              <p class="diary-excerpt">{{ diary.content.substring(0, 80) }}{{ diary.content.length > 80 ? '...' : '' }}</p>
              <div class="diary-tags" v-if="diary.tags && diary.tags.length">
                <el-tag 
                  v-for="tag in diary.tags" 
                  :key="tag" 
                  size="small"
                  type="success"
                  effect="light"
                >{{ tag }}</el-tag>
              </div>
              <div class="diary-mood" v-if="diary.mood">
                <el-tag type="info" size="small" effect="plain">心情: {{ diary.mood }}</el-tag>
              </div>
            </div>
          </div>
        </el-scrollbar>
      </div>
      
      <!-- 主内容区域：日记详情 -->
      <div class="main-content">
        <div v-if="selectedDiary" class="diary-detail">
          <div v-if="!editMode" class="diary-view">
            <div class="diary-header">
              <div class="diary-info">
                <h2 class="diary-title">{{ selectedDiary.title }}</h2>
                <p class="diary-date-full">{{ formatDate(selectedDiary.createTime) }}</p>
              </div>
              <div class="diary-actions">
                <el-button type="primary" plain @click="editMode = true">
                  <el-icon><EditPen /></el-icon>
                  编辑
                </el-button>
                <el-popconfirm
                  title="确定要删除这篇日记吗？"
                  @confirm="deleteDiaryItem"
                  confirm-button-text="确定"
                  cancel-button-text="取消"
                >
                  <template #reference>
                    <el-button type="danger" plain>
                      <el-icon><Delete /></el-icon>
                      删除
                    </el-button>
                  </template>
                </el-popconfirm>
              </div>
            </div>
            
            <el-divider />
            
            <div class="diary-content-view">
              <p v-for="(paragraph, index) in contentParagraphs" :key="index" class="content-paragraph">
                {{ paragraph }}
              </p>
            </div>
            
            <el-divider />
            
            <div class="diary-footer">
              <div class="diary-tags" v-if="selectedDiary.tags && selectedDiary.tags.length">
                <span class="tags-label">标签:</span>
                <el-tag 
                  v-for="tag in selectedDiary.tags" 
                  :key="tag" 
                  size="small"
                  type="success"
                  effect="light"
                  class="ml-1"
                >{{ tag }}</el-tag>
              </div>
              <div class="diary-mood" v-if="selectedDiary.mood">
                <span class="mood-label">心情:</span>
                <el-tag type="info">{{ selectedDiary.mood }}</el-tag>
              </div>
            </div>
          </div>
          
          <div v-else class="diary-edit">
            <div class="edit-header">
              <h2>编辑日记</h2>
            </div>
            
            <el-form label-position="top" :model="editForm">
              <el-form-item label="标题">
                <el-input v-model="editForm.title" placeholder="日记标题" />
              </el-form-item>
              
              <el-form-item label="内容">
                <el-input 
                  v-model="editForm.content" 
                  type="textarea"
                  :autosize="{ minRows: 8, maxRows: 15 }"
                  placeholder="今天发生了什么..."
                />
              </el-form-item>
              
              <el-form-item label="标签">
                <el-tag
                  v-for="tag in editForm.tags"
                  :key="tag"
                  closable
                  @close="removeTag(tag)"
                  class="mr-1 mb-1"
                >
                  {{ tag }}
                </el-tag>
                <el-input
                  v-if="inputTagVisible"
                  ref="tagInputRef"
                  v-model="tagInputValue"
                  @keyup.enter="handleTagInputConfirm"
                  @blur="handleTagInputConfirm"
                  class="tag-input"
                  placeholder="请输入标签并按Enter"
                  size="small"
                />
                <el-button v-else plain size="small" @click="showTagInput">+ 添加标签</el-button>
              </el-form-item>
              
              <el-form-item label="心情">
                <el-select v-model="editForm.mood" placeholder="选择心情">
                  <el-option label="开心" value="开心" />
                  <el-option label="平静" value="平静" />
                  <el-option label="疲惫" value="疲惫" />
                  <el-option label="伤心" value="伤心" />
                  <el-option label="愤怒" value="愤怒" />
                  <el-option label="焦虑" value="焦虑" />
                </el-select>
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="saveDiary" :loading="saving">保存</el-button>
                <el-button @click="editMode = false">取消</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
        
        <div v-else class="empty-detail">
          <el-empty 
            description="选择一篇日记查看详情或创建一篇新的日记"
            :image-size="200"
          >
            <el-button type="primary" @click="createNewDiary(false)">创建新日记</el-button>
          </el-empty>
        </div>
      </div>
    </div>
    
    <!-- AI与模板区域 -->
    <el-row :gutter="20" class="feature-section">
      <el-col :span="24">
        <el-card class="feature-card template-card">
          <template #header>
            <div class="card-header">
              <h3>日记模板</h3>
              <el-tag type="primary" effect="dark">快速创建</el-tag>
            </div>
          </template>
          <p class="card-description">
            选择一个预设的模板快速创建日记，让记录更加高效。想要使用AI智能写日记？
            <router-link to="/ai-diary" class="ai-link">前往AI日记</router-link>
          </p>
          <el-select v-model="selectedTemplate" placeholder="选择模板" class="template-select">
            <el-option 
              v-for="template in templates" 
              :key="template.id" 
              :label="template.name" 
              :value="template.id"
            />
          </el-select>
          <div class="template-preview" v-if="selectedTemplate && getLocalTemplate(selectedTemplate)">
            <p>{{ getLocalTemplate(selectedTemplate)?.description || '无描述' }}</p>
          </div>
          <div class="card-footer">
            <el-button 
              type="primary" 
              @click="createFromTemplate" 
              :disabled="!selectedTemplate || templateLoading"
              :loading="templateLoading"
            >
              使用模板
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue';
import { formatDate } from '@/utils/dateFormatter';
import * as API from '@/api/index.ts';
import type { Diary, DiaryTemplate } from '@/api/index.ts';
import { ElMessage } from 'element-plus';
import { Plus, Delete, EditPen } from '@element-plus/icons-vue';

// 状态定义
const diaries = ref<Diary[]>([]);
const loading = ref(true);
const saving = ref(false);
const templateLoading = ref(false);
const selectedDiary = ref<Diary | null>(null);
const editMode = ref(false);
const editForm = ref({
  title: '',
  content: '',
  tags: [] as string[],
  mood: ''
});

// 标签输入相关
const inputTagVisible = ref(false);
const tagInputValue = ref('');
const tagInputRef = ref<HTMLInputElement | null>(null);

// 模板相关
const templates = ref<DiaryTemplate[]>([]);
const selectedTemplate = ref<number | null>(null);

// 内容分段显示
const contentParagraphs = computed(() => {
  if (!selectedDiary.value) return [];
  return selectedDiary.value.content.split('\n').filter(p => p.trim() !== '');
});

// 获取日记列表
const fetchDiaries = async () => {
  try {
    loading.value = true;
    
    // 调用实际的API
    diaries.value = await API.getDiaries();
    console.log('获取到日记列表:', diaries.value);
    
    loading.value = false;
  } catch (error) {
    console.error('获取日记失败:', error);
    ElMessage.error('获取日记失败，请稍后再试');
    loading.value = false;
  }
};

// 获取模板列表
const fetchTemplates = async () => {
  try {
    console.log('正在获取模板列表...');
    const result = await API.getTemplates();
    console.log('获取到模板列表:', result);
    templates.value = result;
  } catch (error) {
    console.error('获取模板失败:', error);
    ElMessage.error('获取模板失败，请稍后再试');
  }
};

// 根据ID在本地获取模板
const getLocalTemplate = (id: number): DiaryTemplate | undefined => {
  return templates.value.find(t => t.id === id);
};

// 使用模板创建日记
const createFromTemplate = async () => {
  if (!selectedTemplate.value) return;
  
  try {
    templateLoading.value = true;
    
    // 先尝试从本地缓存中获取模板
    let template = getLocalTemplate(selectedTemplate.value);
    
    // 如果本地没有，则从API获取
    if (!template) {
      try {
        template = await API.getTemplateById(selectedTemplate.value);
      } catch (error) {
        console.error('获取模板详情失败:', error);
        ElMessage.error('获取模板详情失败');
        return;
      }
    }
    
    if (!template) {
      ElMessage.warning('模板不存在');
      return;
    }
    
    // 使用模板创建新日记
    createNewDiary(false, template.name, template.content);
    selectedTemplate.value = null;
  } catch (error) {
    console.error('使用模板失败:', error);
    ElMessage.error('使用模板失败，请稍后再试');
  } finally {
    templateLoading.value = false;
  }
};

// 选择日记
const selectDiary = (diary: Diary) => {
  selectedDiary.value = diary;
  editForm.value = {
    title: diary.title,
    content: diary.content,
    tags: diary.tags || [],
    mood: diary.mood || ''
  };
  editMode.value = false;
};

// 标签相关方法
const showTagInput = () => {
  inputTagVisible.value = true;
  nextTick(() => {
    tagInputRef.value?.focus();
  });
};

const handleTagInputConfirm = () => {
  if (tagInputValue.value && !editForm.value.tags.includes(tagInputValue.value)) {
    editForm.value.tags.push(tagInputValue.value);
  }
  inputTagVisible.value = false;
  tagInputValue.value = '';
};

// 移除标签
const removeTag = (tagToRemove: string) => {
  editForm.value.tags = editForm.value.tags.filter(tag => tag !== tagToRemove);
};

// 保存日记
const saveDiary = async () => {
  if (!selectedDiary.value) return;
  
  if (!editForm.value.title.trim()) {
    ElMessage.warning('标题不能为空');
    return;
  }
  
  try {
    saving.value = true;
    console.log('保存日记:', editForm.value);
    
    // 调用实际的API
    const result = await API.updateDiary(selectedDiary.value.id, {
      title: editForm.value.title,
      content: editForm.value.content,
      tags: editForm.value.tags,
      mood: editForm.value.mood
    });
    
    // 更新选中的日记
    selectedDiary.value = result;
    
    // 更新列表中的日记
    const index = diaries.value.findIndex(d => d.id === result.id);
    if (index !== -1) {
      diaries.value[index] = result;
    }
    
    editMode.value = false;
    
    ElMessage.success('日记已保存');
  } catch (error) {
    console.error('保存日记失败:', error);
    ElMessage.error('保存失败，请重试');
  } finally {
    saving.value = false;
  }
};

// 删除日记
const deleteDiaryItem = async () => {
  if (!selectedDiary.value) return;
  
  try {
    // 调用实际的API
    await API.deleteDiary(selectedDiary.value.id);
    
    // 从列表中移除
    diaries.value = diaries.value.filter(d => d.id !== selectedDiary.value?.id);
    selectedDiary.value = null;
    
    ElMessage.success('日记已删除');
  } catch (error) {
    console.error('删除日记失败:', error);
    ElMessage.error('删除失败，请重试');
  }
};

// 创建新日记
const createNewDiary = async (fromTemplate = false, draftTitle = '新日记', draftContent = '') => {
  try {
    console.log('创建新日记:', { title: draftTitle, content: draftContent });
    const result = await API.createDiary({
      title: draftTitle,
      content: draftContent,
      tags: [],
      mood: ''
    });
    console.log('创建日记结果:', result);
    
    // 添加到列表
    diaries.value.unshift(result);
    
    // 选中新创建的日记
    selectedDiary.value = result;
    editForm.value = {
      title: result.title,
      content: result.content,
      tags: result.tags || [],
      mood: result.mood || ''
    };
    
    editMode.value = true;
    ElMessage.success('日记创建成功，请编辑内容');
  } catch (error) {
    console.error('创建日记失败:', error);
    ElMessage.error('创建失败，请重试');
  }
};

// 初始化
onMounted(async () => {
  console.log('组件挂载，准备初始化...');
  
  // 检查localStorage中是否已有token
  const token = localStorage.getItem('token');
  if (!token) {
    console.log('未找到token，尝试登录...');
    try {
      // 尝试登录
      await API.login({
        userAccount: 'user123',
        userPassword: 'password123'
      });
      console.log('登录成功，token已自动保存');
    } catch (error: any) {
      console.error('自动登录失败:', error);
      // 登录失败仍继续获取数据，因为部分API可能不需要认证
    }
  } else {
    console.log('已有token，跳过登录步骤');
  }
  
  // 从URL查询参数判断是否需要创建新日记（来自模板广场）
  const query = new URLSearchParams(window.location.search);
  const createFromDraft = query.get('createFromDraft') === 'true';
  
  if (createFromDraft) {
    // 尝试从sessionStorage获取草稿
    const draftJson = sessionStorage.getItem('draftDiary');
    if (draftJson) {
      try {
        const draft = JSON.parse(draftJson);
        // 创建新日记
        await createNewDiary(false, draft.title, draft.content);
        // 清除sessionStorage，防止重复创建
        sessionStorage.removeItem('draftDiary');
        
        // 移除URL中的查询参数，避免刷新后再次触发创建
        const url = new URL(window.location.href);
        url.searchParams.delete('createFromDraft');
        window.history.replaceState({}, '', url.toString());
        
      } catch (e) {
        console.error('解析草稿失败或创建日记失败:', e);
        ElMessage.error('加载草稿失败');
        sessionStorage.removeItem('draftDiary'); // 出现错误也清理草稿
      }
    }
  }
  
  // 获取数据
  fetchDiaries();
  fetchTemplates();
});
</script>

<style scoped>
.diary-container {
  max-width: 100%;
  width: 100%;
  margin: 0 auto;
  padding: 20px;
}

.page-title {
  font-size: 2rem;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
  font-weight: 600;
}

.content-wrapper {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
}

.sidebar {
  width: 320px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  height: 600px;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #eee;
}

.sidebar-header h2 {
  font-size: 18px;
  margin: 0;
  color: #333;
}

.diary-list {
  overflow-y: auto;
  flex-grow: 1;
}

.diary-item {
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: all 0.2s;
}

.diary-item:hover {
  background-color: #f9f9f9;
}

.diary-item.active {
  background-color: #f0f7f0;
  border-left: 4px solid #4caf50;
}

.diary-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.diary-date, .diary-time {
  font-size: 12px;
  color: #888;
}

.diary-title {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 8px;
  color: #333;
}

.diary-excerpt {
  font-size: 14px;
  color: #666;
  margin: 0 0 8px;
  line-height: 1.4;
}

.diary-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 10px;
}

.main-content {
  flex-grow: 1;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  min-height: 600px;
  display: flex;
  flex-direction: column;
}

.diary-detail {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.diary-view, .diary-edit {
  padding: 24px;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.diary-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.diary-info {
  flex-grow: 1;
}

.diary-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 10px;
  color: #333;
}

.diary-date-full {
  font-size: 14px;
  color: #888;
  margin: 0;
}

.diary-actions {
  display: flex;
  gap: 10px;
}

.diary-content-view {
  flex-grow: 1;
  line-height: 1.6;
  color: #444;
  font-size: 16px;
  padding: 20px 0;
}

.content-paragraph {
  margin-bottom: 16px;
}

.diary-footer {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.tags-label, .mood-label {
  font-size: 14px;
  color: #666;
  margin-right: 8px;
}

.feature-section {
  margin-bottom: 40px;
}

.feature-card {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 18px;
}

.card-description {
  margin-bottom: 20px;
  color: #666;
}

.card-footer {
  margin-top: 20px;
  text-align: right;
}

.template-select {
  width: 100%;
  margin-bottom: 15px;
}

.template-preview {
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
  margin-bottom: 15px;
  color: #666;
}

.tag-input {
  width: 120px;
  margin-left: 8px;
  vertical-align: top;
}

.mr-1 {
  margin-right: 8px;
}

.mb-1 {
  margin-bottom: 8px;
}

.ml-1 {
  margin-left: 8px;
}

.empty-detail {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.debug-buttons {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

@media (max-width: 1200px) {
  .content-wrapper {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
    max-height: 300px;
  }
  
  .feature-section {
    flex-direction: column;
  }
  
  .feature-card {
    margin-bottom: 20px;
  }
}

.ai-link {
  color: #409eff;
  text-decoration: none;
  font-weight: bold;
}

.ai-link:hover {
  text-decoration: underline;
}
</style> 