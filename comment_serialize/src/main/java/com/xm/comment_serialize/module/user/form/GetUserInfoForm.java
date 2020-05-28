package com.xm.comment_serialize.module.user.form;

import com.xm.comment_serialize.form.BaseForm;
import lombok.Data;

@Data
public class GetUserInfoForm {
    private Integer shareUserId;
    private String from;
    private String openId;
    private String code;
    private String ip;
//    private Integer userId;
}
