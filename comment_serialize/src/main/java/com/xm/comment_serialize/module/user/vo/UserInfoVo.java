package com.xm.comment_serialize.module.user.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoVo implements Serializable {
    private Integer id;
    private String nickname;
    private String headImg;
    private String openId;
    private Integer sex;
    private Integer state;
    private Integer pid;
}
