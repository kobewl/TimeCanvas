package com.time.canvas.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface DiaryAssistant {

    // 将系统提示词定义为一个方法参数，使用 @SystemMessage 注解标记
    @SystemMessage("system-prompt.txt")
    String chat(@UserMessage String userMessage);

}