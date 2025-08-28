package com.time.canvas.vo.knowledge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识条目视图对象
 * 
 * @author wangliang
 */
@Data
@Schema(description = "知识条目视图对象")
public class KnowledgeEntryVO {

    /**
     * 知识条目ID
     */
    @Schema(description = "知识条目ID")
    private Long id;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "内容")
    private String content;

    /**
     * AI生成的摘要
     */
    @Schema(description = "AI生成的摘要")
    private String summary;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID")
    private Long categoryId;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String categoryName;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 标签列表
     */
    @Schema(description = "标签列表")
    private List<String> tagList;

    /**
     * 查看次数
     */
    @Schema(description = "查看次数")
    private Integer viewCount;

    /**
     * 来源类型
     */
    @Schema(description = "来源类型")
    private String sourceType;

    /**
     * 来源ID
     */
    @Schema(description = "来源ID")
    private Long sourceId;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    /**
     * 相似度分数（搜索时使用）
     */
    @Schema(description = "相似度分数")
    private Float similarityScore;

    /**
     * 相关知识条目
     */
    @Schema(description = "相关知识条目")
    private List<KnowledgeEntryVO> relatedEntries;
}