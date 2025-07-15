<template>
  <div class="login-page">
    <div class="login-header">
      <img src="/logo.svg" alt="logo" class="login-logo" />
      <div class="login-title-main">智能时光绘</div>
    </div>
    <el-card class="login-card" shadow="always">
      <h2 class="login-title">用户登录</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px" @keyup.enter="onLogin">
        <el-form-item label="账号" prop="userAccount">
          <el-input v-model="form.userAccount" placeholder="请输入账号" clearable />
        </el-form-item>
        <el-form-item label="密码" prop="userPassword">
          <el-input v-model="form.userPassword" type="password" show-password placeholder="请输入密码" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="onLogin" class="login-btn">登录</el-button>
        </el-form-item>
        <el-form-item>
          <span class="to-register">没有账号？<router-link to="/register">去注册</router-link></span>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { userLogin } from '../api/user';

const router = useRouter();
const form = ref({
  userAccount: '',
  userPassword: '',
});
const rules = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
};
const formRef = ref();
const loading = ref(false);

const onLogin = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
    loading.value = true;
    try {
      const res = await userLogin(form.value);
      const token = res.data.data.token;
      localStorage.setItem('token', token);
      ElMessage.success('登录成功！');
      router.push('/ai-diary');
    } catch (e: any) {
      ElMessage.error(e?.response?.data?.message || '登录失败，请检查账号密码');
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped>
.login-page {
  max-width: 480px;
  margin: 60px auto 0 auto;
}
.login-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 18px;
}
.login-logo {
  width: 60px;
  height: 60px;
  margin-bottom: 6px;
}
.login-title-main {
  font-size: 1.6rem;
  font-weight: bold;
  color: #409eff;
  letter-spacing: 2px;
}
.login-card {
  padding: 36px 28px 24px 28px;
  border-radius: 18px;
  box-shadow: 0 4px 24px rgba(64,158,255,0.10);
  border: none;
}
.login-title {
  text-align: center;
  margin-bottom: 24px;
  color: #222;
  font-size: 1.2rem;
  font-weight: 600;
}
.login-btn {
  width: 100%;
  border-radius: 24px;
  font-size: 1.1rem;
  font-weight: 600;
  letter-spacing: 1px;
  transition: background 0.2s;
}
.login-btn:hover {
  background: #66b1ff;
}
.to-register {
  font-size: 14px;
  color: #888;
  display: block;
  width: 100%;
  text-align: right;
}
.to-register a {
  color: #409eff;
  text-decoration: underline;
  margin-left: 4px;
}
</style> 