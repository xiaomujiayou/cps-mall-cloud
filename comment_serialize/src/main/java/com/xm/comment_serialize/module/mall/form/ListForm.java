package com.xm.comment_serialize.module.mall.form;

import com.xm.comment_serialize.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ListForm extends BaseForm {
    private Integer pageNum;
    private Integer pageSize;
}
