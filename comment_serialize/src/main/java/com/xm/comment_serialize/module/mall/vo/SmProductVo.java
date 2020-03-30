package com.xm.comment_serialize.module.mall.vo;

import lombok.Data;

@Data
public class SmProductVo {
    private Integer id;
    private Integer type;
    private String goodsId;
    private String goodsThumbnailUrl;
    private String goodsGalleryUrls;
    private String name;
    private String des;
    private Integer originalPrice;
    private Integer couponPrice;
    private Integer couponStartFee;
    private Integer sharePrice;
    private Integer buyPrice;
    private String mallId;
    private String mallName;
    private String salesTip;
    private Integer mallCps;
    private Integer hasCoupon;
    private String couponId;
    private String serviceTags;
    private String activityType;
    private java.util.Date createTime;
    //淘宝购买链接，用于生成淘口令
    private String tbBuyUrl;
}
