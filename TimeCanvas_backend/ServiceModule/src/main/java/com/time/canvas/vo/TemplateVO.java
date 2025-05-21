package com.time.canvas.vo;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;


@Data
public class TemplateVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 模版类型: 如简洁、详细、情感等
     */
    private String type;

    /**
     * 模版内容
     */
    private String content;

    /**
     * 创建人姓名，NULL表示系统创建
     */
    private String creatorName;

    /**
     * 模版标签，逗号分隔，如日常,反思,情感
     */
    private String tags;

    /**
     * 创建时间
     */
    private Date createTime;


}
