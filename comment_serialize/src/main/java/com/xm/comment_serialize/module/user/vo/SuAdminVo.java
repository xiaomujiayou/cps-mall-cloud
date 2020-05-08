package com.xm.comment_serialize.module.user.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SuAdminVo implements Serializable {
    private Integer id;
    private String userName;
    private String headImg;
    private Serializable token;
}
