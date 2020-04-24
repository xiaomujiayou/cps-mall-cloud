package com.xm.comment_serialize.module.user.vo;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.xm.comment_serialize.module.user.constant.BillStateConstant;
import com.xm.comment_serialize.module.user.constant.OrderStateConstant;
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
    private Integer billState;
    private String failReason;
    private Integer orderState;
    private String orderSn;
    private Integer originalPrice;
    private Integer quantity;
    private Integer amount;
    private Integer money;
    private Integer creditState;
    private String createTime;
    //支付时间
    private Date payTime;
    //状态简介
    private String stateDesc;
    //返现倒计时
    private Long countDown;


    /**
     * 信用支付状态(0:非信用支付,1:达到信用支付条件,2:信用下降解除绑定)
     * 订单状态(-1:无效订单,0:未支付,1:已支付,2:确认收货,3:已结算,4:结算失败)
     * 账单状态(1:等待确认,2:准备发放,3:已发放,4:已失效)
     *
     * 信用支付状态-订单状态-账单状态
     * 0-1-1：等待确认收货
     * 0-2-1：等待交易结束
     * 0-3-2：即将返现至微信零钱
     * 0-3-3：返现完成
     * 0-4-4：--失败原因--
     *
     * 1-1-1：根据您的信用，确认收货即可返现
     * 1-2-1：--返现倒计时--
     * 1-2-2：--返现倒计时--
     * 1-2-3：返现完成(退单将影响信用)
     * 1-3-3：返现完成(信用增加)
     * 1-4-3：恶意退款(将影响您的信用)
     * 1-4-4：--失败原因--
     *
     * 2-2-1：由于信用下降，返现需要等待交易彻底结束，请耐心等待
     * 2-3-2：即将返现至微信零钱
     * 2-3-3：返现完成
     * 2-4-4：--失败原因--
     */
    public Long getCountDown() {
        if(checkState("1-2-1") || checkState("1-2-2")){
            if(payTime == null)
                return null;
            //返现倒计时
            if(payTime.getTime() > new Date().getTime())
                return payTime.getTime() - new Date().getTime();
            return 0L;
        }
        return null;
    }

    public String getStateDesc() {
        if(checkState("0-1-1")){
            return "等待确认收货";
        }else if(checkState("0-2-1")){
            return "等待交易结束";
        }else if(checkState("0-3-2")){
            return "即将返现至微信零钱";
        }else if(checkState("0-3-3")){
            return "返现完成";
        }else if(checkState("0-4-4")){
            return failReason;
        }else if(checkState("1-1-1")){
            return "根据您的信用，确认收货即可返现";
        }else if(checkState("1-2-3")){
            return "返现完成(退单将影响信用)";
        }else if(checkState("1-3-3")){
            return "返现完成(守约，信用增加)";
        }else if(checkState("1-4-3")){
            return "返现后 退单、取消订单将影响您的信用";
//            return "恶意退款(将影响您的信用)";
        }else if(checkState("1-4-4")){
            return failReason;
        }else if(checkState("2-2-1")){
            return "由于信用下降，返现需要等待交易彻底结束，请耐心等待";
        }else if(checkState("2-3-2")){
            return "即将返现至微信零钱";
        }else if(checkState("2-3-3")){
            return "返现完成";
        }else if(checkState("2-4-4")){
            return failReason;
        }
        return null;
    }

    /**
     * 状态检测
     * @param stateStr
     * @return
     */
    private boolean checkState(String stateStr){
        String[] states = stateStr.split("-");
        if(creditState == null)
            creditState = 0;
        if(Integer.valueOf(states[0]).equals(creditState) && Integer.valueOf(states[1]).equals(orderState) && Integer.valueOf(states[2]).equals(billState))
            return true;
        return false;
    }
}
