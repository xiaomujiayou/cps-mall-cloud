package com.xm.comment_serialize.module.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddUserHistoryForm {
    @NotNull(message = "platformType不能为空")
    private Integer platformType;
    @NotNull(message = "goodsId不能为空")
    private String goodsId;
    private Integer shareUserId;
}
