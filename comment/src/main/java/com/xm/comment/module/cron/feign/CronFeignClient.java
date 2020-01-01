package com.xm.comment.module.cron.feign;

import com.xm.comment.config.FeignConfiguration;
import com.xm.comment.module.cron.feign.fallback.CronFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "cron-service",fallback = CronFeignClientFallBack.class,configuration = FeignConfiguration.class)
public interface CronFeignClient {

}
