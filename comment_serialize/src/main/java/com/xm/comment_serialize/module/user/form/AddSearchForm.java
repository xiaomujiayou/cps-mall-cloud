package com.xm.comment_serialize.module.user.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddSearchForm {
    @NotEmpty(message = "keyWords不能为空")
    private String keyWords;
}
