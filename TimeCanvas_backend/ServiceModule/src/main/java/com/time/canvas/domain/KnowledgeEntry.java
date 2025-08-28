package com.time.canvas.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识库条目实体类
 * 
 * @author wangliang
 */
@Data
@TableName("knowledge_entry")
@Schema(description = "知识库条目")
public class KnowledgeEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "知识条目ID")
    private Long id;

    /**
     * 标题
     */
    @Schema(description = "知识条目标题")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "知识条目内容")
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
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 标签，逗号分隔
     */
    @Schema(description = "标签，逗号分隔")
    private String tags;

    /**
     * 查看次数
     */
    @Schema(description = "查看次数")
    private Integer viewCount;

    /**
     * 向量嵌入（JSON格式存储）
     */
    @Schema(description = "向量嵌入")
    private String embeddingVector;

    /**
     * 来源类型：manual-手动创建，diary_extract-日记提取，import-导入
     */
    @Schema(description = "来源类型")
    private String sourceType;

    /**
     * 来源ID（如日记ID）
     */
    @Schema(description = "来源ID")
    private Long sourceId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private LocalDateTime updatedTime;

    /**
     * 是否删除
     */
    @TableLogic
    @Schema(description = "是否删除")
    private Integer isDeleted;

    /**
     * 标签列表（用于前端展示，不存储到数据库）
     */
    @TableField(exist = false)
    @Schema(description = "标签列表")
    private List<String> tagList;

    /**
     * 分类名称（关联查询时使用）
     */
    @TableField(exist = false)
    @Schema(description = "分类名称")
    private String categoryName;

    /**
     * 相似度分数（搜索时使用）
     */
    @TableField(exist = false)
    @Schema(description = "相似度分数")
    private Float similarityScore;
}