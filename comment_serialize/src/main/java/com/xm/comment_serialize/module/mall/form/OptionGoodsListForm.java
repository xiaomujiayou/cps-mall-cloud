package com.xm.comment_serialize.module.mall.form;

import com.xm.comment_serialize.form.BaseForm;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

/**
 * 商品列表表单
 */
@Data
public class OptionGoodsListForm extends GoodsListForm {
    @NotNull(message = "optionId 不能为空")
    private Integer optionId;
}
