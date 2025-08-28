package com.time.canvas.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.time.canvas.domain.KnowledgeEntry;
import com.time.canvas.dto.knowledge.KnowledgeEntryAddRequest;
import com.time.canvas.dto.knowledge.KnowledgeEntryQueryRequest;
import com.time.canvas.vo.knowledge.KnowledgeEntryVO;

import java.util.List;

/**
 * 知识库服务接口
 * 
 * @author wangliang
 */
public interface KnowledgeService extends IService<KnowledgeEntry> {

    /**
     * 创建知识条目
     * 
     * @param addRequest 创建请求
     * @param userId 用户ID
     * @return 知识条目ID
     */
    Long createKnowledgeEntry(KnowledgeEntryAddRequest addRequest, Long userId);

    /**
     * 更新知识条目
     * 
     * @param id 知识条目ID
     * @param addRequest 更新请求
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean updateKnowledgeEntry(Long id, KnowledgeEntryAddRequest addRequest, Long userId);

    /**
     * 删除知识条目
     * 
     * @param id 知识条目ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteKnowledgeEntry(Long id, Long userId);

    /**
     * 获取知识条目详情
     * 
     * @param id 知识条目ID
     * @param userId 用户ID
     * @return 知识条目VO
     */
    KnowledgeEntryVO getKnowledgeEntryVO(Long id, Long userId);

    /**
     * 分页查询知识条目
     * 
     * @param queryRequest 查询请求
     * @return 分页结果
     */
    IPage<KnowledgeEntryVO> listKnowledgeEntryVO(KnowledgeEntryQueryRequest queryRequest);

    /**
     * 搜索知识条目（关键词 + 语义搜索）
     * 
     * @param queryRequest 查询请求
     * @return 搜索结果
     */
    List<KnowledgeEntryVO> searchKnowledgeEntries(KnowledgeEntryQueryRequest queryRequest);

    /**
     * 获取相关知识条目
     * 
     * @param entryId 知识条目ID
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 相关知识条目列表
     */
    List<KnowledgeEntryVO> getRelatedEntries(Long entryId, Long userId, Integer limit);

    /**
     * 从日记中提取知识条目
     * 
     * @param diaryId 日记ID
     * @param userId 用户ID
     * @return 提取的知识条目列表
     */
    List<KnowledgeEntryVO> extractFromDiary(Long diaryId, Long userId);

    /**
     * 智能问答
     * 
     * @param question 问题
     * @param userId 用户ID
     * @return 答案
     */
    String intelligentQA(String question, Long userId);

    /**
     * 获取热门标签
     * 
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 标签列表
     */
    List<String> getPopularTags(Long userId, Integer limit);

    /**
     * 生成知识摘要
     * 
     * @param content 知识内容
     * @return AI生成的摘要
     */
    String generateSummary(String content);

    /**
     * 批量导入知识条目
     * 
     * @param entries 知识条目列表
     * @param userId 用户ID
     * @return 导入成功数量
     */
    Integer batchImport(List<KnowledgeEntryAddRequest> entries, Long userId);
}