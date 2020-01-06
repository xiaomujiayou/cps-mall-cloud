package com.xm.comment_serialize.module.mall.form;

import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import lombok.Data;

import java.util.List;

@Data
public class CalcProfitForm {
    private Integer userId;
    private List<SmProductEntity> smProductEntities;
}
