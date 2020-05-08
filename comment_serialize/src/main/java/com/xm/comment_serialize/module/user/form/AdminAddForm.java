package com.xm.comment_serialize.module.user.form;

import com.xm.comment_serialize.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AdminAddForm {
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String headImg;
}
