package com.xm.comment_serialize.module.mall.vo;

import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import com.xm.comment_serialize.module.mall.entity.SmMenuEntity;
import lombok.Data;

@Data
public class MenuVo extends SmMenuEntity {
    private Boolean hot;
    private Integer num;
}
