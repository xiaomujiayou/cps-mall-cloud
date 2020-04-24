package com.xm.comment_feign.module.wind.feign.fallback;

import com.xm.comment_feign.module.wind.feign.WindFeignClient;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WindFeignClientFallBack implements WindFeignClient {
    @Override
    public List<SmProductEntityEx> productCheck(List<SmProductEntityEx> smProductEntityExes) {
        throw new GlobleException(MsgEnum.SERVICE_AVAILABLE);
    }
}
