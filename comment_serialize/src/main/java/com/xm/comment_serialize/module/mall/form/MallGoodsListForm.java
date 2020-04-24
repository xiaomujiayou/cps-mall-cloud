package com.xm.comment_serialize.module.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MallGoodsListForm extends ListForm{
    @NotNull(message = "店铺ID")
    private String mallId;
}
