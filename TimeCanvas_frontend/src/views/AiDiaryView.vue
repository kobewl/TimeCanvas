<template>
  <div class="ai-diary-page">
    <div class="page-header">
      <h1>AI智能日记</h1>
      <p class="desc">让AI帮你记录和总结每一天，轻松生成高质量日记。</p>
    </div>
    <el-row :gutter="32" class="main-row">
      <el-col :xs="24" :md="10">
        <!-- 输入区 -->
        <el-card class="input-card" shadow="always">
          <h2 class="section-title">填写今日信息</h2>
          <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
            <el-form-item label="心情" prop="mood">
              <el-select v-model="form.mood" placeholder="请选择今日心情">
                <el-option label="开心" value="开心" />
                <el-option label="平静" value="平静" />
                <el-option label="难过" value="难过" />
                <el-option label="激动" value="激动" />
                <el-option label="焦虑" value="焦虑" />
              </el-select>
            </el-form-item>
            <el-form-item label="关键词" prop="keywords">
              <el-input v-model="form.keywords" placeholder="请输入关键词（如学习、运动、朋友等）" />
            </el-form-item>
            <el-form-item label="想法" prop="thoughts">
              <el-input
                v-model="form.thoughts"
                type="textarea"
                :rows="3"
                placeholder="可以写下你今天的感受、想法或期待..."
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="large" :loading="loading" @click="onGenerate" class="gen-btn">生成AI日记</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="14">
        <el-card class="result-card" shadow="always">
          <h2 class="section-title">AI生成结果</h2>
          <template v-if="aiDiary">
            <div class="diary-header">
              <el-tag type="success" effect="plain">{{ form.mood }}</el-tag>
              <span class="diary-date">{{ today }}</span>
            </div>
            <div class="diary-content markdown-body" v-html="aiDiaryHtml"></div>
            <div class="diary-keywords">
              <el-tag v-for="(kw, idx) in form.keywords.split(/[，,\s]+/).filter(Boolean)" :key="idx" type="info" effect="plain">{{ kw }}</el-tag>
            </div>
          </template>
          <el-empty v-else description="请填写信息并点击生成AI日记" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';
import { generateAiDiary } from '../api/diary';
import MarkdownIt from 'markdown-it';

const md = new MarkdownIt();

// 表单数据
const form = ref({
  mood: '',
  keywords: '',
  thoughts: '',
});
const rules = {
  mood: [{ required: true, message: '请选择心情', trigger: 'change' }],
  keywords: [{ required: true, message: '请输入关键词', trigger: 'blur' }],
};
const formRef = ref();
const loading = ref(false);
const aiDiary = ref('');

// 获取今天日期
const today = new Date().toLocaleDateString();

// 默认模板ID（如需支持多模板可扩展）
const templateId = 1;

// 生成AI日记（对接后端接口）
const onGenerate = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
    loading.value = true;
    try {
      // 拼接message，后端只接收message和templateId
      const message = `心情：${form.value.mood}；关键词：${form.value.keywords}；想法：${form.value.thoughts}`;
      const res = await generateAiDiary({ message, templateId });
      aiDiary.value = res.data;
      ElMessage.success('AI日记生成成功！');
    } catch (e: any) {
      ElMessage.error(e?.response?.data?.message || 'AI日记生成失败，请稍后重试');
    } finally {
      loading.value = false;
    }
  });
};

// 计算属性：将AI日记内容渲染为HTML
const aiDiaryHtml = computed(() => aiDiary.value ? md.render(aiDiary.value) : '');
</script>

<style scoped>
.ai-diary-page {
  max-width: 1100px;
  margin: 0 auto;
  padding: 32px 0 40px 0;
}
.page-header {
  text-align: center;
  margin-bottom: 32px;
}
.page-header h1 {
  font-size: 2.2rem;
  font-weight: bold;
  margin-bottom: 8px;
  color: #409eff;
}
.desc {
  color: #888;
  font-size: 1.1rem;
}
.main-row {
  margin-top: 0;
}
.input-card, .result-card {
  border-radius: 18px;
  box-shadow: 0 4px 24px rgba(64,158,255,0.08);
  border: none;
  margin-bottom: 24px;
  background: linear-gradient(135deg, #fafdff 60%, #eaf3ff 100%);
}
.input-card {
  padding-bottom: 12px;
}
.result-card {
  min-height: 350px;
  padding-bottom: 12px;
}
.section-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 18px;
  letter-spacing: 1px;
}
.gen-btn {
  width: 100%;
  border-radius: 24px;
  font-size: 1.1rem;
  font-weight: 600;
  letter-spacing: 1px;
  transition: background 0.2s;
}
.gen-btn:hover {
  background: #66b1ff;
}
.diary-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}
.diary-date {
  color: #999;
  font-size: 14px;
}
.diary-content {
  font-size: 16px;
  line-height: 1.8;
  margin-bottom: 12px;
  white-space: pre-line;
  padding: 8px 0 0 0;
}
.diary-keywords {
  margin-top: 8px;
}
/* Markdown美化 */
.markdown-body h1, .markdown-body h2, .markdown-body h3 {
  color: #409eff;
  margin: 12px 0 8px 0;
}
.markdown-body ul, .markdown-body ol {
  margin: 8px 0 8px 24px;
}
.markdown-body strong {
  color: #222;
}
.markdown-body code {
  background: #f4f4f4;
  border-radius: 4px;
  padding: 2px 6px;
  font-size: 0.95em;
}
@media (max-width: 900px) {
  .ai-diary-page {
    padding: 12px 2vw 24px 2vw;
  }
  .input-card, .result-card {
    margin-bottom: 18px;
  }
}
</style> 