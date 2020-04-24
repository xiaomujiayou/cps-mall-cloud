package com.xm.comment_serialize.module.wind.vo;

import com.xm.comment_serialize.module.wind.entity.SwCreditBillConfEntity;
import lombok.Data;

import java.util.List;

@Data
public class UserCreditVo {
    private Integer scores;
    private SwCreditBillConfEntity swCreditBillConfEntity;
    private String desc;
    private List<String> getCredit;
    private Integer bindCount;
    private Integer bindTotalMoney;
}
