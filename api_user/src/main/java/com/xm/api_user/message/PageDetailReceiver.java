package com.xm.api_user.message;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PageDetailReceiver {

//    @RabbitListener(bindings = @QueueBinding(
//            exchange = @Exchange(MessageConfig.EXCHANGE_PDD),
//            key = MessageConfig.KEY_PAGE,
//            value = @Queue(MessageConfig.QUEUE_PDD_PAGE)
//    ))
//    public void onMessage(SpProductBo spProductBo){
//        log.debug(JSON.toJSONString(spProductBo));
//
//
//    }
}
