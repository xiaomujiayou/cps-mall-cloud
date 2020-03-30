package com.xm.comment_serialize.module.mall.form;

import com.xm.comment_serialize.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 商品列表表单
 */
@Data
public class GoodsDetailForm extends BaseForm {
    @NotNull(message = "goodsId 不能为空")
    private String goodsId;
    private Integer shareUserId;
}
