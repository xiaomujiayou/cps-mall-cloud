package com.xm.comment_serialize.module.pay.message;

import com.xm.comment_serialize.module.active.entity.SaBillEntity;
import com.xm.comment_serialize.module.active.entity.SaCashOutRecordEntity;
import com.xm.comment_serialize.module.cron.entity.ScBillPayEntity;
import lombok.Data;

/**
 *  活动付款消息
 */
@Data
public class ActiveEntPayMessage {
    private String retryTradeNo;
    private String desc;
    private String ip;
    private Integer userId;
    private SaCashOutRecordEntity saCashOutRecordEntity;
}
