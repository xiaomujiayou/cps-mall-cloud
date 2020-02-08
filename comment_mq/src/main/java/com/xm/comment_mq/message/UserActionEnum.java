package com.xm.comment_mq.message;

import com.xm.comment_mq.message.impl.*;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import lombok.Data;

/**
 * 用户动态信息
 * 站在用户的层面上考虑
 */
public enum UserActionEnum {

    //用户分享事件
    USER_FRIST_LOGIN(10000,"首次登录", UserAddProxyMessage.class,true),
    USER_ADD_PROXY(10001,"新增一个下级用户", UserAddProxyMessage.class,true),
    USER_SHARE_GOODS(10002,"分享商品被点击", UserShareGoodsMessage.class,true),
    USER_SHARE_APP(10003,"分享程序被点击", SuUserEntity.class,false),

    //用户订单事件
    ORDER_CREATE(10101,"用户下单", OrderCreateMessage.class,true),
    ORDER_STATE_CHANGE(10102,"用户订单状态变更", OrderStateChangeMessage.class,true),
    ORDER_SETTLEMENT_SUCESS(10103,"用户订单交易成功", OrderSettlementSucessMessage.class,true),
    ORDER_SETTLEMENT_FAIL(10104,"用户订单交易失败",OrderSettlementFailMessage.class,true),
    ORDER_COMMISSION_SUCESS(10105,"用户订单佣金结算成功", OrderCommissionSucessMessage.class,false),
    ORDER_COMMISSION_FAIL(10106,"用户订单佣金结算失败", OrderCommissionFailMessage.class,false),

    //用户账单事件,
    USER_BILL_CREATE(10301,"用户账单已创建", UserBillCreateMessage.class,true),
    USER_BILL_STATE_CHANGE(10302,"用户账单状态已改变", UserBillStateChangeMessage.class,true),
    USER_BILL_COMMISSION_SUCESS(10303,"用户账单结算成功", UserBillCommissionSucessMessage.class,false),
    USER_BILL_COMMISSION_FAIL(10304,"用户账单结算失败", UserBillCommissionFailSucessMessage.class,false),

    //用户使用事件
    USER_OPEN_APP(10201,"用户打开程序", UserOpenAppMessage.class,false),
    USER_SHOW_GOODS(10202,"用户浏览一个商品", UserShowGoodsMessage.class,true),
    USER_SEARCH_GOODS(10203,"用户搜索一个商品", UserSearchGoodsMessage.class,true),
    USER_SMART_SEARCH_GOODS(10203,"用户智能搜索一个商品", UserSmartSearchGoodsMessage.class,true),
    ;

    UserActionEnum(Integer type, String name, Class messageType, boolean ready) {
        this.type = type;
        this.name = name;
        this.messageType = messageType;
        this.ready = ready;
    }

    //事件类型
    private Integer type;
    //事件名称
    private String name;
    //消息类型
    private Class messageType;
    //消息是否编写就绪
    private boolean ready;

    public Class getMessageType() {
        return messageType;
    }

    public void setMessageType(Class messageType) {
        this.messageType = messageType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
