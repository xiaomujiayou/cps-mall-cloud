package com.xm.comment_serialize.module.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProductHistoryDelForm {
    //历史记录Id
//    @NotNull(message = "id不能为空")
    private Integer id;
    //是否全部删除
    @NotNull(message = "isAll不能为空")
    private Boolean isAll;
}
