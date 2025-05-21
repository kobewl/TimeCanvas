package com.time.canvas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "用户视图对象")
public class UserVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "用户角色: 1-管理员, 2-VIP, 3-普通用户")
    private Integer userRole;

    @Schema(description = "用户邮箱")
    private String userEmail;

    @Schema(description = "用户电话")
    private String userPhone;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "个人简介")
    private String bio;

    @Schema(description = "性别: male-男, female-女, other-其他")
    private String gender;

    @Schema(description = "出生日期")
    private Date birthDate;

    @Schema(description = "最后登录时间")
    private Date lastLoginTime;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "JWT令牌")
    private String token;
} 