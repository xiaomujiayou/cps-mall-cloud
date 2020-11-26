package com.xm.comment_serialize.module.user.bo;

import lombok.Data;

@Data
public class OrderCustomParameters {
    private Integer uid;
    private Integer userId;
    private Integer shareUserId;
    private Integer fromApp;
    private String pid;
}
