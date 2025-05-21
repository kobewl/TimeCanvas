<template>
  <el-dialog
    :model-value="modelValue"
    :title="title"
    :width="width"
    :close-on-click-modal="closeOnClickModal"
    @close="$emit('update:modelValue', false)"
    class="confirm-dialog"
  >
    <div class="dialog-content">
      <el-icon v-if="icon" class="dialog-icon" :class="iconClass">
        <component :is="icon" />
      </el-icon>
      <div class="dialog-message">
        <div v-if="$slots.default">
          <slot></slot>
        </div>
        <p v-else>{{ message }}</p>
      </div>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleCancel" :disabled="loading">{{ cancelText }}</el-button>
        <el-button :type="confirmType" @click="handleConfirm" :loading="loading">
          {{ confirmText }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true
  },
  title: {
    type: String,
    default: '确认'
  },
  message: {
    type: String,
    default: '确定要执行此操作吗？'
  },
  icon: {
    type: String,
    default: 'Warning'
  },
  iconClass: {
    type: String,
    default: 'warning'
  },
  confirmText: {
    type: String,
    default: '确定'
  },
  cancelText: {
    type: String,
    default: '取消'
  },
  confirmType: {
    type: String,
    default: 'primary'
  },
  width: {
    type: String,
    default: '420px'
  },
  loading: {
    type: Boolean,
    default: false
  },
  closeOnClickModal: {
    type: Boolean,
    default: false
  }
});

const emits = defineEmits(['update:modelValue', 'confirm', 'cancel']);

const handleConfirm = () => {
  emits('confirm');
};

const handleCancel = () => {
  emits('cancel');
  emits('update:modelValue', false);
};
</script>

<style scoped>
.confirm-dialog :deep(.el-dialog__header) {
  padding-bottom: 16px;
  margin-right: 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.confirm-dialog :deep(.el-dialog__body) {
  padding: 24px;
}

.dialog-content {
  display: flex;
  align-items: flex-start;
}

.dialog-icon {
  font-size: 24px;
  margin-right: 16px;
  margin-top: 2px;
}

.dialog-icon.warning {
  color: var(--el-color-warning);
}

.dialog-icon.danger {
  color: var(--el-color-danger);
}

.dialog-icon.success {
  color: var(--el-color-success);
}

.dialog-icon.info {
  color: var(--el-color-info);
}

.dialog-message {
  flex: 1;
  font-size: 14px;
  color: var(--el-text-color-primary);
  line-height: 1.6;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style> 