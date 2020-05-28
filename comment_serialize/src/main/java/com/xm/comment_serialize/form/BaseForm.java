package com.xm.comment_serialize.form;

import lombok.Data;

@Data
public class BaseForm {
    private Integer appType;
    private Integer platformType;
    private Integer userId;
    private String pid;
    private String ip;
    private String openId;
}
