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
    USER_ADD_PROXY(10001,"新增一个下级用户", UserAddProxyMessage.class),
    USER_SHARE_GOODS(10002,"分享商品被点击", UserShareGoodsMessage.class),
    USER_SHARE_APP(10003,"分享程序被点击", SuUserEntity.class),

    //用户订单事件
    ORDER_CREATE(10101,"用户下单", OrderCreateMessage.class),
    ORDER_STATE_CHANGE(10102,"用户订单状态变更", OrderStateChangeMessage.class),
    ORDER_SETTLEMENT_SUCESS(10103,"用户订单交易成功", OrderSettlementSucess.class),
    ORDER_SETTLEMENT_FAIL(10104,"用户订单交易失败",OrderSettlementFail.class),
    ORDER_COMMISSION_SUCESS(10105,"用户订单佣金结算成功", OrderCommissionSucess.class),
    ORDER_COMMISSION_FAIL(10106,"用户订单佣金结算失败", OrderCommissionFail.class),

    //用户使用事件
    USER_OPEN_APP(10201,"用户打开程序", UserOpenAppMessage.class),
    USER_SHOW_GOODS(10202,"用户浏览一个商品", UserShowGoodsMessage.class),
    USER_SEARCH_GOODS(10203,"用户搜索一个商品", UserSearchGoodsMessage.class),
    USER_SMART_SEARCH_GOODS(10203,"用户智能搜索一个商品", SuUserEntity.class),
    ;

    UserActionEnum(Integer type, String name, Class messageType) {
        this.type = type;
        this.name = name;
        this.messageType = messageType;
    }

    //事件类型
    private Integer type;
    //事件名称
    private String name;
    //消息类型
    private Class messageType;

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
}
