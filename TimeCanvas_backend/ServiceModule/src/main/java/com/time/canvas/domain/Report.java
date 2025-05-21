package com.time.canvas.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 日报、周报、月报表
 * @TableName report
 */
@TableName(value ="report")
@Data
public class Report {
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
     * 报告内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 类型: daily-日报, weekly-周报, monthly-月报
     */
    @TableField(value = "type")
    private Object type;

    /**
     * 报告日期
     */
    @TableField(value = "report_date")
    private Date reportDate;

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