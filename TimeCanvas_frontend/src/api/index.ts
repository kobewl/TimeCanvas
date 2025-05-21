import axios from 'axios';

export interface User {
  // 与后端UserVO对应
  id?: number;
  userAccount?: string;
  userName: string;
  userRole?: number;
  userEmail?: string;
  userPhone?: string;
  avatar?: string;
  bio?: string;
  gender?: string;
  birthDate?: string;
  lastLoginTime?: string;
  createTime?: string;
  token?: string;
}

// 定义 UserUpdateRequest 接口，与后端DTO对应
export interface UserUpdateRequest {
  userName?: string;
  userEmail?: string;
  userPhone?: string;
  bio?: string;
  gender?: number; // Assuming gender is number in request body based on backend validation
  birthDate?: string; // Assuming birthDate is string in request body based on backend date format
}

export interface LoginRequest {
  userAccount: string;
  userPassword: string;
}

// 实际后端接口返回的数据结构
export interface ApiResponse<T> {
  code: number;
  data: T;
  message: string;
}

export interface LoginResponse {
  token: string;
  user: User;
  data?: any; // 兼容可能的data字段
}

export interface RegisterRequest {
  userAccount: string;
  userPassword: string;
  checkPassword: string;
  userName: string;
  userEmail?: string;
  userPhone?: string;
}

export interface Diary {
  id: number;
  title: string;
  content: string;
  createTime: string;
  userId?: number;
  tags?: string[];
  mood?: string;
}

export interface CreateDiaryDto {
  title: string;
  content: string;
  tags?: string[];
  mood?: string;
}

export interface GenerateDiaryDto {
  prompt: string;
}

export interface DiaryTemplate {
  id: number;
  name: string;
  content: string;
  description?: string;
  creatorId?: number;
  createTime?: string;
  updateTime?: string;
}

// 更新日记创建接口，使用ChatForm结构
export interface ChatForm {
  message: string;
  templateId?: number; // 模板ID，可选
}

// 新增保存日记接口定义
export interface SaveDiaryDto {
  title: string;
  content: string;
  templateId: number;
  mood: string;
  tags: string;
  visibility: string;
}

const api = axios.create({
  baseURL: 'http://localhost:8080',  // 确保这个URL与你的后端服务地址匹配
  timeout: 30000,  // 超时设置为30秒
  headers: {
    'Content-Type': 'application/json',
  }
});

// 请求拦截器，添加token和调试信息
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    // 如果本地存储中有token，则使用本地存储中的token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    } 
    
    // 调试信息
    console.log(`请求: ${config.method?.toUpperCase()} ${config.url}`, config.data || config.params);
    console.log('请求头:', config.headers);
    
    return config;
  },
  (error) => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器，处理错误和调试信息
api.interceptors.response.use(
  (response) => {
    // 调试信息
    console.log(`响应: ${response.config.method?.toUpperCase()} ${response.config.url}`, response.data);
    
    return response;
  },
  (error) => {
    // 调试信息
    console.error('响应错误:', error.response || error.message || error);
    
    // 处理401未授权错误
    if (error.response && error.response.status === 401) {
      // 清除token
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      
      // 如果不在登录页，重定向到登录页
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

// 用户相关API
export const login = async (data: LoginRequest): Promise<ApiResponse<User>> => {
  try {
    console.log('发送登录请求:', data);
    // 确保使用正确的登录路径
    const response = await api.post('/user/login', data);
    console.log('登录响应原始数据:', response.data);
    
    // 如果登录成功，自动保存token
    if (response.data && response.data.data && response.data.data.token) {
      localStorage.setItem('token', response.data.data.token);
      localStorage.setItem('user', JSON.stringify(response.data.data));
      console.log('已自动保存token和用户信息');
    }
    
    return response.data;
  } catch (error) {
    console.error('登录失败:', error);
    throw error; // 抛出实际错误
  }
};

export const register = async (data: RegisterRequest): Promise<User> => {
  try {
    const response = await api.post('/user/register', data);
    return response.data;
  } catch (error) {
    console.error('注册失败:', error);
    // 移除模拟注册逻辑
    // console.warn('尝试使用模拟注册...');
    // const mockUser: User = {
    //   name: data.username,
    //   username: data.username,
    //   id: Math.floor(Math.random() * 1000) + 2,
    //   email: data.email,
    //   avatar: ''
    // };
    // return mockUser;
    
    throw error; // 抛出实际错误
  }
};

export const getUsers = async (): Promise<User[]> => {
  try {
    const response = await api.get('/users');
    return response.data as User[];  // 指定返回类型
  } catch (error) {
    console.error('API error:', error);
    throw error;
  }
};

// 用户更新个人资料API
export const updateUserProfile = async (data: UserUpdateRequest): Promise<ApiResponse<Boolean>> => {
  try {
    console.log('发送更新用户信息请求:', data);
    // 调用后端 /user/updateProfile 接口
    const response = await api.post<ApiResponse<Boolean>>('/user/updateProfile', data);
    console.log('更新用户信息响应原始数据:', response.data);
    return response.data;
  } catch (error) {
    console.error('更新用户信息失败:', error);
    throw error; // 抛出实际错误
  }
};

// 日记相关API - 调用后端真实接口
export const getDiaries = async (): Promise<Diary[]> => {
  try {
    // 调用后端 /diary/list 接口获取日记列表
    const response = await api.get<ApiResponse<Diary[]>>('/diary/list');
    // 假设后端返回的数据结构是 ApiResponse<Diary[]>
    return response.data.data || [];
  } catch (error) {
    console.error('获取日记列表失败:', error);
    throw error;
  }
};

export const getDiaryById = async (id: number): Promise<Diary> => {
  try {
    // 调用后端 /diary/{id} 接口获取单个日记
    const response = await api.get<ApiResponse<Diary>>(`/diary/${id}`);
    // 假设后端返回的数据结构是 ApiResponse<Diary>
    if (!response.data.data) {
      throw new Error(`未找到ID为${id}的日记`);
    }
    return response.data.data;
  } catch (error) {
    console.error(`获取日记(ID: ${id})失败:`, error);
    throw error;
  }
};

export const updateDiary = async (id: number, diary: CreateDiaryDto): Promise<Diary> => {
  try {
    // 调用后端更新日记接口
    const response = await api.put<ApiResponse<Diary>>(`/diary/update/${id}`, diary);
    // 假设后端更新成功后返回更新后的日记对象
    return response.data.data;
  } catch (error) {
    console.error(`更新日记(ID: ${id})失败:`, error);
    throw error;
  }
};

export const deleteDiary = async (id: number): Promise<boolean> => {
  try {
    // 调用后端删除日记接口
    // 假设后端删除成功返回 ApiResponse<Boolean> 且 data 为 true
    const response = await api.delete<ApiResponse<Boolean>>(`/diary/delete/${id}`);
    return response.data.data === true;
  } catch (error) {
    console.error(`删除日记(ID: ${id})失败:`, error);
    throw error;
  }
};

// 获取日记模板列表
export const getTemplates = async (): Promise<DiaryTemplate[]> => {
  try {
    const response = await api.post('/template/list', {
      current: 1,
      pageSize: 10,
      sortField: "",
      sortOrder: ""
    });
    
    // 根据实际返回结构转换数据
    const templates = response.data.records || [];
    
    // 将后端模板转换为前端DiaryTemplate格式
    return templates.map((template: any) => ({
      id: template.id,
      name: template.type || '未命名模板',
      content: template.content || '',
      description: template.tags || '',
      creatorId: template.creatorName ? 1 : 0, // 假设系统创建的ID为0
      createTime: template.createTime,
    }));
  } catch (error) {
    console.error('获取模板列表失败:', error);
    throw error;
  }
};

// 根据ID获取单个模板 - 注意：此接口可能需要具体实现
export const getTemplateById = async (id: number): Promise<DiaryTemplate> => {
  try {
    // 由于后端尚未实现单个模板查询接口，我们先获取所有模板，然后从中找到对应ID的模板
    const templates = await getTemplates();
    const template = templates.find(t => t.id === id);
    
    if (!template) {
      throw new Error(`未找到ID为${id}的模板`);
    }
    
    return template;
  } catch (error) {
    console.error(`获取模板(ID: ${id})失败:`, error);
    throw error;
  }
};

// 创建模板
export const createTemplate = async (template: Omit<DiaryTemplate, 'id' | 'creatorId' | 'createTime' | 'updateTime'>): Promise<DiaryTemplate> => {
  try {
    const response = await api.post('/template/create', template);
    return response.data.data;
  } catch (error) {
    console.error('创建模板失败:', error);
    throw error;
  }
};

// 更新模板
export const updateTemplate = async (id: number, template: Partial<DiaryTemplate>): Promise<DiaryTemplate> => {
  try {
    const response = await api.put(`/template/update`, {
      id,
      ...template
    });
    return response.data.data;
  } catch (error) {
    console.error(`更新模板(ID: ${id})失败:`, error);
    throw error;
  }
};

// 删除模板
export const deleteTemplate = async (id: number): Promise<boolean> => {
  try {
    await api.delete(`/template/delete/${id}`);
    return true;
  } catch (error) {
    console.error(`删除模板(ID: ${id})失败:`, error);
    throw error;
  }
};

// 由于后端尚未实现以下接口，我们暂时使用模拟数据
// 日记相关API - 使用模拟数据
// let mockDiaries: Diary[] = []; 

export const generateAiDiary = async (prompt: string, templateId: number = 0): Promise<Diary> => {
  try {
    // 使用ChatForm结构发送请求
    const chatForm: ChatForm = {
      message: prompt,
      templateId: templateId // 添加模板ID
    };

    // 调用后端 /diary/create 接口获取日记内容
    const response = await api.post<string>('/diary/create', chatForm, { timeout: 30000 }); // 增加超时时间到30秒
    const content = response.data as string || ''; // 后端直接返回字符串内容

    // 将后端返回的字符串内容包装成一个临时的 Diary 对象
    return {
      id: -1, // 临时ID，保存时后端会分配真实ID
      title: 'AI 生成日记', // 初始标题，AiDiaryView 会尝试从内容中提取更准确的标题
      content: content,
      createTime: new Date().toISOString(),
      tags: [], // 初始为空，AiDiaryView 会从内容中提取
      mood: '' // 初始为空，AiDiaryView 会从内容中提取
    };
  } catch (error) {
    console.error('AI生成日记失败:', error);
    throw error;
  }
};

// 保存日记到数据库
export const saveDiary = async (saveDiaryDto: SaveDiaryDto): Promise<boolean> => {
  try {
    // 直接调用后端 /diary/save 接口
    const response = await api.post<ApiResponse<Boolean>>('/diary/save', saveDiaryDto);
    // 根据后端返回的结构，判断保存是否成功
    return response.data.data === true;
  } catch (error) {
    console.error('保存日记失败:', error);
    throw error;
  }
}; 