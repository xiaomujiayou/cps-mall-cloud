package com.xm.comment_feign.module.active.feign;

import com.xm.comment_feign.module.active.feign.fallback.ActiveFeignClientFallBack;
import com.xm.comment_feign.module.cron.feign.fallback.CronFeignClientFallBack;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "api-active",fallback = ActiveFeignClientFallBack.class)
public interface ActiveFeignClient {

    @PostMapping(value = "/active/goods/list",consumes = "application/json")
    public List<SmProductEntityEx> goodsActiveInfo(@RequestParam Integer userId,@RequestBody List<SmProductEntityEx> productEntityExes);

    @PostMapping(value = "/active/goods",consumes = "application/json")
    public SmProductEntityEx goodsActiveInfo(@RequestParam Integer userId,@RequestBody SmProductEntityEx productEntityEx);
}
