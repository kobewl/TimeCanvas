<template>
  <el-card 
    :body-style="{ padding: bodyPadding }" 
    :shadow="shadow" 
    class="content-card"
    :class="{ 'hover-effect': hoverEffect }"
  >
    <template v-if="$slots.header || title" #header>
      <div class="card-header">
        <div class="header-left">
          <el-icon v-if="icon" class="header-icon">
            <component :is="icon" />
          </el-icon>
          <span class="header-title">{{ title }}</span>
          <el-tag v-if="tag" size="small" :type="tagType" class="header-tag">
            {{ tag }}
          </el-tag>
        </div>
        
        <div v-if="$slots.action" class="header-action">
          <slot name="action"></slot>
        </div>
      </div>
    </template>
    
    <div class="card-content" :class="{ 'is-loading': loading }">
      <el-skeleton v-if="loading" :rows="loadingRows" animated />
      <slot v-else></slot>
    </div>
    
    <div v-if="$slots.footer" class="card-footer">
      <slot name="footer"></slot>
    </div>
  </el-card>
</template>

<script setup lang="ts">
defineProps({
  title: {
    type: String,
    default: ''
  },
  icon: {
    type: String,
    default: ''
  },
  tag: {
    type: String,
    default: ''
  },
  tagType: {
    type: String,
    default: 'info'
  },
  shadow: {
    type: String,
    default: 'hover'
  },
  bodyPadding: {
    type: String,
    default: '20px'
  },
  loading: {
    type: Boolean,
    default: false
  },
  loadingRows: {
    type: Number,
    default: 4
  },
  hoverEffect: {
    type: Boolean,
    default: false
  }
});
</script>

<style scoped>
.content-card {
  transition: all 0.3s;
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
}

.hover-effect:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-icon {
  margin-right: 8px;
  font-size: 1.1em;
  color: var(--el-color-primary);
}

.header-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.header-tag {
  margin-left: 10px;
}

.card-content {
  min-height: 50px;
}

.card-content.is-loading {
  padding: 20px 0;
}

.card-footer {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid var(--el-border-color-light);
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 