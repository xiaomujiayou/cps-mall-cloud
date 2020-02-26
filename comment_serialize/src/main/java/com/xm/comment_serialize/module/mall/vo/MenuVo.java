package com.xm.comment_serialize.module.mall.vo;

import com.xm.comment_serialize.module.mall.entity.SmBannerEntity;
import lombok.Data;

@Data
public class MenuVo extends SmBannerEntity {
    private Boolean hot;
    private Integer num;
}
