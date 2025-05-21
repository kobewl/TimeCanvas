<template>
  <div class="profile-container">
    <div class="profile-card">
      <div class="profile-header">
        <h1>个人中心</h1>
      </div>
      
      <div v-if="!user" class="loading">
        <p>加载中...</p>
      </div>
      
      <div v-else class="profile-content">
        <div class="avatar-section">
          <div class="avatar">
            <img v-if="user.avatar" :src="user.avatar" alt="头像" />
            <div v-else class="avatar-placeholder">{{ userInitials }}</div>
          </div>
          <button class="upload-btn">更换头像</button>
        </div>
        
        <div class="info-section">
          <div class="info-item">
            <label>用户名</label>
            <div>{{ user.userName || '未设置' }}</div>
          </div>
          
          <div class="info-item">
            <label>账号</label>
            <div>{{ user.userAccount || '未设置' }}</div>
          </div>
          
          <div class="info-item">
            <label>邮箱</label>
            <div>{{ user.userEmail || '未设置' }}</div>
          </div>
          
          <div class="info-item">
            <label>手机</label>
            <div>{{ user.userPhone || '未设置' }}</div>
          </div>
          
          <div class="info-item">
            <label>性别</label>
            <div>{{ userGenderText }}</div>
          </div>
          
          <div class="info-item">
            <label>个人简介</label>
            <div>{{ user.bio || '未设置' }}</div>
          </div>
          
          <div class="info-item">
            <label>角色</label>
            <div>{{ userRoleText }}</div>
          </div>
          
          <div class="info-item">
            <label>最后登录</label>
            <div>{{ formatDate(user.lastLoginTime) }}</div>
          </div>
          
          <div class="info-item">
            <label>注册时间</label>
            <div>{{ formatDate(user.createTime) }}</div>
          </div>
        </div>
        
        <div class="action-buttons">
          <button class="edit-btn" @click="handleEditInfo">编辑信息</button>
          <button class="password-btn">修改密码</button>
          <button class="logout-btn" @click="handleLogout">退出登录</button>
        </div>
      </div>
    </div>

    <!-- 编辑信息弹窗 -->
    <el-dialog v-model="editDialogVisible" title="编辑个人信息" width="500px">
      <el-form v-if="editForm" :model="editForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="editForm.userName"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="editForm.userEmail"></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="editForm.userPhone"></el-input>
        </el-form-item>
        <el-form-item label="个人简介">
          <el-input type="textarea" v-model="editForm.bio"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="editForm.gender" placeholder="请选择性别">
            <el-option label="未知" :value="0"></el-option>
            <el-option label="男" :value="1"></el-option>
            <el-option label="女" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期">
          <el-date-picker v-model="editForm.birthDate" type="date" placeholder="选择日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleCancelEdit">取消</el-button>
          <el-button type="primary" @click="handleSaveEdit">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import type { User } from '@/api/index.ts';
import { updateUserProfile } from '@/api/index.ts';
import { ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElDatePicker, ElMessage } from 'element-plus';
import 'element-plus/theme-chalk/el-dialog.css';
import 'element-plus/theme-chalk/el-form.css';
import 'element-plus/theme-chalk/el-input.css';
import 'element-plus/theme-chalk/el-select.css';
import 'element-plus/theme-chalk/el-date-picker.css';
import 'element-plus/theme-chalk/el-option.css';
import { dayjs } from 'element-plus'

const router = useRouter();
const user = ref<User | null>(null);
const editDialogVisible = ref(false);
const editForm = ref<User | null>(null);

// 获取用户信息
onMounted(() => {
  try {
    const userJson = localStorage.getItem('user');
    if (userJson) {
      user.value = JSON.parse(userJson);
      console.log('已加载用户信息:', user.value);
    } else {
      console.error('未找到用户信息');
      // 如果没有用户信息，重定向到登录页
      router.push('/login');
    }
  } catch (error) {
    console.error('解析用户信息错误:', error);
  }
});

// 计算用户名首字母（用于头像占位符）
const userInitials = computed(() => {
  if (!user.value || !user.value.userName) return '?';
  
  const name = user.value.userName;
  return name.charAt(0).toUpperCase();
});

// 角色文本
const userRoleText = computed(() => {
  if (!user.value) return '未知';
  
  switch (user.value.userRole) {
    case 1:
      return '管理员';
    case 2:
      return 'VIP用户';
    case 3:
    default:
      return '普通用户';
  }
});

// 性别文本
const userGenderText = computed(() => {
  if (!user.value || user.value.gender === undefined || user.value.gender === null) return '未设置';
  
  // Assuming gender is stored as Integer 0:未知, 1:男, 2:女
  // Convert gender value to string for display if it's not already a string
  const genderValue = typeof user.value.gender === 'string' ? parseInt(user.value.gender, 10) : user.value.gender;
  
  switch (genderValue) {
    case 0:
      return '未知';
    case 1:
      return '男';
    case 2:
      return '女';
    default:
      return '未设置'; // Handle unexpected values
  }
});

// 格式化日期
const formatDate = (date: string | undefined) => {
  if (!date) return '未知';
  
  try {
    return new Date(date).toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  } catch (error) {
    return '日期格式错误';
  }
};

// 处理退出登录
const handleLogout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
  router.push('/login');
};

// 处理编辑信息
const handleEditInfo = () => {
  console.log('点击了编辑信息按钮');
  if (user.value) {
    editForm.value = JSON.parse(JSON.stringify(user.value));
    if (editForm.value.birthDate) {
      editForm.value.birthDate = dayjs(editForm.value.birthDate).toDate();
    }
    editDialogVisible.value = true;
  }
};

// 处理保存编辑
const handleSaveEdit = async () => {
  console.log('保存编辑', editForm.value);
  if (!editForm.value) return;

  // 构建发送给后端的数据，只包含允许更新的字段
  const updateData = {
    userName: editForm.value.userName,
    userEmail: editForm.value.userEmail,
    userPhone: editForm.value.userPhone,
    bio: editForm.value.bio,
    gender: editForm.value.gender, // 注意：如果后端期望的是String，这里可能需要转换
    birthDate: editForm.value.birthDate, // 注意：这里发送的是Date对象，后端可能期望String，如果后端需要String，前端需要格式化
  };
  
  // 再次检查并删除可能不需要发送的 undefined 字段
  for (const key in updateData) {
      if (updateData[key] === undefined) {
          delete updateData[key];
      }
  }

  try {
    // 调用后端更新用户信息的接口
    const response = await updateUserProfile(updateData);

    if (response.code === 0) { // 假设后端成功码为0
      // 更新成功
      console.log('用户信息更新成功');
      ElMessage.success('个人信息更新成功！');

      // 更新本地存储的用户信息
      if (user.value) {
        // 复制更新后的字段到原始 user 对象
        Object.assign(user.value, editForm.value);
        // 需要注意的是，如果后端返回了更新后的完整user对象，建议使用后端返回的对象
        // 这里为了简化，直接将editForm的字段复制过去
        localStorage.setItem('user', JSON.stringify(user.value));
      }

      editDialogVisible.value = false; // 关闭弹窗
    } else {
      // 更新失败，显示后端返回的错误信息
      console.error('用户信息更新失败:', response.message);
      ElMessage.error(response.message || '个人信息更新失败，请稍后再试');
      // 不关闭弹窗，让用户修改
    }
  } catch (error) {
    console.error('调用更新用户信息接口失败:', error);
    ElMessage.error('个人信息更新失败，请检查网络或稍后再试');
    // 不关闭弹窗，让用户修改
  }
};

// 处理取消编辑
const handleCancelEdit = () => {
  console.log('取消编辑');
  editDialogVisible.value = false;
};
</script>

<style scoped>
.profile-container {
  display: flex;
  justify-content: center;
  padding: 40px 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 230px); /* 减去头部和底部高度 */
}

.profile-card {
  width: 100%;
  max-width: 800px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  padding: 30px;
}

.profile-header {
  margin-bottom: 30px;
  text-align: center;
}

.profile-header h1 {
  font-size: 24px;
  color: #333;
  margin: 0;
}

.loading {
  text-align: center;
  padding: 40px 0;
  color: #666;
}

.profile-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  overflow: hidden;
  background: #e0e0e0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  font-size: 48px;
  font-weight: bold;
  color: #4caf50;
}

.upload-btn {
  background: transparent;
  color: #4caf50;
  border: 1px solid #4caf50;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
}

.upload-btn:hover {
  background: #e8f5e9;
}

.info-section {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.info-item label {
  font-size: 14px;
  color: #666;
}

.info-item div {
  font-size: 16px;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
  color: #333;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 15px;
  margin-top: 20px;
}

.action-buttons button {
  padding: 10px 20px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
}

.edit-btn {
  background: #4caf50;
  color: white;
}

.edit-btn:hover {
  background: #43a047;
}

.password-btn {
  background: #2196f3;
  color: white;
}

.password-btn:hover {
  background: #1e88e5;
}

.logout-btn {
  background: #f44336;
  color: white;
}

.logout-btn:hover {
  background: #e53935;
}

@media (max-width: 768px) {
  .info-section {
    grid-template-columns: 1fr;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .action-buttons button {
    width: 100%;
    margin-bottom: 10px;
  }
}
</style> 