package com.xm.comment_serialize.module.active.form;

import com.xm.comment_serialize.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ActiveProfitForm extends BaseForm {
    private Integer activeId;
    private Integer state;
}
