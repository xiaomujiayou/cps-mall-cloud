package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.vo.SmProductVo;
import lombok.Data;

@Data
public class UserClickGoodsMessage extends AbsUserActionMessage {

    public UserClickGoodsMessage() {}

    public UserClickGoodsMessage(Integer userId, Integer fromUserId,String ip,Integer appType, SmProductEntityEx smProductEntityEx) {
        super(userId);
        this.fromUserId = fromUserId;
        this.smProductEntityEx = smProductEntityEx;
        this.appType = appType;
        this.ip = ip;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_CLICK_GOODS;
    private Integer fromUserId;
    private Integer appType;
    private String ip;
    private SmProductEntityEx smProductEntityEx;
}
