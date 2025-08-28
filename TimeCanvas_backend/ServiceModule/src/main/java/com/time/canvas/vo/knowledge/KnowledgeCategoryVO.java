package com.time.canvas.vo.knowledge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 知识分类VO
 * 
 * @author wangliang
 */
@Data
@Schema(description = "知识分类VO")
public class KnowledgeCategoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
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
     * 父分类名称
     */
    @Schema(description = "父分类名称")
    private String parentName;

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
    @Schema(description = "创建时间")
    private LocalDateTime createdTime;

    /**
     * 子分类列表
     */
    @Schema(description = "子分类列表")
    private List<KnowledgeCategoryVO> children;

    /**
     * 知识条目数量
     */
    @Schema(description = "知识条目数量")
    private Integer entryCount;
}