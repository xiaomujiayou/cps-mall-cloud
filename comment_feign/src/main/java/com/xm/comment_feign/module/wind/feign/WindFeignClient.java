package com.xm.comment_feign.module.wind.feign;

import com.xm.comment_feign.module.wind.feign.fallback.WindFeignClientFallBack;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(value = "wind-control",fallback = WindFeignClientFallBack.class)
public interface WindFeignClient {
    @PostMapping(value = "/credit/product/check",consumes = "application/json")
    public List<SmProductEntityEx> productCheck(@RequestBody List<SmProductEntityEx> smProductEntityExes);
}
