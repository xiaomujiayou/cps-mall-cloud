package com.xm.comment_serialize.module.active.form;

import com.xm.comment_serialize.module.mall.form.ListForm;
import lombok.Data;

@Data
public class ActiveBillListForm extends ListForm {
    private Integer activeId;
    private Integer state;
}
