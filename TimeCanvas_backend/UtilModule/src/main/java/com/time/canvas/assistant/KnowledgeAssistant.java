package com.time.canvas.assistant;

import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * 知识库AI助手
 * 
 * @author wangliang
 */
public interface KnowledgeAssistant {

    /**
     * 生成知识摘要
     * 
     * @param content 知识内容
     * @return 摘要
     */
    @UserMessage("请为以下内容生成一个简洁的摘要，控制在100字以内：\n{{content}}")
    String generateSummary(@V("content") String content);

    /**
     * 提取关键词标签
     * 
     * @param content 知识内容
     * @return 标签列表（逗号分隔）
     */
    @UserMessage("请从以下内容中提取5-10个关键词作为标签，用逗号分隔：\n{{content}}")
    String extractTags(@V("content") String content);

    /**
     * 从日记中提取知识点
     * 
     * @param diaryContent 日记内容
     * @return 知识点列表
     */
    @UserMessage("请从以下日记内容中提取可以形成知识条目的信息，每个知识点包含标题和内容，用JSON格式返回：\n{{diaryContent}}")
    String extractKnowledgeFromDiary(@V("diaryContent") String diaryContent);

    /**
     * 知识库问答
     * 
     * @param question 用户问题
     * @param knowledgeContext 相关知识上下文
     * @return 答案
     */
    @UserMessage("基于以下知识库内容回答问题。如果知识库中没有相关信息，请说明无法基于现有知识回答。\n\n知识库内容：\n{{knowledgeContext}}\n\n用户问题：{{question}}")
    String answerQuestion(@V("question") String question, @V("knowledgeContext") String knowledgeContext);

    /**
     * 知识关联分析
     * 
     * @param sourceContent 源知识内容
     * @param targetContent 目标知识内容
     * @return 关联分析结果
     */
    @UserMessage("分析以下两个知识条目的关联性，返回关联类型（related/reference/derived）和关联理由：\n\n知识条目1：\n{{sourceContent}}\n\n知识条目2：\n{{targetContent}}")
    String analyzeRelation(@V("sourceContent") String sourceContent, @V("targetContent") String targetContent);

    /**
     * 知识结构化
     * 
     * @param rawContent 原始内容
     * @return 结构化后的内容
     */
    @UserMessage("请将以下内容结构化整理，包含：标题、要点、详细说明、相关概念等部分：\n{{rawContent}}")
    String structureKnowledge(@V("rawContent") String rawContent);

    /**
     * 生成知识点建议
     * 
     * @param topic 主题
     * @param existingKnowledge 已有知识
     * @return 建议
     */
    @UserMessage("基于主题'{{topic}}'和已有知识，建议用户还应该学习哪些相关知识点：\n\n已有知识：\n{{existingKnowledge}}")
    String suggestKnowledge(@V("topic") String topic, @V("existingKnowledge") String existingKnowledge);
}