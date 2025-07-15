<template>
  <div class="template-square-page">
    <div class="template-header">
      <h1>模板广场</h1>
      <p class="desc">挑选你喜欢的日记模板，让AI帮你写出更有风格的日记。</p>
    </div>
    <el-row :gutter="24">
      <el-col :xs="24" :sm="12" :md="8" v-for="item in templates" :key="item.id">
        <el-card class="template-card" shadow="hover">
          <div class="template-title">{{ item.type || '通用模板' }}</div>
          <div class="template-content">{{ item.content }}</div>
          <div class="template-tags">
            <el-tag v-for="(tag, idx) in (item.tags ? item.tags.split(',') : [])" :key="idx" type="info" effect="plain">{{ tag }}</el-tag>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
// TODO: 后续可从后端获取模板列表
const templates = ref([
  { id: 1, type: '简洁', content: '适合日常简要记录，突出重点。', tags: '日常,简洁' },
  { id: 2, type: '情感', content: '适合表达心情、情感和感悟。', tags: '情感,心情' },
  { id: 3, type: '详细', content: '适合详细记录一天的每个细节。', tags: '详细,回顾' },
]);

onMounted(() => {
  // 后续可请求后端接口获取模板数据
});
</script>

<style scoped>
.template-square-page {
  max-width: 1100px;
  margin: 0 auto;
  padding: 32px 0 40px 0;
}
.template-header {
  text-align: center;
  margin-bottom: 32px;
}
.template-header h1 {
  font-size: 2rem;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}
.desc {
  color: #888;
  font-size: 1.1rem;
}
.template-card {
  border-radius: 16px;
  box-shadow: 0 4px 18px rgba(64,158,255,0.08);
  border: none;
  margin-bottom: 24px;
  min-height: 140px;
  background: linear-gradient(135deg, #fafdff 60%, #eaf3ff 100%);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.template-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 8px;
}
.template-content {
  font-size: 15px;
  color: #333;
  margin-bottom: 10px;
}
.template-tags {
  margin-top: 6px;
}
</style> 