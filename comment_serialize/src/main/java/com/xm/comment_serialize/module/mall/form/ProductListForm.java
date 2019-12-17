package com.xm.comment_serialize.module.mall.form;

import com.xm.comment_serialize.form.AbsPageForm;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProductListForm extends AbsPageForm {
    //所属平台
    @NotNull(message = "platformType不能为空")
    private Integer platformType;
    //列表类型
    @NotNull(message = "listType不能为空")
    private Integer listType;
    //optionId
    private Integer optionId;
    //关键字
    private String keywords;
    //排序
    // 0-综合排序;
    // 1-按佣金比率升序;
    // 2-按佣金比例降序;
    // 3-按价格升序;
    // 4-按价格降序;
    // 5-按销量升序;
    // 6-按销量降序;
    // 7-优惠券金额排序升序;
    // 8-优惠券金额排序降序;
    // 9-券后价升序排序;
    // 10-券后价降序排序;
    private Integer sort;
    //最高价
    private Integer minPrice;
    //最低价
    private Integer maxPrice;
    //商品Id
    private String goodsId;

}


