package com.xm.cron_service.task;

import com.xm.cron_service.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 同步唯品会订单
 */
@Slf4j
@Component
public class WphOrderSyncTask {

    @Resource(name = "wphTaskService")
    private TaskService wphTaskService;

    @Scheduled(cron = "15/45 * * * * ?")
    public void dowork() {
        wphTaskService.start();
    }
}
