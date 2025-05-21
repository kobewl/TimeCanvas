<template>
  <div class="ai-diary-container">
    <h1 class="page-title">AI智能写日记</h1>
    
    <el-row :gutter="24">
      <el-col :span="24" :md="12">
        <el-card class="input-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <h2><el-icon class="header-icon"><EditPen /></el-icon> 输入提示词</h2>
              <el-tag type="danger" effect="dark" class="header-tag">智能助手</el-tag>
            </div>
          </template>
          
          <p class="description">
            简单描述一下今天的经历，AI将为你生成完整、详细的日记内容。
          </p>
          
          <el-form>
            <!-- 提示词输入框 -->
            <el-form-item>
              <el-input
                v-model="aiPrompt"
                type="textarea"
                :autosize="{ minRows: 5, maxRows: 10 }"
                placeholder="例如：今天和朋友去了海边，看了日落，感觉很放松..."
                class="prompt-input"
              />
            </el-form-item>
            
            <!-- 模板选择 -->
            <el-form-item label="选择模板风格">
              <el-select v-model="selectedTemplateId" placeholder="选择一个模板风格" style="width: 100%">
                <el-option label="不使用模板" :value="0" />
                <el-option
                  v-for="template in templates"
                  :key="template.id"
                  :label="template.name"
                  :value="template.id"
                >
                  <div class="template-option">
                    <span>{{ template.name }}</span>
                    <el-tooltip :content="template.description || '无描述'" placement="right">
                      <el-icon><InfoFilled /></el-icon>
                    </el-tooltip>
                  </div>
                </el-option>
              </el-select>
            </el-form-item>
            
            <el-form-item>
              <el-button 
                type="primary" 
                @click="generateDiary" 
                :loading="aiLoading"
                :disabled="!aiPrompt.trim()"
                style="width: 100%"
                size="large"
                class="generate-btn"
              >
                <el-icon class="btn-icon"><MagicStick /></el-icon> 生成日记
              </el-button>
            </el-form-item>
          </el-form>
          
          <el-divider content-position="center">历史提示词</el-divider>
          
          <div class="prompt-history">
            <el-tag
              v-for="(prompt, index) in promptHistory"
              :key="index"
              closable
              @click="useHistoryPrompt(prompt)"
              @close="removeHistoryPrompt(index)"
              class="prompt-tag"
              effect="light"
            >
              {{ prompt.substring(0, 20) }}{{ prompt.length > 20 ? '...' : '' }}
            </el-tag>
            
            <div v-if="promptHistory.length === 0" class="empty-history">
              <p>还没有历史提示词</p>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="24" :md="12">
        <el-card class="result-card" shadow="hover" v-loading="aiLoading" element-loading-text="AI正在创作中...">
          <template #header>
            <div class="card-header">
              <h2><el-icon class="header-icon"><Document /></el-icon> AI生成结果</h2>
              <div class="header-actions" v-if="generatedDiary">
                <el-button type="success" size="small" @click="openSaveDialog">
                  <el-icon><Check /></el-icon> 保存到日记
                </el-button>
                <el-button type="primary" size="small" @click="editBeforeSave">
                  <el-icon><Edit /></el-icon> 编辑后保存
                </el-button>
              </div>
            </div>
          </template>
          
          <div v-if="generatedDiary" class="diary-result">
            <h2 class="diary-title">{{ generatedDiary.title }}</h2>
            <div class="diary-meta">
              <span class="diary-date">{{ formatDate(generatedDiary.createTime) }}</span>
            </div>
            
            <!-- 使用 markdown 渲染内容 -->
            <div class="markdown-content">
              <div v-html="renderedContent"></div>
            </div>
            
            <!-- 分析出的标签和心情 -->
            <div class="analysis-result" v-if="hasAnalyzedContent">
              <el-divider content-position="center">AI分析</el-divider>
              <div class="tags-and-mood">
                <div class="ai-tags" v-if="extractedTags.length > 0">
                  <span class="analysis-label">标签:</span>
                  <el-tag 
                    v-for="tag in extractedTags" 
                    :key="tag" 
                    size="small"
                    type="success"
                    effect="light"
                    class="ai-tag"
                  >{{ tag }}</el-tag>
                </div>
                
                <div class="ai-mood" v-if="extractedMood">
                  <span class="analysis-label">心情:</span>
                  <el-tag size="small" :type="getMoodType(extractedMood)">{{ extractedMood }}</el-tag>
                </div>
              </div>
            </div>
          </div>
          
          <div v-else class="empty-result">
            <el-empty description="请输入提示词生成日记内容">
              <template #image>
                <el-icon class="empty-icon"><EditPen /></el-icon>
              </template>
            </el-empty>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 保存日记对话框 -->
    <el-dialog
      v-model="saveDialogVisible"
      title="保存日记"
      width="500px"
      custom-class="save-dialog"
      :close-on-click-modal="false"
    >
      <el-form :model="saveDiaryForm" label-position="top">
        <el-form-item label="标题">
          <el-input v-model="saveDiaryForm.title" />
        </el-form-item>
        
        <el-form-item label="心情">
          <el-select v-model="saveDiaryForm.mood" style="width: 100%">
            <el-option label="开心" value="开心" />
            <el-option label="平静" value="平静" />
            <el-option label="疲惫" value="疲惫" />
            <el-option label="伤心" value="伤心" />
            <el-option label="愤怒" value="愤怒" />
            <el-option label="焦虑" value="焦虑" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="标签">
          <div class="tags-container">
            <el-tag
              v-for="tag in tagList"
              :key="tag"
              closable
              @close="removeTag(tag)"
              class="tag-item"
              effect="light"
            >
              {{ tag }}
            </el-tag>
          </div>
          <div class="tag-input-container">
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
            <el-button v-else plain size="small" @click="showTagInput" class="add-tag-btn">
              <el-icon><Plus /></el-icon> 添加标签
            </el-button>
          </div>
        </el-form-item>
        
        <el-form-item label="可见性">
          <el-radio-group v-model="saveDiaryForm.visibility">
            <el-radio label="private">私密</el-radio>
            <el-radio label="public">公开</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="saveDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveDiary" :loading="saving">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick, watch } from 'vue';
import { useRouter } from 'vue-router';
import * as API from '@/api/index.ts';
import type { Diary, DiaryTemplate } from '@/api/index.ts';
import { ElMessage } from 'element-plus';
import { Check, Edit, EditPen, InfoFilled, Document, MagicStick, Plus } from '@element-plus/icons-vue';
import { marked } from 'marked';

const router = useRouter();
const aiPrompt = ref('');
const aiLoading = ref(false);
const generatedDiary = ref<Diary | null>(null);
const promptHistory = ref<string[]>([]);
const selectedTemplateId = ref(0); // 默认不使用模板
const templates = ref<DiaryTemplate[]>([]);

// 保存日记相关
const saveDialogVisible = ref(false);
const saving = ref(false);
const saveDiaryForm = ref({
  title: '',
  content: '',
  templateId: 0,
  mood: '',
  tags: '',
  visibility: 'private'
});

// 标签输入相关
const tagList = ref<string[]>([]);
const inputTagVisible = ref(false);
const tagInputValue = ref('');
const tagInputRef = ref<HTMLInputElement | null>(null);

// 提取的标签和心情, 标题
const extractedTags = ref<string[]>([]);
const extractedMood = ref('');
const extractedTitle = ref(''); // 新增：提取的标题

// 判断是否有分析结果
const hasAnalyzedContent = computed(() => {
  return extractedTags.value.length > 0 || extractedMood.value !== '';
});

// 格式化日期显示
const formatDate = (dateStr: string): string => {
  try {
    const date = new Date(dateStr);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch (e) {
    return dateStr || '未知时间';
  }
};

// 根据心情返回对应的标签类型
const getMoodType = (mood: string): string => {
  const moodMap: Record<string, string> = {
    '开心': 'success',
    '平静': 'info',
    '疲惫': 'warning',
    '伤心': 'danger',
    '愤怒': 'danger',
    '焦虑': 'warning'
  };
  return moodMap[mood] || 'info';
};

// 使用 marked 库渲染 Markdown 内容
const renderedContent = computed(() => {
  if (!generatedDiary.value) return '';
  try {
    return marked(generatedDiary.value.content);
  } catch (e) {
    console.error('Markdown 渲染错误:', e);
    return generatedDiary.value.content;
  }
});

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

// 生成日记
const generateDiary = async () => {
  if (!aiPrompt.value.trim()) {
    ElMessage.warning('请输入日记提示内容');
    return;
  }
  
  try {
    aiLoading.value = true;
    console.log('生成AI日记，提示词:', aiPrompt.value, '模板ID:', selectedTemplateId.value);
    
    // 调用API生成日记，传递模板ID
    const result = await API.generateAiDiary(aiPrompt.value, selectedTemplateId.value);
    console.log('AI生成结果:', result);
    
    // 保存生成结果
    generatedDiary.value = result;
    
    // 从内容中提取标签和心情
    extractedTags.value = extractTagsFromContent(result.content);
    extractedMood.value = extractMoodFromContent(result.content);
    // 新增：从内容中提取标题并更新 generatedDiary 的标题
    const extracted = extractTitleFromContent(result.content);
    extractedTitle.value = extracted; // Optionally store extracted title separately if needed
    if (generatedDiary.value) {
        generatedDiary.value.title = extracted || 'AI 生成日记'; // Use extracted title or fallback
    }
    
    // 保存提示词到历史记录
    savePromptToHistory(aiPrompt.value);
    
    ElMessage.success('日记生成成功');
  } catch (error) {
    console.error('AI生成日记失败:', error);
    ElMessage.error('AI生成失败，请稍后再试');
  } finally {
    aiLoading.value = false;
  }
};

// 将提示词保存到历史记录
const savePromptToHistory = (prompt: string) => {
  // 如果已存在相同提示词，则不重复添加
  if (!promptHistory.value.includes(prompt)) {
    // 限制历史记录数量为10条
    if (promptHistory.value.length >= 10) {
      promptHistory.value.pop();
    }
    
    // 添加到历史记录的开头
    promptHistory.value.unshift(prompt);
    
    // 保存到localStorage
    localStorage.setItem('promptHistory', JSON.stringify(promptHistory.value));
  }
};

// 从历史记录中使用提示词
const useHistoryPrompt = (prompt: string) => {
  aiPrompt.value = prompt;
};

// 移除历史记录中的提示词
const removeHistoryPrompt = (index: number) => {
  promptHistory.value.splice(index, 1);
  localStorage.setItem('promptHistory', JSON.stringify(promptHistory.value));
};

// 从内容中提取标签
const extractTagsFromContent = (content: string): string[] => {
  // 尝试匹配 "**标签**：tag1,tag2,tag3" 或 "标签：tag1,tag2,tag3" 格式
  const tagMatch = content.match(/\*\*标签\*\*：(.*?)($|\n)/) || content.match(/标签：(.*?)($|\n)/);
  if (tagMatch && tagMatch[1]) {
    // 分割标签并去除空格
    return tagMatch[1].split(',').map(tag => tag.trim()).filter(tag => tag);
  }
  return [];
};

// 从内容中提取心情
const extractMoodFromContent = (content: string): string => {
  // 尝试匹配 "**心情**：xxx" 或 "心情：xxx" 格式
  const moodMatch = content.match(/\*\*心情\*\*：(.*?)($|\n)/) || content.match(/心情：(.*?)($|\n)/);
  if (moodMatch && moodMatch[1]) {
    const mood = moodMatch[1].trim();
    
    // 检查提取的心情是否在预设的心情列表中
    const validMoods = ['开心', '平静', '疲惫', '伤心', '愤怒', '焦虑'];
    if (validMoods.includes(mood)) {
      return mood;
    }
    
    // 简单的情感分析，将相似词映射到预设心情
    if (/快乐|愉快|高兴|兴奋|喜悦/.test(mood)) return '开心';
    if (/平和|安静|普通|一般|正常/.test(mood)) return '平静';
    if (/累|疲乏|劳累|困倦|辛苦/.test(mood)) return '疲惫';
    if (/悲伤|难过|沮丧|郁闷|失落/.test(mood)) return '伤心';
    if (/生气|恼怒|火大|烦躁|暴躁/.test(mood)) return '愤怒';
    if (/担心|紧张|不安|忧虑/.test(mood)) return '焦虑';
  }
  return '';
};

// 新增：从内容中提取标题
const extractTitleFromContent = (content: string): string => {
  // 尝试匹配 "**标题**：xxx" 或 "标题：xxx" 格式
  const titleMatch = content.match(/\*\*标题\*\*：(.*?)($|\n)/) || content.match(/标题：(.*?)($|\n)/);
  if (titleMatch && titleMatch[1]) {
    return titleMatch[1].trim();
  }
  // 如果没有找到特定格式的标题，可以尝试查找第一个非空行作为标题，或者返回空字符串
  const firstLineMatch = content.trim().split('\n')[0];
   if (firstLineMatch) {
       // Simple heuristic: check if the first line looks like a title (not too long, no markdown lists/code blocks)
       if (firstLineMatch.length < 50 && !firstLineMatch.startsWith('-') && !firstLineMatch.startsWith('*' ) && !firstLineMatch.startsWith('1.') && !firstLineMatch.startsWith('`')) {
           return firstLineMatch;
       }
   }
  return '';
};

// 打开保存对话框
const openSaveDialog = () => {
  if (!generatedDiary.value) return;
  
  saveDiaryForm.value = {
    title: generatedDiary.value.title,
    content: generatedDiary.value.content,
    templateId: selectedTemplateId.value,
    mood: extractedMood.value || '',
    tags: '',
    visibility: 'private'
  };
  
  // 设置提取的标签
  tagList.value = [...extractedTags.value];
  
  saveDialogVisible.value = true;
};

// 直接保存到日记中
const saveDiary = async () => {
  if (!generatedDiary.value) return;
  
  // 将标签列表转换为字符串
  saveDiaryForm.value.tags = tagList.value.join(',');
  
  try {
    saving.value = true;
    
    // 使用新的保存接口
    const result = await API.saveDiary({
      title: saveDiaryForm.value.title,
      content: generatedDiary.value.content,
      templateId: selectedTemplateId.value,
      mood: saveDiaryForm.value.mood,
      tags: saveDiaryForm.value.tags,
      visibility: saveDiaryForm.value.visibility
    });
    
    saving.value = false;
    saveDialogVisible.value = false;
    
    ElMessage.success('日记已保存');
    // 跳转到日记详情页
    router.push('/diary');
  } catch (error) {
    console.error('保存日记失败:', error);
    ElMessage.error('保存失败，请重试');
    saving.value = false;
  }
};

// 编辑后再保存
const editBeforeSave = () => {
  if (!generatedDiary.value) return;
  
  // 将日记数据保存到sessionStorage，以便在日记编辑页面中使用
  sessionStorage.setItem('draftDiary', JSON.stringify({
    title: generatedDiary.value.title,
    content: generatedDiary.value.content,
    templateId: selectedTemplateId.value
  }));
  
  // 跳转到日记页面，并传递参数指示打开编辑模式
  router.push({ path: '/diary', query: { createFromDraft: 'true' } });
};

// 标签相关方法
const showTagInput = () => {
  inputTagVisible.value = true;
  nextTick(() => {
    tagInputRef.value?.focus();
  });
};

const handleTagInputConfirm = () => {
  if (tagInputValue.value && !tagList.value.includes(tagInputValue.value)) {
    tagList.value.push(tagInputValue.value);
  }
  inputTagVisible.value = false;
  tagInputValue.value = '';
};

const removeTag = (tag: string) => {
  tagList.value = tagList.value.filter(t => t !== tag);
};

// 初始化
onMounted(async () => {
  // 从localStorage加载历史提示词
  const savedHistory = localStorage.getItem('promptHistory');
  if (savedHistory) {
    try {
      promptHistory.value = JSON.parse(savedHistory);
    } catch (e) {
      console.error('解析历史提示词失败:', e);
    }
  }
  
  // 获取模板列表
  await fetchTemplates();
});
</script>

<style scoped>
.ai-diary-container {
  max-width: 1280px;
  width: 100%;
  margin: 0 auto;
  padding: 30px 20px;
}

.page-title {
  font-size: 2rem;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
  font-weight: 600;
  letter-spacing: 2px;
  position: relative;
}

.page-title::after {
  content: '';
  display: block;
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, #4caf50, #8bc34a);
  margin: 12px auto 0;
  border-radius: 3px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  display: flex;
  align-items: center;
  font-size: 1.2rem;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.header-icon {
  margin-right: 8px;
  font-size: 1.1em;
}

.header-tag {
  padding: 2px 8px;
  font-size: 0.8rem;
}

.description {
  color: #666;
  margin-bottom: 20px;
  line-height: 1.5;
  font-size: 0.95rem;
}

.input-card, .result-card {
  height: 100%;
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s;
}

.input-card:hover, .result-card:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
  transform: translateY(-5px);
}

.prompt-input textarea {
  border-radius: 6px;
  font-size: 0.95rem;
  padding: 12px;
  transition: all 0.3s;
}

.prompt-input textarea:focus {
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.2);
}

.generate-btn {
  transition: all 0.3s;
  border-radius: 6px;
  font-weight: 600;
  letter-spacing: 1px;
  box-shadow: 0 4px 10px rgba(76, 175, 80, 0.2);
}

.generate-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(76, 175, 80, 0.3);
}

.btn-icon {
  margin-right: 8px;
  font-size: 1.2em;
}

.prompt-history {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 15px;
  min-height: 40px;
}

.prompt-tag {
  cursor: pointer;
  margin-bottom: 8px;
  transition: all 0.2s;
}

.prompt-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
}

.empty-history {
  color: #999;
  text-align: center;
  padding: 10px 0;
  width: 100%;
  font-style: italic;
}

.diary-result {
  padding: 16px;
}

.diary-title {
  font-size: 1.6rem;
  color: #333;
  margin-bottom: 15px;
  line-height: 1.3;
  font-weight: 600;
}

.diary-meta {
  font-size: 0.9rem;
  color: #888;
  margin-bottom: 25px;
  display: flex;
  align-items: center;
}

.diary-date::before {
  content: "📅";
  margin-right: 5px;
}

.markdown-content {
  line-height: 1.7;
  color: #333;
  font-size: 1rem;
}

.markdown-content :deep(h1),
.markdown-content :deep(h2),
.markdown-content :deep(h3) {
  margin-top: 1.2em;
  margin-bottom: 0.8em;
  color: #333;
  font-weight: 600;
}

.markdown-content :deep(p) {
  margin-bottom: 1em;
}

.markdown-content :deep(ul),
.markdown-content :deep(ol) {
  padding-left: 1.5em;
  margin-bottom: 1em;
}

.markdown-content :deep(li) {
  margin-bottom: 0.5em;
}

.markdown-content :deep(blockquote) {
  border-left: 4px solid #eaeaea;
  padding-left: 1em;
  margin-left: 0;
  color: #666;
  font-style: italic;
}

.empty-result {
  padding: 60px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.empty-icon {
  font-size: 48px;
  color: #ddd;
}

.analysis-result {
  margin-top: 25px;
  padding-top: 5px;
}

.tags-and-mood {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin: 15px 0;
}

.ai-tags, .ai-mood {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.analysis-label {
  font-size: 0.9rem;
  color: #666;
  margin-right: 8px;
  font-weight: 500;
}

.ai-tag {
  margin-right: 6px;
  margin-bottom: 6px;
}

.template-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 10px;
}

.tag-item {
  margin-right: 8px;
  margin-bottom: 8px;
  transition: all 0.2s;
}

.tag-input-container {
  margin-top: 8px;
}

.tag-input {
  width: 120px;
  margin-left: 8px;
  vertical-align: bottom;
}

.add-tag-btn {
  transition: all 0.2s;
}

.add-tag-btn:hover {
  background-color: #f0f9eb;
  color: #67c23a;
}

.save-dialog :deep(.el-dialog__title) {
  font-weight: 600;
  font-size: 1.2rem;
}

.save-dialog :deep(.el-form-item__label) {
  font-weight: 500;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 1.5rem;
    margin-bottom: 20px;
  }
  
  .ai-diary-container {
    padding: 20px 12px;
  }
  
  .diary-title {
    font-size: 1.3rem;
  }
  
  .tags-and-mood {
    flex-direction: column;
    gap: 10px;
  }
}
</style> 