package com.xm.comment_serialize.module.user.form;

import com.xm.comment_serialize.form.AbsPageForm;
import com.xm.comment_serialize.form.BaseForm;
import com.xm.comment_serialize.module.mall.form.GoodsListForm;
import com.xm.comment_serialize.module.mall.form.ListForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetOrderForm extends ListForm {
    private Integer state;
    //订单类型(1:自购订单,2:分享订单)
    private Integer type;
}
