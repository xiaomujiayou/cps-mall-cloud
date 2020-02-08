package com.xm.comment_serialize.module.user.form;

import com.xm.comment_serialize.module.lottery.ex.SlPropSpecEx;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import lombok.Data;

@Data
public class WxPayForm {
    private SlPropSpecEx slPropSpecEx;
    private SuUserEntity suUserEntity;
}
