package com.xm.comment_serialize.module.user.vo;


import lombok.Data;

import java.util.Date;

@Data
public class OrderBillVo {
    private String orderNum;
    private String userName;
    private String headImg;
    private String productImgUrl;
    private String title;
    private Integer platformType;
    private String goodsId;
    private Integer billId;
    private String billSn;
    private Integer orderState;
    private Integer state;
    private Integer originalPrice;
    private Integer quantity;
    private Integer amount;
    private Integer money;
    private Date createTime;
}
