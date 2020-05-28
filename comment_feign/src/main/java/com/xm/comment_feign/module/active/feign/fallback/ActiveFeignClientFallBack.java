package com.xm.comment_feign.module.active.feign.fallback;

import com.xm.comment_feign.module.active.feign.ActiveFeignClient;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActiveFeignClientFallBack implements ActiveFeignClient {
    @Override
    public List<SmProductEntityEx> goodsActiveInfo(Integer userId, List<SmProductEntityEx> productEntityExes) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }

    @Override
    public SmProductEntityEx goodsActiveInfo(Integer userId, SmProductEntityEx productEntityEx) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }
}
