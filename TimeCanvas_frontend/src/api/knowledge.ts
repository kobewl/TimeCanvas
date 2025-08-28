import request from './axios'

// 知识分类相关API
export const knowledgeCategoryApi = {
  // 创建知识分类
  createCategory: (data: any) => {
    return request({
      url: '/api/knowledge/category/create',
      method: 'post',
      data
    })
  },

  // 更新知识分类
  updateCategory: (data: any) => {
    return request({
      url: '/api/knowledge/category/update',
      method: 'post',
      data
    })
  },

  // 删除知识分类
  deleteCategory: (id: number) => {
    return request({
      url: `/api/knowledge/category/delete/${id}`,
      method: 'post'
    })
  },

  // 获取分类详情
  getCategory: (id: number) => {
    return request({
      url: `/api/knowledge/category/get/${id}`,
      method: 'get'
    })
  },

  // 获取分类树
  getCategoryTree: () => {
    return request({
      url: '/api/knowledge/category/tree',
      method: 'get'
    })
  },

  // 获取子分类ID
  getChildCategoryIds: (id: number) => {
    return request({
      url: `/api/knowledge/category/children/${id}`,
      method: 'get'
    })
  }
}

// 知识条目相关API
export const knowledgeApi = {
  // 创建知识条目
  create: (data: any) => {
    return request({
      url: '/api/knowledge/create',
      method: 'post',
      data
    })
  },

  // 更新知识条目
  update: (data: any) => {
    return request({
      url: '/api/knowledge/update',
      method: 'post',
      data
    })
  },

  // 删除知识条目
  delete: (id: number) => {
    return request({
      url: `/api/knowledge/delete/${id}`,
      method: 'post'
    })
  },

  // 获取知识条目详情
  get: (id: number) => {
    return request({
      url: `/api/knowledge/get/${id}`,
      method: 'get'
    })
  },

  // 分页查询知识条目
  page: (params: any) => {
    return request({
      url: '/api/knowledge/page',
      method: 'get',
      params
    })
  },

  // 搜索知识条目
  search: (data: any) => {
    return request({
      url: '/api/knowledge/search',
      method: 'post',
      data
    })
  },

  // 智能问答
  ask: (data: any) => {
    return request({
      url: '/api/knowledge/ask',
      method: 'post',
      data
    })
  },

  // 生成摘要
  generateSummary: (data: any) => {
    return request({
      url: '/api/knowledge/generate-summary',
      method: 'post',
      data
    })
  },

  // 提取标签
  extractTags: (data: any) => {
    return request({
      url: '/api/knowledge/extract-tags',
      method: 'post',
      data
    })
  },

  // 导入文档
  importDocument: (data: any) => {
    return request({
      url: '/api/knowledge/import',
      method: 'post',
      data,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 导出文档
  export: (ids: number[]) => {
    return request({
      url: '/api/knowledge/export',
      method: 'post',
      data: { ids },
      responseType: 'blob'
    })
  },

  // 批量操作
  batchOperation: (data: any) => {
    return request({
      url: '/api/knowledge/batch',
      method: 'post',
      data
    })
  }
}