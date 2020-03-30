package com.xm.comment_serialize.module.mall.form;

import com.xm.comment_serialize.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UrlParseForm extends BaseForm {
    @NotNull(message = "url 不能为空")
    private String url;
}
