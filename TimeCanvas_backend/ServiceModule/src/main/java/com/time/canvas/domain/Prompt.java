package com.time.canvas.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * Prompt表
 * @TableName prompt
 */
@TableName(value ="prompt")
@Data
public class Prompt {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * Prompt名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * Prompt内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 类型: diary-日记生成, report-报告生成, todo-待办生成, expense-开支分析, analysis-情感分析, other-其他
     */
    @TableField(value = "type")
    private Object type;

    /**
     * 创建人ID，NULL表示系统默认
     */
    @TableField(value = "creator_id")
    private Long creatorId;

    /**
     * 是否系统默认: 0-用户自定义, 1-系统默认
     */
    @TableField(value = "is_system")
    private Integer isSystem;

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