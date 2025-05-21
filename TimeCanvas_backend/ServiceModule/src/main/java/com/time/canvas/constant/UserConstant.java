package com.time.canvas.constant;

/**
 * 用户常量
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    /**
     * 密码加密盐值
     */
    String SALT = "TimeCanvas";

    /**
     * 用户角色：管理员
     */
    int ADMIN_ROLE = 1;

    /**
     * 用户角色：VIP用户
     */
    int VIP_ROLE = 2;

    /**
     * 用户角色：普通用户
     */
    int NORMAL_ROLE = 3;
} 