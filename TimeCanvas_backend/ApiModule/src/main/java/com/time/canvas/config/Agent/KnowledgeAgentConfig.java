package com.time.canvas.config.Agent;

import com.time.canvas.assistant.KnowledgeAssistant;
import com.time.canvas.dao.AiConfigDao;
import com.time.canvas.domain.AiConfig;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Resource;

/**
 * 知识库AI配置类
 * 
 * @author wangliang
 */
@Configuration
@Slf4j
public class KnowledgeAgentConfig {

    @Resource
    private AiConfigDao aiConfigDao;

    /**
     * 配置知识库AI助手
     */
    @Bean
    public KnowledgeAssistant knowledgeAgent() {
        try {
            // 从数据库获取AI配置
            AiConfig aiConfig = aiConfigDao.getKnowledgeAiConfig();
            
            if (aiConfig == null) {
                log.error("知识库AI配置不存在，请在数据库中配置");
                return null;
            }

            // 创建OpenAI聊天模型
            OpenAiChatModel chatModel = OpenAiChatModel.builder()
                    .apiKey(aiConfig.getApiKey())
                    .modelName(aiConfig.getModelName())
                    .baseUrl(aiConfig.getBaseUrl())
                    .temperature(0.7) // 知识库任务需要相对稳定的输出
                    .maxTokens(2000)
                    .build();

            // 创建AI服务
            return AiServices.create(KnowledgeAssistant.class, chatModel);
            
        } catch (Exception e) {
            log.error("知识库AI助手配置失败", e);
            return null;
        }
    }
}