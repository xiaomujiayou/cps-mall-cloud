package com.xm.comment_serialize.module.user.form;

import lombok.Data;

import java.util.Date;

@Data
public class BillProfitSearchForm {
    private Integer state;
    private Date createStart;
    private Date createEnd;
}
