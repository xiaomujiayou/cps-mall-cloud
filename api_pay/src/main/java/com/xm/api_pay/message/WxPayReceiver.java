package com.xm.api_pay.message;

import com.rabbitmq.client.Channel;
import com.xm.api_pay.service.WxPayApiService;
import com.xm.comment_mq.constant.RabbitMqConstant;
import com.xm.comment_mq.message.config.PayMqConfig;
import com.xm.comment_serialize.module.pay.entity.SpWxOrderNotifyEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class WxPayReceiver {

    @Autowired
    private WxPayApiService wxPayApiService;

    /**
     * 微信支付回调
     * @param spWxOrderNotifyEntity
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(PayMqConfig.EXCHANGE),
            key = PayMqConfig.KEY_WX_NOTIFY,
            value = @Queue(value = PayMqConfig.QUEUE_WX_NOTIFY,
                    arguments = {
                            @Argument(name = RabbitMqConstant.DEAD_QUEUE_ARG_EXCHANGE_NAME,value = PayMqConfig.EXCHANGE),
                            @Argument(name = RabbitMqConstant.DEAD_QUEUE_ARG_KEY_NAME,value = PayMqConfig.KEY_WX_NOTIFY_FAIL)
                    })
    ))
    public void onNotifyMessage(SpWxOrderNotifyEntity spWxOrderNotifyEntity, Channel channel, Message message) throws IOException {
        Long msgId = message.getMessageProperties().getDeliveryTag();
        try{
            wxPayApiService.onPaySucess(spWxOrderNotifyEntity);
        }catch (Exception e){
            channel.basicReject(msgId,false);
            log.error("消息：{} 单号：{} 处理失败 error：{}",msgId,spWxOrderNotifyEntity.getOutTradeNo(),e);
        } finally {
            channel.basicAck(msgId,false);
        }
    }
}
