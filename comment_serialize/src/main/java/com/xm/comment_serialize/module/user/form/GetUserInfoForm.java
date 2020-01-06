package com.xm.comment_serialize.module.user.form;

import lombok.Data;

@Data
public class GetUserInfoForm {
    private Integer shareUserId;
    private String openId;
    private String code;
//    private Integer userId;
}
