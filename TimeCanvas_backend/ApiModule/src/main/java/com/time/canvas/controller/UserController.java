package com.time.canvas.controller;

import com.time.canvas.model.UserLoginRequest;
import com.time.canvas.model.UserRegisterRequest;
import com.time.canvas.model.UserVO;
import com.time.canvas.model.dto.UserUpdateRequest;
import com.time.canvas.service.UserService;
import com.time.canvas.util.BaseResponse;
import com.time.canvas.util.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口", description = "用户注册、登录等接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册请求
     * @return 注册结果
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "用户注册接口")
    public BaseResponse<Long> userRegister(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        long userId = userService.userRegister(userRegisterRequest);
        return ResultUtils.success(userId);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录接口")
    public BaseResponse<UserVO> userLogin(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        UserVO userVO = userService.userLogin(userLoginRequest);
        return ResultUtils.success(userVO);
    }

    /**
     * 获取当前登录用户
     *
     * @param request HTTP请求
     * @return 当前用户信息
     */
    @GetMapping("/current")
    @Operation(summary = "获取当前用户", description = "获取当前登录用户信息")
    public BaseResponse<UserVO> getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        UserVO userVO = userService.getCurrentUser(token);
        return ResultUtils.success(userVO);
    }

    /**
     * 用户注销
     *
     * @param request HTTP请求
     * @return 注销结果
     */
    @PostMapping("/logout")
    @Operation(summary = "用户注销", description = "用户注销接口")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        userService.userLogout(token);
        return ResultUtils.success(true);
    }

    /**
     * 更新用户个人资料
     *
     * @param userUpdateRequest 更新请求体
     * @return 是否更新成功
     */
    @PostMapping("/updateProfile")
    @Operation(summary = "更新用户个人资料", description = "更新当前登录用户的个人资料")
    public BaseResponse<Boolean> updateUserProfile(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        boolean result = userService.updateUserProfile(userUpdateRequest);
        return ResultUtils.success(result);
    }
} 