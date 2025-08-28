package com.time.canvas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.time.canvas.domain.KnowledgeCategory;
import com.time.canvas.dto.knowledge.KnowledgeCategoryAddRequest;
import com.time.canvas.dto.knowledge.KnowledgeCategoryUpdateRequest;
import com.time.canvas.vo.knowledge.KnowledgeCategoryVO;

import java.util.List;

/**
 * 知识分类服务接口
 * 
 * @author wangliang
 */
public interface KnowledgeCategoryService extends IService<KnowledgeCategory> {

    /**
     * 创建知识分类
     * 
     * @param addRequest 创建请求
     * @param userId 用户ID
     * @return 分类ID
     */
    Long createCategory(KnowledgeCategoryAddRequest addRequest, Long userId);

    /**
     * 更新知识分类
     * 
     * @param id 分类ID
     * @param updateRequest 更新请求
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean updateCategory(Long id, KnowledgeCategoryUpdateRequest updateRequest, Long userId);

    /**
     * 删除知识分类
     * 
     * @param id 分类ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean deleteCategory(Long id, Long userId);

    /**
     * 获取用户的分类树
     * 
     * @param userId 用户ID
     * @return 分类树
     */
    List<KnowledgeCategoryVO> getCategoryTree(Long userId);

    /**
     * 获取分类详情
     * 
     * @param id 分类ID
     * @param userId 用户ID
     * @return 分类VO
     */
    KnowledgeCategoryVO getCategoryVO(Long id, Long userId);

    /**
     * 获取所有子分类ID（包括自身）
     * 
     * @param categoryId 分类ID
     * @param userId 用户ID
     * @return 子分类ID列表
     */
    List<Long> getChildCategoryIds(Long categoryId, Long userId);
}