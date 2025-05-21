package com.time.canvas.service;

import com.mongodb.internal.bulk.UpdateRequest;
import com.time.canvas.domain.User;
import com.time.canvas.model.UserLoginRequest;
import com.time.canvas.model.UserRegisterRequest;
import com.time.canvas.model.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.time.canvas.model.dto.UserUpdateRequest;

/**
* @author wangliang
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-05-14 14:12:43
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterRequest 注册请求
     * @return 新用户ID
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     * @param userLoginRequest 登录请求
     * @return 登录成功的用户信息和token
     */
    UserVO userLogin(UserLoginRequest userLoginRequest);

    /**
     * 获取当前登录用户
     * @param token JWT令牌
     * @return 用户信息
     */
    UserVO getCurrentUser(String token);

    /**
     * 用户注销
     * @param token JWT令牌
     */
    void userLogout(String token);

    /**
     * 根据账号获取用户
     * @param userAccount 用户账号
     * @return 用户信息
     */
    User getUserByAccount(String userAccount);

    /**
     * 更新用户个人资料
     * @param userUpdateRequest 包含需要更新的字段的请求体
     * @return 是否更新成功
     */
    boolean updateUserProfile(UserUpdateRequest userUpdateRequest);
}
