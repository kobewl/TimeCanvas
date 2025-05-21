package com.time.canvas.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 日记模版表
 * @TableName template
 */
@TableName(value ="template")
@Data
public class Template {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模版类型: 如日记、周报、月报
     */
    @TableField(value = "type")
    private String type;

    /**
     * 模版内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 创建人ID，NULL表示系统创建
     */
    @TableField(value = "creator_id")
    private Long creatorId;

    /**
     * 模版标签，逗号分隔，如日常,反思,情感
     */
    @TableField(value = "tags")
    private String tags;

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