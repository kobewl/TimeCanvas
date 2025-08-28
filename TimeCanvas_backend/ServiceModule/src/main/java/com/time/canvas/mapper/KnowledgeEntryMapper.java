package com.time.canvas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.time.canvas.domain.KnowledgeEntry;
import com.time.canvas.dto.knowledge.KnowledgeEntryQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识条目Mapper接口
 * 
 * @author wangliang
 */
@Mapper
public interface KnowledgeEntryMapper extends BaseMapper<KnowledgeEntry> {

    /**
     * 分页查询知识条目
     * 
     * @param page 分页参数
     * @param queryRequest 查询条件
     * @return 知识条目分页数据
     */
    IPage<KnowledgeEntry> selectKnowledgeEntryPage(Page<KnowledgeEntry> page, 
                                                  @Param("query") KnowledgeEntryQueryRequest queryRequest);

    /**
     * 根据标签查询知识条目
     * 
     * @param tags 标签列表
     * @param userId 用户ID
     * @return 知识条目列表
     */
    List<KnowledgeEntry> selectByTags(@Param("tags") List<String> tags, 
                                     @Param("userId") Long userId);

    /**
     * 搜索知识条目（全文搜索）
     * 
     * @param keyword 搜索关键词
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 知识条目列表
     */
    List<KnowledgeEntry> searchKnowledgeEntries(@Param("keyword") String keyword, 
                                               @Param("userId") Long userId,
                                               @Param("limit") Integer limit);

    /**
     * 获取热门标签
     * 
     * @param userId 用户ID
     * @param limit 返回数量限制
     * @return 标签列表
     */
    List<String> getPopularTags(@Param("userId") Long userId, 
                               @Param("limit") Integer limit);

    /**
     * 更新查看次数
     * 
     * @param id 知识条目ID
     */
    void incrementViewCount(@Param("id") Long id);

    /**
     * 获取用户统计信息
     * 
     * @param userId 用户ID
     * @return 统计信息
     */
    KnowledgeEntry getUserStatistics(@Param("userId") Long userId);
}