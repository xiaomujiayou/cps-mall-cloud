package com.xm.comment_serialize.module.cron.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ScBillPayVo {
    private Integer id;
    private String name;
    private Integer totalMoney;
    private Integer state;
    private String failReason;
    private String processSn;
    private Date createTime;
}
