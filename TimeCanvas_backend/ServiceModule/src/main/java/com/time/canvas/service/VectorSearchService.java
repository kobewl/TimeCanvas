package com.time.canvas.service;

import java.util.List;
import java.util.Map;

/**
 * 向量搜索服务接口
 * 
 * @author wangliang
 */
public interface VectorSearchService {

    /**
     * 添加文档到向量数据库
     * 
     * @param id 文档ID
     * @param text 文档内容
     * @param metadata 元数据
     * @return 是否成功
     */
    boolean addDocument(String id, String text, Map<String, Object> metadata);

    /**
     * 批量添加文档
     * 
     * @param documents 文档列表
     * @return 是否成功
     */
    boolean addDocuments(List<DocumentVector> documents);

    /**
     * 删除文档
     * 
     * @param id 文档ID
     * @return 是否成功
     */
    boolean deleteDocument(String id);

    /**
     * 更新文档
     * 
     * @param id 文档ID
     * @param text 新的文档内容
     * @param metadata 新的元数据
     * @return 是否成功
     */
    boolean updateDocument(String id, String text, Map<String, Object> metadata);

    /**
     * 语义搜索
     * 
     * @param query 查询文本
     * @param topK 返回前K个结果
     * @param threshold 相似度阈值
     * @return 搜索结果
     */
    List<SearchResult> semanticSearch(String query, int topK, float threshold);

    /**
     * 根据文档ID搜索相似文档
     * 
     * @param documentId 文档ID
     * @param topK 返回前K个结果
     * @param threshold 相似度阈值
     * @return 搜索结果
     */
    List<SearchResult> findSimilarDocuments(String documentId, int topK, float threshold);

    /**
     * 文档向量化
     * 
     * @param text 文本内容
     * @return 向量
     */
    float[] embedding(String text);

    /**
     * 文档向量
     */
    class DocumentVector {
        private String id;
        private String text;
        private float[] vector;
        private Map<String, Object> metadata;

        // 构造函数、getter、setter
        public DocumentVector() {}

        public DocumentVector(String id, String text, Map<String, Object> metadata) {
            this.id = id;
            this.text = text;
            this.metadata = metadata;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        
        public float[] getVector() { return vector; }
        public void setVector(float[] vector) { this.vector = vector; }
        
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    /**
     * 搜索结果
     */
    class SearchResult {
        private String id;
        private String text;
        private float score;
        private Map<String, Object> metadata;

        // 构造函数、getter、setter
        public SearchResult() {}

        public SearchResult(String id, String text, float score, Map<String, Object> metadata) {
            this.id = id;
            this.text = text;
            this.score = score;
            this.metadata = metadata;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        
        public float getScore() { return score; }
        public void setScore(float score) { this.score = score; }
        
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }
}