package com.xm.comment_serialize.module.user.vo;

import com.xm.comment_serialize.module.mall.vo.SmProductVo;
import lombok.Data;

@Data
public class SuProductHistoryVo extends SmProductVo {
    private Integer itemId;
    private Integer shareUserId;
    private Integer productType;
}
