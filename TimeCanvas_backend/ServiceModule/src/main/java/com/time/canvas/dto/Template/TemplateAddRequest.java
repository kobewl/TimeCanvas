package com.time.canvas.dto.Template;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 新增日记模板请求
 */
@Data
public class TemplateAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 模板类型: 如日记、周报、月报 */
    @NotBlank(message = "模板类型不能为空")
    @Size(max = 50, message = "模板类型不能超过50个字符")
    private String type;

    /** 模板内容 */
    @NotBlank(message = "模板内容不能为空")
    private String content;

    /** 模板标签，逗号分隔，如日常,反思,情感 */
    @Size(max = 255, message = "标签不能超过255个字符")
    private String tags;
} 