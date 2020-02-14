package com.xm.comment_feign.module.cron.feign;

import com.xm.comment_feign.module.cron.feign.fallback.CronFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "cron-service",fallback = CronFeignClientFallBack.class)
public interface CronFeignClient {

}
