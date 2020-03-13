package com.xm.comment_serialize.module.pay.message;

import com.xm.comment_serialize.module.cron.entity.ScBillPayEntity;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import lombok.Data;

import java.util.List;

/**
 *  企业付款消息
 */
@Data
public class EntPayMessage {
    private String retryTradeNo;
    private String desc;
    private String ip;
    private ScBillPayEntity scBillPayEntity;
}
