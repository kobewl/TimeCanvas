import request from './axios';

// 用户登录
export function userLogin(data: { userAccount: string; userPassword: string }) {
  return request.post('/user/login', data);
} 