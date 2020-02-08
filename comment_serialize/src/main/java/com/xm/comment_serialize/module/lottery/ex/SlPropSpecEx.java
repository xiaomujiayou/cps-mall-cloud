package com.xm.comment_serialize.module.lottery.ex;

import com.xm.comment_serialize.module.lottery.entity.SlPropEntity;
import com.xm.comment_serialize.module.lottery.entity.SlPropSpecEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import lombok.Data;

@Data
public class SlPropSpecEx extends SlPropSpecEntity {
    private SuUserEntity suUserEntity;
    private String clientIp;
    private SlPropEntity slPropEntity;
}
