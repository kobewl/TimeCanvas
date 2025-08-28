package com.time.canvas.config.Agent;

import com.time.canvas.assistant.DiaryAssistant;
import com.time.canvas.dao.AiConfigDao;
import com.time.canvas.domain.AiConfig;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiaryAgentConfig {

    @Autowired
    private AiConfigDao configDao;

    @Bean
    public DiaryAssistant chatAssistant() {
        OpenAiChatModel chatModel = diaryChatModel();
        return AiServices.create(DiaryAssistant.class, chatModel);
    }

    @Bean
    public OpenAiChatModel diaryChatModel() {
        AiConfig config = configDao.getConfig("deepseek-chat");
        if (config == null) {
            throw new RuntimeException("无法找到AI模型配置");
        }
        return OpenAiChatModel.builder()
                .apiKey(config.getApiKey())
                .modelName(config.getModelName())
                .baseUrl(config.getBaseUrl())
                .build();
    }

}
