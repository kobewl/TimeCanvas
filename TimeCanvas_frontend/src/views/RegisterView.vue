<template>
  <div class="register-page">
    <div class="register-header">
      <img src="/logo.svg" alt="logo" class="register-logo" />
      <div class="register-title-main">智能时光绘</div>
    </div>
    <el-card class="register-card" shadow="always">
      <h2 class="register-title">用户注册</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px" @keyup.enter="onRegister">
        <el-form-item label="账号" prop="userAccount">
          <el-input v-model="form.userAccount" placeholder="4-20位字母数字下划线" clearable />
        </el-form-item>
        <el-form-item label="密码" prop="userPassword">
          <el-input v-model="form.userPassword" type="password" show-password placeholder="8-20位密码" clearable />
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPassword">
          <el-input v-model="form.checkPassword" type="password" show-password placeholder="请再次输入密码" clearable />
        </el-form-item>
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="2-20位昵称" clearable />
        </el-form-item>
        <el-form-item label="邮箱" prop="userEmail">
          <el-input v-model="form.userEmail" placeholder="请输入邮箱" clearable />
        </el-form-item>
        <el-form-item label="手机号" prop="userPhone">
          <el-input v-model="form.userPhone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="onRegister" class="register-btn">注册</el-button>
        </el-form-item>
        <el-form-item>
          <span class="to-login">已有账号？<router-link to="/login">去登录</router-link></span>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import request from '../api/axios';

const router = useRouter();
const form = ref({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
  userName: '',
  userEmail: '',
  userPhone: '',
});
const rules = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 4, max: 20, message: '账号长度4-20位', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '账号只能包含字母数字下划线', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 8, max: 20, message: '密码长度8-20位', trigger: 'blur' }
  ],
  checkPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value !== form.value.userPassword) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    }, trigger: 'blur' }
  ],
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名2-20位', trigger: 'blur' }
  ],
  userEmail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  userPhone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
};
const formRef = ref();
const loading = ref(false);

const onRegister = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
    loading.value = true;
    try {
      await request.post('/user/register', form.value);
      ElMessage.success('注册成功，请登录！');
      router.push('/login');
    } catch (e: any) {
      ElMessage.error(e?.response?.data?.message || '注册失败，请检查信息');
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped>
.register-page {
  max-width: 480px;
  margin: 60px auto 0 auto;
}
.register-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 18px;
}
.register-logo {
  width: 60px;
  height: 60px;
  margin-bottom: 6px;
}
.register-title-main {
  font-size: 1.6rem;
  font-weight: bold;
  color: #409eff;
  letter-spacing: 2px;
}
.register-card {
  padding: 36px 28px 24px 28px;
  border-radius: 18px;
  box-shadow: 0 4px 24px rgba(64,158,255,0.10);
  border: none;
}
.register-title {
  text-align: center;
  margin-bottom: 24px;
  color: #222;
  font-size: 1.2rem;
  font-weight: 600;
}
.register-btn {
  width: 100%;
  border-radius: 24px;
  font-size: 1.1rem;
  font-weight: 600;
  letter-spacing: 1px;
  transition: background 0.2s;
}
.register-btn:hover {
  background: #66b1ff;
}
.to-login {
  font-size: 14px;
  color: #888;
  display: block;
  width: 100%;
  text-align: right;
}
.to-login a {
  color: #409eff;
  text-decoration: underline;
  margin-left: 4px;
}
</style> 