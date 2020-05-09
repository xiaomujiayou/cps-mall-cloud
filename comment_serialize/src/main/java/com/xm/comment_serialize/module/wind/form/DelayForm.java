package com.xm.comment_serialize.module.wind.form;

import com.xm.comment_serialize.module.mall.form.ListForm;
import lombok.Data;

import java.util.Date;

@Data
public class DelayForm extends ListForm {
    private Integer userId;
    private String url;
    private String method;
    private Integer appType;
    private String ipAddr;
    private Integer appTypeExclude;
    private Integer timeMin;
    private Integer timeMax;
    private Date createStart;
    private Date createEnd;
}
