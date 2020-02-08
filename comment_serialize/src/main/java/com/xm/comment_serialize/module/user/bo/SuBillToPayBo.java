package com.xm.comment_serialize.module.user.bo;

import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import lombok.Data;

@Data
public class SuBillToPayBo extends SuBillEntity {
    private String openId;
    //客户端ip
    private String clientIp;
}
