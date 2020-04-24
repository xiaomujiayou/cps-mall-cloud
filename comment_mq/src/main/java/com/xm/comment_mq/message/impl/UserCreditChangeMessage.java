package com.xm.comment_mq.message.impl;

import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.UserActionEnum;
import com.xm.comment_serialize.module.wind.constant.ChangeCreditEnum;
import com.xm.comment_serialize.module.wind.entity.SwCreditRecordEntity;
import lombok.Data;

@Data
public class UserCreditChangeMessage extends AbsUserActionMessage {
    public UserCreditChangeMessage() {}

    public UserCreditChangeMessage(Integer userId, SwCreditRecordEntity oldRecord, SwCreditRecordEntity newRecord, ChangeCreditEnum changeCreditEnum) {
        super(userId);
        this.oldRecord = oldRecord;
        this.newRecord = newRecord;
        this.changeCreditEnum = changeCreditEnum;
    }

    private final UserActionEnum userActionEnum = UserActionEnum.USER_CREDIT_CHANGE;
    //老记录
    private SwCreditRecordEntity oldRecord;
    //新纪录
    private SwCreditRecordEntity newRecord;
    //变更类型
    private ChangeCreditEnum changeCreditEnum;
}
