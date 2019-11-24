package com.xm.comment_serialize.module.user.form;

import lombok.Data;

@Data
public class GetUserInfoForm {
//    @NotNull(message = "from 不能为空")
    private String openId;
//    @NotEmpty(message = "shopIds不能为空")
    private String code;
//    @NotEmpty(message = "urls不能为空")
    private Integer userId;
}
