package com.time.canvas.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识分类实体类
 * 
 * @author wangliang
 */
@Data
@TableName("knowledge_category")
@Schema(description = "知识分类")
public class KnowledgeCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "分类ID")
    private Long id;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String name;

    /**
     * 分类描述
     */
    @Schema(description = "分类描述")
    private String description;

    /**
     * 父分类ID
     */
    @Schema(description = "父分类ID")
    private Long parentId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 排序顺序
     */
    @Schema(description = "排序顺序")
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    /**
     * 是否删除
     */
    @TableLogic
    @Schema(description = "是否删除")
    private Integer isDeleted;

    /**
     * 子分类列表（用于树形结构展示）
     */
    @TableField(exist = false)
    @Schema(description = "子分类列表")
    private List<KnowledgeCategory> children;

    /**
     * 该分类下的知识条目数量
     */
    @TableField(exist = false)
    @Schema(description = "知识条目数量")
    private Integer entryCount;
}