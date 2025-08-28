import axios from './axios'

// 知识库相关API接口

/**
 * 创建知识条目
 */
export function createKnowledgeEntry(data: {
  title: string
  content: string
  categoryId?: number
  tags?: string[]
  sourceType?: string
  sourceId?: number
}) {
  return axios.post('/api/knowledge/create', data)
}

/**
 * 更新知识条目
 */
export function updateKnowledgeEntry(id: number, data: {
  title: string
  content: string
  categoryId?: number
  tags?: string[]
  sourceType?: string
  sourceId?: number
}) {
  return axios.post(`/api/knowledge/update/${id}`, data)
}

/**
 * 删除知识条目
 */
export function deleteKnowledgeEntry(id: number) {
  return axios.post(`/api/knowledge/delete/${id}`)
}

/**
 * 获取知识条目详情
 */
export function getKnowledgeEntry(id: number) {
  return axios.get(`/api/knowledge/get/${id}`)
}

/**
 * 分页查询知识条目
 */
export function listKnowledgeEntries(data: {
  current?: number
  pageSize?: number
  keyword?: string
  categoryId?: number
  tags?: string[]
  sourceType?: string
  enableSemanticSearch?: boolean
  semanticQuery?: string
}) {
  return axios.post('/api/knowledge/list', data)
}

/**
 * 搜索知识条目
 */
export function searchKnowledgeEntries(data: {
  keyword?: string
  categoryId?: number
  tags?: string[]
  enableSemanticSearch?: boolean
  semanticQuery?: string
}) {
  return axios.post('/api/knowledge/search', data)
}

/**
 * 获取相关知识条目
 */
export function getRelatedEntries(id: number, limit: number = 5) {
  return axios.get(`/api/knowledge/related/${id}?limit=${limit}`)
}

/**
 * 从日记提取知识
 */
export function extractFromDiary(diaryId: number) {
  return axios.post(`/api/knowledge/extract-from-diary/${diaryId}`)
}

/**
 * 智能问答
 */
export function intelligentQA(question: string) {
  return axios.post('/api/knowledge/qa', null, {
    params: { question }
  })
}

/**
 * 获取热门标签
 */
export function getPopularTags(limit: number = 20) {
  return axios.get(`/api/knowledge/tags/popular?limit=${limit}`)
}

/**
 * 生成摘要
 */
export function generateSummary(content: string) {
  return axios.post('/api/knowledge/generate-summary', null, {
    params: { content }
  })
}

/**
 * 批量导入知识条目
 */
export function batchImport(entries: Array<{
  title: string
  content: string
  categoryId?: number
  tags?: string[]
  sourceType?: string
  sourceId?: number
}>) {
  return axios.post('/api/knowledge/batch-import', entries)
}

/**
 * 文件上传导入
 */
export function uploadFile(file: File) {
  const formData = new FormData()
  formData.append('file', file)
  return axios.post('/api/knowledge/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}