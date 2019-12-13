package com.xm.comment_serialize.module.user.dto;

import lombok.Data;

@Data
public class ProxyProfitDto {
    private Integer proxyUserId;
    private String proxyName;
    private String proxyHeadImg;
    private Integer proxyNum;
    private Integer proxyProfit;
    private String createTime;
}
