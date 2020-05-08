package com.xm.comment_serialize.module.user.form;

import com.xm.comment_serialize.module.mall.form.ListForm;
import lombok.Data;

import java.util.Date;

@Data
public class UserSearchForm extends ListForm {
    private Integer userId;
    private Integer parentId;
    private String userSn;
    private Integer pId;
    private Date createStart;
    private Date createEnd;
    private Date lastStart;
    private Date lastEnd;
}
