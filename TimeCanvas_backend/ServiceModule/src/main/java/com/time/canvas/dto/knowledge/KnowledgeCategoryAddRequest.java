package com.time.canvas.dto.knowledge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * 知识分类创建请求
 * 
 * @author wangliang
 */
@Data
@Schema(description = "知识分类创建请求")
public class KnowledgeCategoryAddRequest {

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称", required = true)
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
     * 排序顺序
     */
    @Schema(description = "排序顺序")
    private Integer sortOrder;
}