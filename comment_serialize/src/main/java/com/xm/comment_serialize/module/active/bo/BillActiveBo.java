package com.xm.comment_serialize.module.active.bo;

import com.xm.comment_serialize.module.active.entity.SaActiveEntity;
import com.xm.comment_serialize.module.active.entity.SaBillEntity;
import lombok.Data;

@Data
public class BillActiveBo extends SaBillEntity {
    private SaActiveEntity saActiveEntity;
}
