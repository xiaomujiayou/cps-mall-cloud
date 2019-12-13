package com.xm.comment_serialize.module.user.form;

import com.xm.comment_serialize.form.AbsPageForm;
import lombok.Data;

@Data
public class GetProxyProfitForm extends AbsPageForm {

    private Integer orderColumn;

    private Integer orderBy;
    //账单状态
    private Integer state;
}
