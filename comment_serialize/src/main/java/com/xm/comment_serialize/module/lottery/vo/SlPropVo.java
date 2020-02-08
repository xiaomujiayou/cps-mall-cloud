package com.xm.comment_serialize.module.lottery.vo;

import lombok.Data;

import java.util.List;

@Data
public class SlPropVo {

    /**
     * name : VIP会员
     * price : ￥10
     * has : false
     * specDes : 请选择购买时长
     * des : 畅想vip权益
     * disable : false
     * disableTips :
     * spec : [{"name":"7天","price":"5","des":"返5元","choose":true}]
     */
    private String name;
    private String price;
    private boolean has;
    private String specDes;
    private String des;
    private List<SpecBean> spec;

    @Data
    public static class SpecBean {
        /**
         * name : 7天
         * price : 5
         * des : 返5元
         * choose : true
         */
        private String name;
        private String price;
        private String originalPrice;
        private String des;
        private boolean choose;
        private boolean disable;
        private String disableTips;
    }
}
