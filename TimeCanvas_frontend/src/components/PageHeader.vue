<template>
  <div class="page-header">
    <div class="title-wrapper">
      <h1 class="title">
        <slot name="icon">
          <el-icon class="header-icon"><component :is="icon" /></el-icon>
        </slot>
        {{ title }}
      </h1>
      <div v-if="$slots.actions" class="header-actions">
        <slot name="actions"></slot>
      </div>
    </div>
    <p v-if="description" class="description">{{ description }}</p>
    <el-divider v-if="showDivider" />
  </div>
</template>

<script setup lang="ts">
defineProps({
  title: {
    type: String,
    required: true
  },
  description: {
    type: String,
    default: ''
  },
  icon: {
    type: String,
    default: 'Document'
  },
  showDivider: {
    type: Boolean,
    default: false
  }
});
</script>

<style scoped>
.page-header {
  margin-bottom: 24px;
  animation: fade-in 0.5s ease-out;
}

.title-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.title {
  font-size: 1.8rem;
  font-weight: 600;
  color: var(--el-text-color-primary);
  display: flex;
  align-items: center;
  margin: 0;
}

.header-icon {
  margin-right: 10px;
  font-size: 1.4rem;
  color: var(--el-color-primary);
}

.description {
  color: var(--el-text-color-secondary);
  font-size: 1rem;
  margin-top: 8px;
  max-width: 800px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

@keyframes fade-in {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 768px) {
  .title-wrapper {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .header-actions {
    align-self: flex-start;
  }
  
  .title {
    font-size: 1.6rem;
  }
}
</style> 