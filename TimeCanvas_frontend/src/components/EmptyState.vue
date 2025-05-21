<template>
  <div class="empty-state">
    <el-empty 
      :image="image" 
      :image-size="imageSize" 
      :description="description"
    >
      <template v-if="$slots.default">
        <slot></slot>
      </template>
      <el-button v-else-if="actionText" :type="actionType" @click="$emit('action')">
        <el-icon v-if="actionIcon" class="action-icon">
          <component :is="actionIcon" />
        </el-icon>
        {{ actionText }}
      </el-button>
    </el-empty>
  </div>
</template>

<script setup lang="ts">
defineProps({
  description: {
    type: String,
    default: '暂无数据'
  },
  image: {
    type: String,
    default: ''
  },
  imageSize: {
    type: Number,
    default: 150
  },
  actionText: {
    type: String,
    default: ''
  },
  actionIcon: {
    type: String,
    default: ''
  },
  actionType: {
    type: String,
    default: 'primary'
  }
});

defineEmits(['action']);
</script>

<style scoped>
.empty-state {
  padding: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.action-icon {
  margin-right: 5px;
}

:deep(.el-empty__image) {
  margin-bottom: 20px;
}

:deep(.el-empty__description) {
  margin-bottom: 20px;
  color: var(--el-text-color-secondary);
}
</style> 