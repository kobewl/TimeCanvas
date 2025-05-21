package com.time.canvas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@Schema(description = "用户注册请求")
public class UserRegisterRequest {

    @NotBlank(message = "账号不能为空")
    @Size(min = 4, max = 20, message = "账号长度必须在4-20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "账号只能包含字母、数字和下划线")
    @Schema(description = "用户账号", example = "user123")
    private String userAccount;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度必须在8-20个字符之间")
    @Schema(description = "用户密码", example = "password123")
    private String userPassword;

    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码", example = "password123")
    private String checkPassword;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间")
    @Schema(description = "用户名", example = "张三")
    private String userName;

    @Email(message = "邮箱格式不正确")
    @Schema(description = "用户邮箱", example = "example@email.com")
    private String userEmail;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "用户手机号", example = "13800138000")
    private String userPhone;
} 