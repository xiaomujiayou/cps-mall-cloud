package com.xm.comment_serialize.module.user.vo;

import lombok.Data;

@Data
public class UserInfoVo {
    private Integer id;
    private String nickname;
    private String headImg;
    private String openId;
    private Integer sex;
}
