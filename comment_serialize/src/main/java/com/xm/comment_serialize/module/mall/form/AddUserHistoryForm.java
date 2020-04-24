package com.xm.comment_serialize.module.mall.form;

import com.xm.comment_serialize.form.BaseForm;
import com.xm.comment_serialize.module.mall.vo.SmProductVo;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddUserHistoryForm extends BaseForm {
    @NotNull(message = "goodsId不能为空")
    private String goodsId;
    private Integer shareUserId;
    private SmProductVo smProductVo;
}
