package com.xm.comment_serialize.module.user.form;

import com.xm.comment_serialize.form.AbsPageForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetOrderForm extends AbsPageForm {

    private Integer platformType;

    private Integer state;
    //订单类型(1:自购订单,2:分享订单)
    private Integer type;
}
