package com.time.canvas.dto.Template;

import com.time.canvas.request.PageRequest;

import java.io.Serializable;

public class TemplateQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private String content;
    private String tags;
    private Long creatorName;
}
