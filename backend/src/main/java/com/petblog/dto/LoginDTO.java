package com.petblog.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 登录请求数据传输对象
 * 用于接收前端传来的登录参数
 */
@Data
public class LoginDTO {

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
}
