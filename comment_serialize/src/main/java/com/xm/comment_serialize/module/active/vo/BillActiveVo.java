package com.xm.comment_serialize.module.active.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BillActiveVo {
    private Integer billId;
    private Integer money;
    private Integer state;
    private String activeName;
    private String attachDes;
    private String failReason;
    private String createTime;
}
