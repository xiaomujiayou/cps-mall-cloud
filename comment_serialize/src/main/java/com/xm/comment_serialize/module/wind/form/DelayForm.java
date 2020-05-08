package com.xm.comment_serialize.module.wind.form;

import com.xm.comment_serialize.module.mall.form.ListForm;
import lombok.Data;

import java.util.Date;

@Data
public class DelayForm extends ListForm {
    private String url;
    private String method;
    private Integer appType;
    private Integer appTypeExclude;
    private Date createStart;
    private Date createEnd;
}
