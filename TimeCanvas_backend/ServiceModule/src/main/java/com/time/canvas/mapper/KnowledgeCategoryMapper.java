package com.time.canvas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.time.canvas.domain.KnowledgeCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识分类Mapper接口
 * 
 * @author wangliang
 */
@Mapper
public interface KnowledgeCategoryMapper extends BaseMapper<KnowledgeCategory> {

    /**
     * 查询用户的分类树
     * 
     * @param userId 用户ID
     * @return 分类树列表
     */
    List<KnowledgeCategory> selectCategoryTree(@Param("userId") Long userId);

    /**
     * 查询某分类下的子分类
     * 
     * @param parentId 父分类ID
     * @param userId 用户ID
     * @return 子分类列表
     */
    List<KnowledgeCategory> selectByParentId(@Param("parentId") Long parentId, 
                                           @Param("userId") Long userId);

    /**
     * 查询分类及其知识条目数量
     * 
     * @param userId 用户ID
     * @return 分类列表（含条目数量）
     */
    List<KnowledgeCategory> selectCategoryWithCount(@Param("userId") Long userId);

    /**
     * 检查分类名称是否存在
     * 
     * @param name 分类名称
     * @param userId 用户ID
     * @param excludeId 排除的分类ID（用于更新时检查）
     * @return 是否存在
     */
    boolean existsByName(@Param("name") String name, 
                        @Param("userId") Long userId, 
                        @Param("excludeId") Long excludeId);
}