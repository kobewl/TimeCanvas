package com.time.canvas.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * AI配置平台表
 * @TableName ai_config
 */
@TableName(value ="ai_config")
@Data
public class AiConfig {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模型类型: 如 doubao-1-5-lite-32k-250115
     */
    @TableField(value = "chat_model")
    private String chatModel;

    /**
     * API密钥
     */
    @TableField(value = "api_key")
    private String apiKey;

    /**
     * 模型名称
     */
    @TableField(value = "model_name")
    private String modelName;

    /**
     * API基础地址: 如 https://ark.cn-beijing.volces.com/api/v3
     */
    @TableField(value = "base_url")
    private String baseUrl;

    /**
     * 是否记录请求日志: 0-否, 1-是
     */
    @TableField(value = "log_requests")
    private Integer logRequests;

    /**
     * 是否记录响应日志: 0-否, 1-是
     */
    @TableField(value = "log_responses")
    private Integer logResponses;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除: 0-未删除, 1-已删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;
}