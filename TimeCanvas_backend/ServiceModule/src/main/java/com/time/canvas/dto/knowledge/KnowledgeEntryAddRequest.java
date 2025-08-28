package com.time.canvas.dto.knowledge;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 知识条目创建请求
 * 
 * @author wangliang
 */
@Data
@Schema(description = "知识条目创建请求")
public class KnowledgeEntryAddRequest {

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Schema(description = "标题", required = true)
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @Schema(description = "内容", required = true)
    private String content;

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
     * 来源ID
     */
    @Schema(description = "来源ID")
    private Long sourceId;
}