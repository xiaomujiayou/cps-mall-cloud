package com.xm.comment_serialize.module.user.form;

import com.xm.comment_serialize.form.AbsPageForm;
import lombok.Data;

@Data
public class GetUserShareForm extends AbsPageForm {
    private Integer orderType;
    private Integer order;
}
