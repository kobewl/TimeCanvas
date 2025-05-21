package com.time.canvas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Schema(description = "用户登录请求")
public class UserLoginRequest {

    @NotBlank(message = "账号不能为空")
    @Schema(description = "用户账号", example = "user123")
    private String userAccount;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "用户密码", example = "password123")
    private String userPassword;
} 