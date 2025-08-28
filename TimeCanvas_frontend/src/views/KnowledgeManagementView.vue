<template>
  <div class="knowledge-management">
    <div class="layout-container">
      <!-- 左侧分类树 -->
      <aside class="sidebar">
        <div class="sidebar-header">
          <h3>知识分类</h3>
          <el-button type="primary" size="small" @click="showAddCategoryDialog">
            <el-icon><Plus /></el-icon>
            新建分类
          </el-button>
        </div>
        
        <div class="category-tree">
          <el-tree
            :data="categoryTree"
            :props="treeProps"
            node-key="id"
            :expand-on-click-node="false"
            :current-node-key="selectedCategoryId"
            @node-click="handleCategoryClick"
            class="custom-tree"
          >
            <template #default="{ node, data }">
              <span class="custom-tree-node">
                <span>{{ node.label }}</span>
                <span class="tree-actions">
                  <el-button
                    type="text"
                    size="small"
                    @click.stop="editCategory(data)"
                  >
                    <el-icon><Edit /></el-icon>
                  </el-button>
                  <el-button
                    type="text"
                    size="small"
                    @click.stop="deleteCategory(data)"
                  >
                    <el-icon><Delete /></el-icon>
                  </el-button>
                </span>
              </span>
            </template>
          </el-tree>
        </div>
      </aside>

      <!-- 主内容区 -->
      <main class="main-content">
        <!-- 工具栏 -->
        <div class="toolbar">
          <div class="toolbar-left">
            <el-input
              v-model="searchQuery"
              placeholder="搜索知识文档..."
              style="width: 300px"
              @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            <el-button @click="handleSearch">搜索</el-button>
          </div>
          
          <div class="toolbar-right">
            <el-button type="primary" @click="showAddDocDialog">
              <el-icon><DocumentAdd /></el-icon>
              新建文档
            </el-button>
            <el-button @click="importDocument">
              <el-icon><Upload /></el-icon>
              导入
            </el-button>
            <el-button @click="exportDocuments">
              <el-icon><Download /></el-icon>
              导出
            </el-button>
          </div>
        </div>

        <!-- 文档列表 -->
        <div class="document-list">
          <el-table
            :data="documentList"
            v-loading="loading"
            @selection-change="handleSelectionChange"
            class="custom-table"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="title" label="标题" min-width="200">
              <template #default="{ row }">
                <div class="doc-title" @click="openDocument(row)">
                  <el-icon class="doc-icon"><Document /></el-icon>
                  {{ row.title }}
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="categoryName" label="分类" width="120" />
            <el-table-column prop="tags" label="标签" width="200">
              <template #default="{ row }">
                <el-tag
                  v-for="tag in row.tags"
                  :key="tag"
                  size="small"
                  style="margin-right: 5px"
                >
                  {{ tag }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="updatedTime" label="更新时间" width="150" />
            <el-table-column label="操作" width="200" fixed="right">
              <template #default="{ row }">
                <el-button type="text" @click="editDocument(row)">编辑</el-button>
                <el-button type="text" @click="viewDocument(row)">查看</el-button>
                <el-button type="text" @click="deleteDocument(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            class="pagination"
          />
        </div>
      </main>
    </div>

    <!-- 文档编辑弹窗 -->
    <el-dialog
      v-model="documentDialogVisible"
      :title="isEdit ? '编辑文档' : '新建文档'"
      width="80%"
      class="document-dialog"
    >
      <el-form :model="documentForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="documentForm.title" placeholder="请输入文档标题" />
        </el-form-item>
        <el-form-item label="分类">
          <el-cascader
            v-model="documentForm.categoryId"
            :options="categoryTree"
            :props="cascaderProps"
            placeholder="请选择分类"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="tagsInput" placeholder="请输入标签，用逗号分隔" />
        </el-form-item>
        <el-form-item label="内容">
          <div class="editor-container">
            <div id="editor" style="height: 400px;"></div>
          </div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="documentDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveDocument">保存</el-button>
          <el-button @click="generateSummary">AI生成摘要</el-button>
          <el-button @click="extractTags">AI提取标签</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 分类管理弹窗 -->
    <el-dialog
      v-model="categoryDialogVisible"
      :title="isCategoryEdit ? '编辑分类' : '新建分类'"
      width="500px"
    >
      <el-form :model="categoryForm" label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="父分类">
          <el-cascader
            v-model="categoryForm.parentId"
            :options="categoryTree"
            :props="cascaderProps"
            placeholder="请选择父分类（可选）"
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="categoryForm.description"
            type="textarea"
            placeholder="请输入分类描述"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="categoryDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveCategory">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import {
  Plus, Edit, Delete, Search, DocumentAdd, Upload, Download, Document
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { knowledgeApi, knowledgeCategoryApi } from '@/api/knowledge'

// 响应式数据
const loading = ref(false)
const categoryTree = ref([])
const selectedCategoryId = ref(null)
const searchQuery = ref('')
const documentList = ref([])
const selectedDocuments = ref<any[]>([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

// 弹窗控制
const documentDialogVisible = ref(false)
const categoryDialogVisible = ref(false)
const isEdit = ref(false)
const isCategoryEdit = ref(false)

// 表单数据
const documentForm = ref({
  id: null,
  title: '',
  content: '',
  categoryId: null,
  tags: [] as string[]
})

const categoryForm = ref({
  id: null,
  name: '',
  description: '',
  parentId: null
})

const tagsInput = ref('')

// 配置
const treeProps = {
  children: 'children',
  label: 'name'
}

const cascaderProps = {
  value: 'id',
  label: 'name',
  children: 'children'
}

// 生命周期
onMounted(() => {
  loadCategoryTree()
  loadDocumentList()
})

// 方法
const loadCategoryTree = async () => {
  try {
    const response = await knowledgeCategoryApi.getCategoryTree()
    if (response.data?.code === 0) {
      categoryTree.value = response.data.data
    }
  } catch (error) {
    console.error('加载分类树失败:', error)
  }
}

const loadDocumentList = async () => {
  loading.value = true
  try {
    const params = {
      current: currentPage.value,
      pageSize: pageSize.value,
      categoryId: selectedCategoryId.value,
      keyword: searchQuery.value
    }
    const response = await knowledgeApi.page(params)
    if (response.data?.code === 0) {
      documentList.value = response.data.data.records
      total.value = response.data.data.total
    }
  } catch (error) {
    console.error('加载文档列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 其他方法
const handleCategoryClick = (data: any) => {
  selectedCategoryId.value = data.id
  currentPage.value = 1
  loadDocumentList()
}

const handleSelectionChange = (selection: any[]) => {
  selectedDocuments.value = selection
}

const handleSizeChange = (newSize: number) => {
  pageSize.value = newSize
  currentPage.value = 1
  loadDocumentList()
}

const handleCurrentChange = (newPage: number) => {
  currentPage.value = newPage
  loadDocumentList()
}

const handleSearch = () => {
  currentPage.value = 1
  loadDocumentList()
}

const showAddDocDialog = () => {
  isEdit.value = false
  documentForm.value = {
    id: null,
    title: '',
    content: '',
    categoryId: null,
    tags: []
  }
  tagsInput.value = ''
  documentDialogVisible.value = true
}

const showAddCategoryDialog = () => {
  isCategoryEdit.value = false
  categoryForm.value = {
    id: null,
    name: '',
    description: '',
    parentId: null
  }
  categoryDialogVisible.value = true
}

const editDocument = (row: any) => {
  isEdit.value = true
  documentForm.value = { ...row }
  tagsInput.value = row.tags ? row.tags.join(', ') : ''
  documentDialogVisible.value = true
}

const viewDocument = (row: any) => {
  // 跳转到文档详情页
  console.log('查看文档:', row)
}

const openDocument = (row: any) => {
  // 在新标签页打开文档
  console.log('打开文档:', row)
}

const deleteDocument = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除这个文档吗？', '确认删除', {
      type: 'warning'
    })
    
    const response = await knowledgeApi.delete(row.id)
    if (response.data?.code === 0) {
      ElMessage.success('删除成功')
      loadDocumentList()
    } else {
      ElMessage.error(response.data?.message || '删除失败')
    }
  } catch (error) {
    console.error('删除文档失败:', error)
  }
}

const editCategory = (data: any) => {
  isCategoryEdit.value = true
  categoryForm.value = { ...data }
  categoryDialogVisible.value = true
}

const deleteCategory = async (data: any) => {
  try {
    await ElMessageBox.confirm('确定要删除这个分类吗？', '确认删除', {
      type: 'warning'
    })
    
    const response = await knowledgeCategoryApi.deleteCategory(data.id)
    if (response.data?.code === 0) {
      ElMessage.success('删除成功')
      loadCategoryTree()
      if (selectedCategoryId.value === data.id) {
        selectedCategoryId.value = null
        loadDocumentList()
      }
    } else {
      ElMessage.error(response.data?.message || '删除失败')
    }
  } catch (error) {
    console.error('删除分类失败:', error)
  }
}

const saveDocument = async () => {
  try {
    // 处理标签
    if (tagsInput.value) {
      documentForm.value.tags = tagsInput.value.split(',').map(tag => tag.trim()).filter(tag => tag)
    }
    
    let response
    if (isEdit.value) {
      response = await knowledgeApi.update(documentForm.value)
    } else {
      response = await knowledgeApi.create(documentForm.value)
    }
    
    if (response.data?.code === 0) {
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
      documentDialogVisible.value = false
      loadDocumentList()
    } else {
      ElMessage.error(response.data?.message || '保存失败')
    }
  } catch (error) {
    console.error('保存文档失败:', error)
    ElMessage.error('保存失败')
  }
}

const saveCategory = async () => {
  try {
    let response
    if (isCategoryEdit.value) {
      response = await knowledgeCategoryApi.updateCategory(categoryForm.value)
    } else {
      response = await knowledgeCategoryApi.createCategory(categoryForm.value)
    }
    
    if (response.data?.code === 0) {
      ElMessage.success(isCategoryEdit.value ? '更新成功' : '创建成功')
      categoryDialogVisible.value = false
      loadCategoryTree()
    } else {
      ElMessage.error(response.data?.message || '保存失败')
    }
  } catch (error) {
    console.error('保存分类失败:', error)
    ElMessage.error('保存失败')
  }
}

const generateSummary = async () => {
  try {
    if (!documentForm.value.content) {
      ElMessage.warning('请先输入文档内容')
      return
    }
    
    const response = await knowledgeApi.generateSummary({
      content: documentForm.value.content
    })
    
    if (response.data?.code === 0) {
      ElMessage.success('摘要生成成功')
      // 这里可以将摘要添加到文档描述或其他字段
      console.log('生成的摘要:', response.data.data)
    } else {
      ElMessage.error(response.data?.message || '摘要生成失败')
    }
  } catch (error) {
    console.error('生成摘要失败:', error)
    ElMessage.error('摘要生成失败')
  }
}

const extractTags = async () => {
  try {
    if (!documentForm.value.content) {
      ElMessage.warning('请先输入文档内容')
      return
    }
    
    const response = await knowledgeApi.extractTags({
      content: documentForm.value.content
    })
    
    if (response.data?.code === 0) {
      ElMessage.success('标签提取成功')
      tagsInput.value = response.data.data.join(', ')
    } else {
      ElMessage.error(response.data?.message || '标签提取失败')
    }
  } catch (error) {
    console.error('提取标签失败:', error)
    ElMessage.error('标签提取失败')
  }
}

const importDocument = () => {
  // 实现文档导入功能
  ElMessage.info('导入功能开发中...')
}

const exportDocuments = () => {
  if (selectedDocuments.value.length === 0) {
    ElMessage.warning('请先选择要导出的文档')
    return
  }
  
  // 实现文档导出功能
  ElMessage.info('导出功能开发中...')
}
</script>

<style scoped>
.knowledge-management {
  height: 100vh;
  background: #0f0f0f;
  color: #e4e4e4;
  display: flex;
  flex-direction: column;
}

.layout-container {
  flex: 1;
  display: flex;
  height: 100%;
}

.sidebar {
  width: 280px;
  background: #1a1a1a;
  border-right: 1px solid #333;
  padding: 20px;
  overflow-y: auto;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px;
  background: #1a1a1a;
  border-radius: 8px;
}

.toolbar-left,
.toolbar-right {
  display: flex;
  gap: 10px;
  align-items: center;
}

.document-list {
  background: #1a1a1a;
  border-radius: 8px;
  padding: 20px;
}

.doc-title {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #6366f1;
}

.doc-icon {
  margin-right: 8px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

:deep(.el-table) {
  background: transparent;
  color: #e4e4e4;
}

:deep(.el-table th.el-table__cell) {
  background: #262626;
  border-color: #333;
  color: #e4e4e4;
}

:deep(.el-table td.el-table__cell) {
  border-color: #333;
}

:deep(.el-tree) {
  background: transparent;
  color: #e4e4e4;
}

:deep(.el-tree-node__content) {
  background: transparent;
  color: #e4e4e4;
}

:deep(.el-tree-node__content:hover) {
  background: #333;
}
</style>