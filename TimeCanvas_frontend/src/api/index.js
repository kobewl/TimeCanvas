import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',  // 假设后端运行在8080端口
  timeout: 5000,  // 超时设置
});

export const getUsers = async () => {
  try {
    const response = await api.get('/users');
    return response.data;
  } catch (error) {
    console.error('API error:', error);
    throw error;
  }
};

// Add more functions as needed, e.g., for other endpoints like /diaries 