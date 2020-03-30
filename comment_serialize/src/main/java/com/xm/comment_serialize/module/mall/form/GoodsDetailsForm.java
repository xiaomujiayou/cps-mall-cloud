package com.xm.comment_serialize.module.mall.form;

import com.xm.comment_serialize.form.BaseForm;
import lombok.Data;

import java.util.List;

@Data
public class GoodsDetailsForm extends BaseForm {
    private List<String> goodsIds;
}
