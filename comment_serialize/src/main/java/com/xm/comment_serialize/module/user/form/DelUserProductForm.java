package com.xm.comment_serialize.module.user.form;

import com.xm.comment_serialize.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DelUserProductForm extends BaseForm {
    @NotNull(message = "id 不能为空")
    private Integer id;
    @NotNull(message = "productType 不能为空")
    private Integer productType;
}
