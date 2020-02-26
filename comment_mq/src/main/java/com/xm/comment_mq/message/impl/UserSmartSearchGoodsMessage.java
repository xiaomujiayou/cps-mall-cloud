package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import lombok.Data;

@Data
public class UserSmartSearchGoodsMessage extends AbsUserActionMessage {

    public UserSmartSearchGoodsMessage() {}

    public UserSmartSearchGoodsMessage(Integer userId, SmProductEntityEx smProductEntityEx, String goodsUrl, String goodsName) {
        super(userId);
        this.smProductEntityEx = smProductEntityEx;
        this.goodsUrl = goodsUrl;
        this.goodsName = goodsName;
    }
    private final UserActionEnum userActionEnum = UserActionEnum.USER_SMART_SEARCH_GOODS;
    //商品信息
    private SmProductEntityEx smProductEntityEx;
    //商品链接
    private String goodsUrl;
    //商品标题(只能搜的标题)
    private String goodsName;
}
