package com.time.canvas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.time.canvas.domain.KnowledgeCategory;
import com.time.canvas.dto.knowledge.KnowledgeCategoryAddRequest;
import com.time.canvas.dto.knowledge.KnowledgeCategoryUpdateRequest;
import com.time.canvas.exception.BusinessException;
import com.time.canvas.mapper.KnowledgeCategoryMapper;
import com.time.canvas.service.KnowledgeCategoryService;
import com.time.canvas.util.ErrorCode;
import com.time.canvas.vo.knowledge.KnowledgeCategoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 知识分类服务实现类
 * 
 * @author wangliang
 */
@Service
@Slf4j
public class KnowledgeCategoryServiceImpl extends ServiceImpl<KnowledgeCategoryMapper, KnowledgeCategory> 
        implements KnowledgeCategoryService {

    @Autowired
    private KnowledgeCategoryMapper knowledgeCategoryMapper;

    @Override
    @Transactional
    public Long createCategory(KnowledgeCategoryAddRequest addRequest, Long userId) {
        // 参数校验
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID无效");
        }

        // 检查分类名称是否重复
        Integer existCount = knowledgeCategoryMapper.checkNameExists(
            addRequest.getName(), 
            addRequest.getParentId(), 
            userId, 
            null
        );
        if (existCount > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类名称已存在");
        }

        // 如果有父分类，检查父分类是否存在且属于当前用户
        if (addRequest.getParentId() != null) {
            KnowledgeCategory parentCategory = this.getById(addRequest.getParentId());
            if (parentCategory == null || !parentCategory.getUserId().equals(userId)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "父分类不存在或无权限");
            }
        }

        KnowledgeCategory category = new KnowledgeCategory();
        BeanUtils.copyProperties(addRequest, category);
        category.setUserId(userId);
        category.setCreatedTime(LocalDateTime.now());
        
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }

        boolean result = this.save(category);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建知识分类失败");
        }

        return category.getId();
    }

    @Override
    @Transactional
    public boolean updateCategory(Long id, KnowledgeCategoryUpdateRequest updateRequest, Long userId) {
        // 参数校验
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类ID无效");
        }
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        }

        KnowledgeCategory existingCategory = this.getById(id);
        if (existingCategory == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "知识分类不存在");
        }
        
        // 权限校验
        if (!existingCategory.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限修改该知识分类");
        }

        // 检查分类名称是否重复（排除自己）
        Integer existCount = knowledgeCategoryMapper.checkNameExists(
            updateRequest.getName(), 
            updateRequest.getParentId(), 
            userId, 
            id
        );
        if (existCount > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类名称已存在");
        }

        // 如果修改了父分类，检查是否会造成循环引用
        if (updateRequest.getParentId() != null && !updateRequest.getParentId().equals(existingCategory.getParentId())) {
            List<Long> childIds = getChildCategoryIds(id, userId);
            if (childIds.contains(updateRequest.getParentId())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能设置子分类为父分类，会造成循环引用");
            }

            // 检查新父分类是否存在且属于当前用户
            KnowledgeCategory parentCategory = this.getById(updateRequest.getParentId());
            if (parentCategory == null || !parentCategory.getUserId().equals(userId)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "父分类不存在或无权限");
            }
        }

        // 更新字段
        existingCategory.setName(updateRequest.getName());
        existingCategory.setDescription(updateRequest.getDescription());
        existingCategory.setParentId(updateRequest.getParentId());
        existingCategory.setSortOrder(updateRequest.getSortOrder());

        return this.updateById(existingCategory);
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long id, Long userId) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类ID无效");
        }

        KnowledgeCategory existingCategory = this.getById(id);
        if (existingCategory == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "知识分类不存在");
        }

        // 权限校验
        if (!existingCategory.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除该知识分类");
        }

        // 检查是否有子分类
        List<KnowledgeCategory> children = knowledgeCategoryMapper.selectByParentId(id, userId);
        if (!children.isEmpty()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该分类下还有子分类，请先删除子分类");
        }

        // 检查是否有知识条目
        Integer entryCount = knowledgeCategoryMapper.countEntriesByCategoryId(id);
        if (entryCount > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该分类下还有知识条目，请先移动或删除这些条目");
        }

        return this.removeById(id);
    }

    @Override
    public List<KnowledgeCategoryVO> getCategoryTree(Long userId) {
        if (userId == null) {
            return new ArrayList<>();
        }

        // 获取用户的所有分类
        List<KnowledgeCategory> allCategories = knowledgeCategoryMapper.selectByUserId(userId);
        
        // 转换为VO并构建树结构
        Map<Long, KnowledgeCategoryVO> categoryMap = new HashMap<>();
        List<KnowledgeCategoryVO> rootCategories = new ArrayList<>();

        // 先转换所有分类为VO
        for (KnowledgeCategory category : allCategories) {
            KnowledgeCategoryVO vo = convertToVO(category);
            vo.setChildren(new ArrayList<>());
            categoryMap.put(category.getId(), vo);
        }

        // 构建树结构
        for (KnowledgeCategoryVO vo : categoryMap.values()) {
            if (vo.getParentId() == null) {
                // 根节点
                rootCategories.add(vo);
            } else {
                // 子节点
                KnowledgeCategoryVO parent = categoryMap.get(vo.getParentId());
                if (parent != null) {
                    parent.getChildren().add(vo);
                }
            }
        }

        // 按排序顺序排序
        sortCategories(rootCategories);
        
        return rootCategories;
    }

    @Override
    public KnowledgeCategoryVO getCategoryVO(Long id, Long userId) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "分类ID无效");
        }

        KnowledgeCategory category = this.getById(id);
        if (category == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "知识分类不存在");
        }

        // 权限校验
        if (!category.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限查看该知识分类");
        }

        return convertToVO(category);
    }

    @Override
    public List<Long> getChildCategoryIds(Long categoryId, Long userId) {
        if (categoryId == null || userId == null) {
            return new ArrayList<>();
        }

        List<Long> result = new ArrayList<>();
        result.add(categoryId);

        // 递归获取所有子分类
        collectChildIds(categoryId, userId, result);
        
        return result;
    }

    /**
     * 递归收集子分类ID
     */
    private void collectChildIds(Long parentId, Long userId, List<Long> result) {
        List<KnowledgeCategory> children = knowledgeCategoryMapper.selectByParentId(parentId, userId);
        for (KnowledgeCategory child : children) {
            result.add(child.getId());
            collectChildIds(child.getId(), userId, result);
        }
    }

    /**
     * 递归排序分类
     */
    private void sortCategories(List<KnowledgeCategoryVO> categories) {
        categories.sort((a, b) -> {
            int orderA = a.getSortOrder() != null ? a.getSortOrder() : 0;
            int orderB = b.getSortOrder() != null ? b.getSortOrder() : 0;
            return Integer.compare(orderA, orderB);
        });

        for (KnowledgeCategoryVO category : categories) {
            if (category.getChildren() != null && !category.getChildren().isEmpty()) {
                sortCategories(category.getChildren());
            }
        }
    }

    /**
     * 将实体转换为VO
     */
    private KnowledgeCategoryVO convertToVO(KnowledgeCategory category) {
        if (category == null) {
            return null;
        }

        KnowledgeCategoryVO vo = new KnowledgeCategoryVO();
        BeanUtils.copyProperties(category, vo);

        // 获取知识条目数量
        Integer entryCount = knowledgeCategoryMapper.countEntriesByCategoryId(category.getId());
        vo.setEntryCount(entryCount != null ? entryCount : 0);

        // 如果有父分类，获取父分类名称
        if (category.getParentId() != null) {
            KnowledgeCategory parent = this.getById(category.getParentId());
            if (parent != null) {
                vo.setParentName(parent.getName());
            }
        }

        return vo;
    }
}