package com.time.canvas.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 日记表
 * @TableName diary
 */
@TableName(value ="diary")
@Data
public class Diary {
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
     * 日记标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 日记内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 模版ID
     */
    @TableField(value = "template_id")
    private Long templateId;

    /**
     * 心情
     */
    @TableField(value = "mood")
    private String mood;

    /**
     * 标签，逗号分隔
     */
    @TableField(value = "tags")
    private String tags;

    /**
     * 可见性: public-公开, private-私有
     */
    @TableField(value = "visibility")
    private Object visibility;

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