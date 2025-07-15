import request from './axios';

// 生成AI日记
export function generateAiDiary(data: { message: string; templateId: number }) {
  return request.post<string>('/diary/create', data);
} 