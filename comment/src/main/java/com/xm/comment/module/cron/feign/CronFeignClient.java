package com.xm.comment.module.cron.feign;

import com.xm.comment.config.FeignConfiguration;
import com.xm.comment.module.cron.feign.fallback.CronFeignClientFallBack;
import com.xm.comment.response.Msg;
import com.xm.comment_serialize.module.user.entity.SuConfigEntity;
import com.xm.comment_serialize.module.user.entity.SuPidEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import com.xm.comment_serialize.module.user.form.GetUserInfoForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "cron-service",fallback = CronFeignClientFallBack.class,configuration = FeignConfiguration.class)
public interface CronFeignClient {

}
