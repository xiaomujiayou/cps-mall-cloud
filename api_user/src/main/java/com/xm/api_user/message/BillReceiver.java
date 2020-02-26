package com.xm.api_user.message;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xm.api_user.service.BillService;
import com.xm.comment_mq.message.config.BillMqConfig;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BillReceiver {

    @Autowired
    private BillService billService;

    /**
     * 有效账单已生成，发放佣金（用户收款）
     * @param suBillEntity
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(BillMqConfig.EXCHANGE),
            key = BillMqConfig.KEY,
            value = @Queue(BillMqConfig.QUEUE)
    ))
    public void onSucessMessage(SuBillEntity suBillEntity, Channel channel, Message message) throws IOException {
        log.debug("发放佣金");
        log.debug(JSON.toJSONString(suBillEntity));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }


    /**
     * 账单支付成功队列（用户付款）
     * @param suBillEntity
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(BillMqConfig.EXCHANGE),
            key = BillMqConfig.KEY_PAY_SUCESS,
            value = @Queue(BillMqConfig.QUEUE_PAY_SUCESS)
    ))
    public void onPaySucessMessage(SuBillEntity suBillEntity, Channel channel, Message message) throws IOException {
        log.debug("账单支付成功");
        billService.paySucess(suBillEntity);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

    /**
     * 微信支付超时账单
     * @param suBillEntity
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(BillMqConfig.EXCHANGE),
            key = BillMqConfig.KEY_PAY_OVERTIME_DEAD,
            value = @Queue(value = BillMqConfig.QUEUE_PAY_OVERTIME_DEAD)
    ))
    public void onOvertimeMessage(SuBillEntity suBillEntity, Channel channel, Message message) throws IOException {
        log.debug("账单超时");
        billService.payOvertime(suBillEntity);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
