package com.time.canvas.dto.knowledge;

import com.time.canvas.request.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 知识条目查询请求
 * 
 * @author wangliang
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "知识条目查询请求")
public class KnowledgeEntryQueryRequest extends PageRequest {

    /**
     * 关键词搜索
     */
    @Schema(description = "关键词搜索")
    private String keyword;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID")
    private Long categoryId;

    /**
     * 标签列表
     */
    @Schema(description = "标签列表")
    private List<String> tags;

    /**
     * 来源类型
     */
    @Schema(description = "来源类型")
    private String sourceType;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 是否启用语义搜索
     */
    @Schema(description = "是否启用语义搜索")
    private Boolean enableSemanticSearch;

    /**
     * 语义搜索查询文本
     */
    @Schema(description = "语义搜索查询文本")
    private String semanticQuery;
}