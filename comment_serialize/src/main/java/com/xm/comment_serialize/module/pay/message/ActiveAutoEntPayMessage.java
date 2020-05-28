package com.xm.comment_serialize.module.pay.message;

import com.xm.comment_serialize.module.active.entity.SaBillEntity;
import com.xm.comment_serialize.module.active.entity.SaCashOutRecordEntity;
import lombok.Data;

@Data
public class ActiveAutoEntPayMessage {
    private String retryTradeNo;
    private String desc;
    private String ip;
    private Integer userId;
    private SaBillEntity saBillEntity;
}
