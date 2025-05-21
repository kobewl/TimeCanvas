package com.time.canvas.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * Todo List 表
 * @TableName todo_list
 */
@TableName(value ="todo_list")
@Data
public class TodoList {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 待办事项内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 优先级: 0-普通, 1-高, 2-紧急
     */
    @TableField(value = "priority")
    private Integer priority;

    /**
     * 状态: pending-待办, completed-已完成
     */
    @TableField(value = "status")
    private Object status;

    /**
     * 截止日期
     */
    @TableField(value = "due_date")
    private Date dueDate;

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