package com.xm.comment_serialize.module.mall.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProductListForm {
    //所属平台
    @NotNull(message = "platformType不能为空")
    private Integer platformType;
    //列表类型
    @NotNull(message = "listType不能为空")
    private Integer listType;
    //optionId
    private Integer optionId;
    //关键字
    private String keyword;
    //商品Id
    private String goodsId;

    private Integer pageNum = 1;
    private Integer pageSize = 20;


}
