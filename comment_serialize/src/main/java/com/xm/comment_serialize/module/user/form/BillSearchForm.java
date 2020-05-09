package com.xm.comment_serialize.module.user.form;

import com.xm.comment_serialize.module.mall.form.ListForm;
import lombok.Data;

import java.util.Date;

@Data
public class BillSearchForm extends ListForm {
    private Integer id;
    private Integer userId;
    private String billSn;
    private Integer fromUserId;
    private Integer type;
    private Integer attach;
    private Integer state;
    private Integer creditState;
    private Date payTimeStart;
    private Date payTimeEnd;
    private Date createStart;
    private Date createEnd;
}
