<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="login-header">
        <div class="logo-container">
          <img src="@/assets/logo.png" alt="智绘时光" class="logo">
          <h1 class="brand-name">智绘时光</h1>
        </div>
        <p class="tagline">AI智能日记 · 记录美好生活</p>
      </div>
      
      <el-tabs v-model="activeTab" class="login-tabs">
        <el-tab-pane name="login" label="登录">
          <el-form 
            ref="loginFormRef" 
            :model="loginForm" 
            :rules="loginRules" 
            @submit.prevent="handleLogin"
            label-position="top"
          >
            <el-form-item label="用户名" prop="username">
              <el-input 
                v-model="loginForm.username" 
                placeholder="请输入用户名"
                prefix-icon="User"
              />
            </el-form-item>
            
            <el-form-item label="密码" prop="password">
              <el-input 
                v-model="loginForm.password" 
                :type="showPassword ? 'text' : 'password'" 
                placeholder="请输入密码"
                prefix-icon="Lock"
                :show-password="true"
              />
            </el-form-item>
            
            <div class="form-options">
              <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
              <el-link type="primary" class="forgot-password">忘记密码？</el-link>
            </div>
            
            <el-button 
              type="primary" 
              :loading="loginLoading" 
              @click="handleLogin"
              class="submit-btn"
              round
            >
              {{ loginLoading ? '登录中...' : '登录' }}
            </el-button>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane name="register" label="注册">
          <el-form 
            ref="registerFormRef" 
            :model="registerForm" 
            :rules="registerRules" 
            @submit.prevent="handleRegister"
            label-position="top"
          >
            <el-form-item label="用户名" prop="username">
              <el-input 
                v-model="registerForm.username" 
                placeholder="请输入用户名"
                prefix-icon="User"
              />
            </el-form-item>
            
            <el-form-item label="邮箱" prop="email">
              <el-input 
                v-model="registerForm.email" 
                placeholder="请输入邮箱"
                prefix-icon="Message"
              />
            </el-form-item>
            
            <el-form-item label="密码" prop="password">
              <el-input 
                v-model="registerForm.password" 
                :type="showPassword ? 'text' : 'password'" 
                placeholder="请输入密码"
                prefix-icon="Lock"
                :show-password="true"
              />
            </el-form-item>
            
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input 
                v-model="registerForm.confirmPassword" 
                :type="showPassword ? 'text' : 'password'" 
                placeholder="请再次输入密码"
                prefix-icon="Lock"
                :show-password="true"
              />
              <div class="password-match" v-if="registerForm.password && registerForm.confirmPassword">
                <el-tag v-if="registerForm.password === registerForm.confirmPassword" type="success" size="small">
                  <el-icon><Check /></el-icon> 密码匹配
                </el-tag>
                <el-tag v-else type="danger" size="small">
                  <el-icon><Close /></el-icon> 密码不匹配
                </el-tag>
              </div>
            </el-form-item>
            
            <el-button 
              type="primary" 
              :loading="registerLoading" 
              :disabled="!canRegister"
              @click="handleRegister"
              class="submit-btn"
              round
            >
              {{ registerLoading ? '注册中...' : '注册' }}
            </el-button>
          </el-form>
        </el-tab-pane>
      </el-tabs>
      
      <el-alert
        v-if="errorMessage"
        :title="errorMessage"
        :type="alertType"
        show-icon
        center
        class="message-alert"
      />
    </el-card>
    
    <div class="decor-circle circle-1"></div>
    <div class="decor-circle circle-2"></div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { login, register } from '@/api/index.ts';
import { ElMessage } from 'element-plus';
import { Check, Close, User, Lock, Message } from '@element-plus/icons-vue';
import type { FormInstance, FormRules } from 'element-plus';

const router = useRouter();
const activeTab = ref('login');
const showPassword = ref(false);
const loginLoading = ref(false);
const registerLoading = ref(false);
const errorMessage = ref('');
const alertType = ref('error');

// 表单引用
const loginFormRef = ref<FormInstance>();
const registerFormRef = ref<FormInstance>();

// 登录表单
const loginForm = reactive({
  username: '',
  password: '',
  remember: false
});

// 注册表单
const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
});

// 表单校验规则
const loginRules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, message: '用户名长度不能小于3个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6个字符', trigger: 'blur' }
  ]
});

const registerRules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, message: '用户名长度不能小于3个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
});

// 计算属性：是否可以注册
const canRegister = computed(() => {
  return (
    registerForm.username.trim() !== '' &&
    registerForm.email.trim() !== '' &&
    registerForm.password.trim() !== '' &&
    registerForm.password === registerForm.confirmPassword
  );
});

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return;
  
  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return;
    
    try {
      errorMessage.value = '';
      loginLoading.value = true;
      
      const response = await login({
        userAccount: loginForm.username,
        userPassword: loginForm.password
      });
      
      console.log('登录成功，返回数据:', response);
      
      // 检查返回数据结构
      let userData;
      let tokenValue;
      
      if (response.data && response.data.token) {
        // 标准结构：后端返回 {code, data: {token, ...userInfo}, message}
        userData = response.data;
        tokenValue = response.data.token;
        console.log('从标准结构提取数据');
      } else if ('token' in response) {
        // 老结构：后端直接返回 {token, user}
        userData = (response as any).user;
        tokenValue = (response as any).token;
        console.log('从旧结构提取数据');
      } else {
        console.error('未知的返回数据结构:', response);
        ElMessage.error('登录成功但返回数据格式错误');
        return;
      }
      
      // 保存token到localStorage
      localStorage.setItem('token', tokenValue);
      
      // 保存用户信息
      if (userData) {
        localStorage.setItem('user', JSON.stringify(userData));
        console.log('已保存用户信息:', userData);
      }
      
      // 登录成功提示
      ElMessage.success('登录成功，即将跳转...');
      
      // 增加短暂延迟，确保localStorage更新
      setTimeout(() => {
        console.log('即将跳转到首页...');
        window.location.href = '/'; // 使用直接导航替代router.push
      }, 300);
    } catch (error: any) {
      console.error('登录失败:', error);
      errorMessage.value = error.message || '登录失败，请检查用户名和密码';
      alertType.value = 'error';
    } finally {
      loginLoading.value = false;
    }
  });
};

// 处理注册
const handleRegister = async () => {
  if (!registerFormRef.value) return;
  
  await registerFormRef.value.validate(async (valid) => {
    if (!valid) return;
    
    try {
      errorMessage.value = '';
      alertType.value = 'error'; // 默认设置为error
      registerLoading.value = true;
      
      await register({
        userAccount: registerForm.username,
        userName: registerForm.username,
        userPassword: registerForm.password,
        checkPassword: registerForm.confirmPassword,
        userEmail: registerForm.email
      });
      
      // 注册成功提示
      ElMessage.success('注册成功，请登录');
      
      // 注册成功后切换到登录选项卡
      activeTab.value = 'login';
      loginForm.username = registerForm.username;
      loginForm.password = '';
      
      // 清空注册表单
      if (registerFormRef.value) {
        registerFormRef.value.resetFields();
      }
      
      // 显示成功消息（可选，ElMessage 已经提示）
      // errorMessage.value = '注册成功，请登录';
      // alertType.value = 'success';
    } catch (error: any) {
      console.error('注册失败:', error);
      // 尝试从后端错误响应中获取具体信息
      if (error.response && error.response.data && error.response.data.message) {
        errorMessage.value = error.response.data.message;
      } else {
        errorMessage.value = '注册失败，请稍后再试';
      }
      alertType.value = 'error';
    } finally {
      registerLoading.value = false;
    }
  });
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 230px);
  width: 100%;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.login-card {
  width: 100%;
  max-width: 480px;
  border-radius: 12px;
  overflow: hidden;
  padding: 0;
  z-index: 2;
  backdrop-filter: blur(10px);
  background-color: rgba(255, 255, 255, 0.95);
}

.login-header {
  text-align: center;
  margin: 24px 0;
  padding: 0 32px;
}

.logo-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
}

.logo {
  width: 40px;
  height: 40px;
  margin-right: 12px;
}

.brand-name {
  font-size: 24px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0;
}

.tagline {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin: 8px 0 0;
}

.login-tabs {
  padding: 0 32px 32px;
}

:deep(.el-tabs__header) {
  margin-bottom: 24px;
}

:deep(.el-tabs__item) {
  font-size: 16px;
  height: 48px;
  line-height: 48px;
}

:deep(.el-tabs__active-bar) {
  background-color: var(--el-color-primary);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  margin-top: 8px;
}

.message-alert {
  margin-top: 16px;
  margin-bottom: 8px;
}

.password-match {
  margin-top: 8px;
}

/* 装饰性圆圈 */
.decor-circle {
  position: absolute;
  border-radius: 50%;
  z-index: 1;
}

.circle-1 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #4caf5080 0%, #45a54980 100%);
  top: -100px;
  left: -100px;
}

.circle-2 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, #43a04780 0%, #2e7d3280 100%);
  bottom: -50px;
  right: -50px;
}

@media (max-width: 768px) {
  .login-card {
    max-width: 95%;
  }
  
  .login-header, .login-tabs {
    padding-left: 20px;
    padding-right: 20px;
  }
  
  .circle-1, .circle-2 {
    opacity: 0.5;
  }
}
</style> 