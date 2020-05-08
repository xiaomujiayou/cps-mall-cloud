package com.xm.comment_serialize.module.cron.form;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BillPayForm {
    private Integer userId;
    private Integer billId;
    private Integer state;
    private Date createStart;
    private Date createEnd;
}
