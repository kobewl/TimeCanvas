package com.time.canvas.dto.Template;

import lombok.Data;
import java.io.Serializable;

/**
 * 修改日记模板请求
 */
@Data
public class TemplateUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 模板ID */
    private Long id;
    /** 模板类型 */
    private String type;
    /** 模板内容 */
    private String content;
    /** 标签，逗号分隔 */
    private String tags;
} 