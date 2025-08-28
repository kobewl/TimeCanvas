<template>
  <div class="knowledge-base">
    <!-- 顶部操作栏 -->
    <div class="header-actions">
      <div class="left-actions">
        <el-button type="primary" icon="Plus" @click="showCreateDialog = true">
          新建知识
        </el-button>
        <el-button icon="Upload" @click="showImportDialog = true">
          导入文件
        </el-button>
        <el-button icon="Download" @click="exportKnowledge">
          导出
        </el-button>
      </div>
      <div class="right-actions">
        <el-select v-model="viewMode" @change="handleViewModeChange">
          <el-option label="列表视图" value="list" />
          <el-option label="卡片视图" value="card" />
          <el-option label="知识图谱" value="graph" />
        </el-select>
      </div>
    </div>

    <!-- 搜索和筛选区域 -->
    <div class="search-section">
      <div class="search-main">
        <el-input
          v-model="searchForm.keyword"
          placeholder="搜索知识库..."
          class="search-input"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button @click="handleSearch">搜索</el-button>
          </template>
        </el-input>
        <el-switch
          v-model="searchForm.enableSemanticSearch"
          active-text="语义搜索"
          inactive-text="关键词搜索"
          @change="handleSearch"
        />
      </div>
      
      <!-- 智能问答区域 -->
      <div class="qa-section">
        <el-input
          v-model="qaQuestion"
          placeholder="有什么问题可以问我..."
          @keyup.enter="handleQA"
        >
          <template #prefix>
            <el-icon><ChatDotRound /></el-icon>
          </template>
          <template #append>
            <el-button type="primary" @click="handleQA">问答</el-button>
          </template>
        </el-input>
      </div>

      <!-- 筛选条件 -->
      <div class="filter-section">
        <el-row :gutter="16">
          <el-col :span="8">
            <el-select v-model="searchForm.categoryId" placeholder="选择分类" clearable>
              <el-option
                v-for="category in categories"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-select
              v-model="searchForm.tags"
              placeholder="选择标签"
              multiple
              clearable
            >
              <el-option
                v-for="tag in popularTags"
                :key="tag"
                :label="tag"
                :value="tag"
              />
            </el-select>
          </el-col>
          <el-col :span="8">
            <el-select v-model="searchForm.sourceType" placeholder="来源类型" clearable>
              <el-option label="手动创建" value="manual" />
              <el-option label="日记提取" value="diary_extract" />
              <el-option label="文件导入" value="import" />
            </el-select>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <el-row :gutter="20">
        <!-- 左侧分类树 -->
        <el-col :span="6">
          <div class="category-tree">
            <div class="tree-header">
              <h3>知识分类</h3>
              <el-button size="small" icon="Plus" @click="showCategoryDialog = true">
                新建分类
              </el-button>
            </div>
            <el-tree
              :data="categoryTree"
              :props="{ children: 'children', label: 'name' }"
              node-key="id"
              @node-click="handleCategoryClick"
              class="category-tree-content"
            />
          </div>
        </el-col>

        <!-- 右侧知识列表 -->
        <el-col :span="18">
          <!-- 列表视图 -->
          <div v-if="viewMode === 'list'" class="knowledge-list">
            <el-table :data="knowledgeList" style="width: 100%">
              <el-table-column prop="title" label="标题" min-width="200">
                <template #default="{ row }">
                  <el-link @click="viewKnowledge(row)" type="primary">
                    {{ row.title }}
                  </el-link>
                </template>
              </el-table-column>
              <el-table-column prop="summary" label="摘要" min-width="300" />
              <el-table-column prop="tagList" label="标签" width="200">
                <template #default="{ row }">
                  <el-tag
                    v-for="tag in row.tagList"
                    :key="tag"
                    size="small"
                    class="tag-item"
                  >
                    {{ tag }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="viewCount" label="查看次数" width="100" />
              <el-table-column prop="createdTime" label="创建时间" width="180" />
              <el-table-column label="操作" width="150">
                <template #default="{ row }">
                  <el-button size="small" @click="editKnowledge(row)">编辑</el-button>
                  <el-button size="small" type="danger" @click="deleteKnowledge(row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <!-- 分页 -->
            <el-pagination
              v-model:current-page="pagination.current"
              v-model:page-size="pagination.pageSize"
              :total="pagination.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              class="pagination"
            />
          </div>

          <!-- 卡片视图 -->
          <div v-if="viewMode === 'card'" class="knowledge-cards">
            <el-row :gutter="20">
              <el-col v-for="item in knowledgeList" :key="item.id" :span="8">
                <el-card class="knowledge-card" @click="viewKnowledge(item)">
                  <template #header>
                    <div class="card-header">
                      <span class="card-title">{{ item.title }}</span>
                      <el-dropdown @command="handleCardAction">
                        <el-icon><More /></el-icon>
                        <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item :command="{ action: 'edit', item }">
                              编辑
                            </el-dropdown-item>
                            <el-dropdown-item :command="{ action: 'delete', item }">
                              删除
                            </el-dropdown-item>
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                    </div>
                  </template>
                  
                  <div class="card-content">
                    <p class="summary">{{ item.summary || '暂无摘要' }}</p>
                    <div class="tags">
                      <el-tag
                        v-for="tag in item.tagList"
                        :key="tag"
                        size="small"
                        class="tag-item"
                      >
                        {{ tag }}
                      </el-tag>
                    </div>
                    <div class="meta">
                      <span>{{ item.viewCount }} 次查看</span>
                      <span>{{ formatDate(item.createdTime) }}</span>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>

          <!-- 知识图谱视图 -->
          <div v-if="viewMode === 'graph'" class="knowledge-graph">
            <div class="graph-container" ref="graphContainer">
              <!-- 这里集成知识图谱可视化组件，如 D3.js 或 ECharts -->
              <div class="graph-placeholder">
                知识图谱视图（待实现）
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 创建/编辑知识对话框 -->
    <KnowledgeEditor
      v-model:visible="showCreateDialog"
      :knowledge="editingKnowledge"
      :categories="categories"
      @success="handleEditorSuccess"
    />

    <!-- 分类管理对话框 -->
    <CategoryManager
      v-model:visible="showCategoryDialog"
      :categories="categories"
      @success="loadCategories"
    />

    <!-- 导入对话框 -->
    <ImportDialog
      v-model:visible="showImportDialog"
      @success="loadKnowledgeList"
    />

    <!-- 问答结果对话框 -->
    <el-dialog v-model="showQADialog" title="智能问答" width="600px">
      <div class="qa-result">
        <div class="question">
          <strong>问题：</strong>{{ qaQuestion }}
        </div>
        <div class="answer">
          <strong>答案：</strong>
          <div v-html="qaAnswer" class="answer-content"></div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  listKnowledgeEntries,
  searchKnowledgeEntries,
  deleteKnowledgeEntry,
  getPopularTags,
  intelligentQA
} from '@/api/knowledge'
import KnowledgeEditor from '@/components/KnowledgeEditor.vue'
import CategoryManager from '@/components/CategoryManager.vue'
import ImportDialog from '@/components/ImportDialog.vue'

// 响应式数据
const viewMode = ref('list')
const knowledgeList = ref([])
const categories = ref([])
const categoryTree = ref([])
const popularTags = ref([])

const searchForm = reactive({
  keyword: '',
  categoryId: null,
  tags: [],
  sourceType: '',
  enableSemanticSearch: false,
  semanticQuery: ''
})

const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

// 对话框状态
const showCreateDialog = ref(false)
const showCategoryDialog = ref(false)
const showImportDialog = ref(false)
const showQADialog = ref(false)

// 编辑相关
const editingKnowledge = ref(null)

// 问答相关
const qaQuestion = ref('')
const qaAnswer = ref('')

// 页面加载
onMounted(() => {
  loadKnowledgeList()
  loadCategories()
  loadPopularTags()
})

// 加载知识列表
const loadKnowledgeList = async () => {
  try {
    const params = {
      current: pagination.current,
      pageSize: pagination.pageSize,
      ...searchForm
    }
    const response = await listKnowledgeEntries(params)
    knowledgeList.value = response.data.records
    pagination.total = response.data.total
  } catch (error) {
    ElMessage.error('加载知识列表失败')
  }
}

// 加载分类
const loadCategories = async () => {
  // TODO: 实现分类加载逻辑
  categories.value = []
  categoryTree.value = []
}

// 加载热门标签
const loadPopularTags = async () => {
  try {
    const response = await getPopularTags(20)
    popularTags.value = response.data
  } catch (error) {
    console.error('加载热门标签失败', error)
  }
}

// 搜索
const handleSearch = async () => {
  try {
    const response = await searchKnowledgeEntries(searchForm)
    knowledgeList.value = response.data
  } catch (error) {
    ElMessage.error('搜索失败')
  }
}

// 智能问答
const handleQA = async () => {
  if (!qaQuestion.value.trim()) {
    ElMessage.warning('请输入问题')
    return
  }
  
  try {
    const response = await intelligentQA(qaQuestion.value)
    qaAnswer.value = response.data
    showQADialog.value = true
  } catch (error) {
    ElMessage.error('问答失败')
  }
}

// 查看知识详情
const viewKnowledge = (knowledge: any) => {
  // TODO: 跳转到知识详情页面
  console.log('查看知识:', knowledge)
}

// 编辑知识
const editKnowledge = (knowledge: any) => {
  editingKnowledge.value = knowledge
  showCreateDialog.value = true
}

// 删除知识
const deleteKnowledge = async (knowledge: any) => {
  try {
    await ElMessageBox.confirm('确定要删除这条知识吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteKnowledgeEntry(knowledge.id)
    ElMessage.success('删除成功')
    loadKnowledgeList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 其他事件处理器
const handleViewModeChange = () => {
  // 视图模式切换逻辑
}

const handleCategoryClick = (category: any) => {
  searchForm.categoryId = category.id
  loadKnowledgeList()
}

const handleCardAction = ({ action, item }: any) => {
  if (action === 'edit') {
    editKnowledge(item)
  } else if (action === 'delete') {
    deleteKnowledge(item)
  }
}

const handleEditorSuccess = () => {
  showCreateDialog.value = false
  editingKnowledge.value = null
  loadKnowledgeList()
}

const handleSizeChange = () => {
  loadKnowledgeList()
}

const handleCurrentChange = () => {
  loadKnowledgeList()
}

const exportKnowledge = () => {
  // TODO: 实现导出功能
  ElMessage.info('导出功能待实现')
}

const formatDate = (date: string) => {
  return new Date(date).toLocaleDateString()
}
</script>

<style scoped lang="scss">
.knowledge-base {
  padding: 20px;
  height: 100vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;

  .header-actions {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;

    .left-actions {
      .el-button {
        margin-right: 10px;
      }
    }
  }

  .search-section {
    margin-bottom: 20px;
    padding: 20px;
    background: #f5f5f5;
    border-radius: 8px;

    .search-main {
      display: flex;
      align-items: center;
      gap: 16px;
      margin-bottom: 16px;

      .search-input {
        flex: 1;
      }
    }

    .qa-section {
      margin-bottom: 16px;
    }

    .filter-section {
      .el-select {
        width: 100%;
      }
    }
  }

  .content-area {
    flex: 1;
    overflow: hidden;

    .category-tree {
      height: 100%;
      border: 1px solid #e4e7ed;
      border-radius: 8px;
      padding: 16px;

      .tree-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16px;

        h3 {
          margin: 0;
        }
      }

      .category-tree-content {
        max-height: 600px;
        overflow-y: auto;
      }
    }

    .knowledge-list {
      .pagination {
        margin-top: 20px;
        text-align: center;
      }

      .tag-item {
        margin-right: 8px;
        margin-bottom: 4px;
      }
    }

    .knowledge-cards {
      .knowledge-card {
        margin-bottom: 16px;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
          transform: translateY(-2px);
        }

        .card-header {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .card-title {
            font-weight: bold;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }

        .card-content {
          .summary {
            color: #666;
            margin-bottom: 12px;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
          }

          .tags {
            margin-bottom: 12px;

            .tag-item {
              margin-right: 8px;
              margin-bottom: 4px;
            }
          }

          .meta {
            display: flex;
            justify-content: space-between;
            color: #999;
            font-size: 12px;
          }
        }
      }
    }

    .knowledge-graph {
      .graph-container {
        height: 600px;
        border: 1px solid #e4e7ed;
        border-radius: 8px;
        background: #fff;

        .graph-placeholder {
          display: flex;
          align-items: center;
          justify-content: center;
          height: 100%;
          color: #999;
          font-size: 18px;
        }
      }
    }
  }

  .qa-result {
    .question {
      margin-bottom: 16px;
      padding: 12px;
      background: #f0f9ff;
      border-radius: 8px;
    }

    .answer {
      .answer-content {
        margin-top: 8px;
        padding: 12px;
        background: #f9f9f9;
        border-radius: 8px;
        line-height: 1.6;
      }
    }
  }
}
</style>