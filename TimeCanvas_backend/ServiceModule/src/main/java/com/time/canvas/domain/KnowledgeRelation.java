package com.time.canvas.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 知识关联实体类
 * 
 * @author wangliang
 */
@Data
@TableName("knowledge_relation")
@Schema(description = "知识关联")
public class KnowledgeRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "关联ID")
    private Long id;

    /**
     * 源知识条目ID
     */
    @Schema(description = "源知识条目ID")
    private Long sourceEntryId;

    /**
     * 目标知识条目ID
     */
    @Schema(description = "目标知识条目ID")
    private Long targetEntryId;

    /**
     * 关联类型：related-相关，reference-引用，derived-衍生
     */
    @Schema(description = "关联类型")
    private String relationType;

    /**
     * 相似度分数
     */
    @Schema(description = "相似度分数")
    private Float similarityScore;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    /**
     * 源知识条目信息（关联查询使用）
     */
    @TableField(exist = false)
    @Schema(description = "源知识条目")
    private KnowledgeEntry sourceEntry;

    /**
     * 目标知识条目信息（关联查询使用）
     */
    @TableField(exist = false)
    @Schema(description = "目标知识条目")
    private KnowledgeEntry targetEntry;
}