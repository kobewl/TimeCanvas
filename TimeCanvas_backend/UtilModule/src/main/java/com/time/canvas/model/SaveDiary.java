package com.time.canvas.model;

import lombok.Data;

@Data
public class SaveDiary {
    private String title;
    private String content;
    private int templateId;
    private String mood;
    private String tags;
    private String visibility;
}
