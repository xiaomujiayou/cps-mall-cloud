package com.xm.comment_serialize.module.user.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AdminLoginForm {
    @NotBlank(message = "请输入用户名")
    private String userName;
    @NotBlank(message = "请输入密码")
    private String password;
}
