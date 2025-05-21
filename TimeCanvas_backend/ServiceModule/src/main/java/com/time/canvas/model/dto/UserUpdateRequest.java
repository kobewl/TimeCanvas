package com.time.canvas.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

import java.io.Serializable;

/**
 * 用户更新请求体
 */
@Data
public class UserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户昵称不能为空")
    private String userName;

    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空") // 根据您之前的校验，邮箱是必填且校验格式
    private String userEmail;

    @NotBlank(message = "手机号不能为空") // 根据您之前的校验，手机号是必填且校验格式
    @Pattern(regexp = "^\\d{11}$", message = "手机号格式不正确") // 匹配11位数字
    private String userPhone;

    private String bio;

    @NotNull(message = "性别不能为空")
    @Min(value = 0, message = "性别值无效") // 假设0:未知, 1:男, 2:女
    @Max(value = 2, message = "性别值无效")
    private Integer gender;

    // 生日这里，DTO中是String，可以使用@Pattern校验格式，业务层再转换为Date并校验日期有效性
    @NotBlank(message = "生日不能为空") // 根据您之前的校验，生日是必填且校验格式
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "生日日期格式不正确，应为 yyyy-MM-dd")
    private String birthDate;
} 