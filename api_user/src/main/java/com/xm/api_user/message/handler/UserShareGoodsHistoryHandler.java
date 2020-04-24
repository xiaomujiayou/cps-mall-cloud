package com.xm.api_user.message.handler;

import com.xm.api_user.service.ShareService;
import com.xm.comment_mq.handler.MessageHandler;
import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.impl.OrderCreateMessage;
import com.xm.comment_mq.message.impl.OrderStateChangeMessage;
import com.xm.comment_mq.message.impl.UserShareGoodsMessage;
import com.xm.comment_serialize.module.user.constant.OrderStateConstant;
import com.xm.comment_utils.lock.LockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * 用户分享商品记录
 */
@Component
public class UserShareGoodsHistoryHandler implements MessageHandler {

    @Autowired
    private ShareService shareService;
    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @Override
    public List<Class> getType() {
        return Arrays.asList(
                UserShareGoodsMessage.class,
                OrderCreateMessage.class,
                OrderStateChangeMessage.class
        );
    }

    @Override
    public void handle(AbsUserActionMessage message) {
        Lock lock = redisLockRegistry.obtain(UserShareGoodsHistoryHandler.class.getSimpleName()+":"+message.getUserId());
        LockUtil.lock(lock,()->{
            if(message instanceof UserShareGoodsMessage){
                UserShareGoodsMessage userShareGoodsMessage = (UserShareGoodsMessage)message;
                shareService.show(userShareGoodsMessage.getUserId(),userShareGoodsMessage.getSmProductEntityEx());
            }else if(message instanceof OrderCreateMessage){
                OrderCreateMessage orderCreateMessage = (OrderCreateMessage)message;
                shareService.buy(orderCreateMessage.getSuOrderEntity());
            }else if(message instanceof OrderStateChangeMessage){
                OrderStateChangeMessage orderStateChangeMessage = (OrderStateChangeMessage)message;
                if(orderStateChangeMessage.getNewState() == OrderStateConstant.FAIL_SETTLED){
                    shareService.buyFail(orderStateChangeMessage.getSuOrderEntity());
                }
            }
        });
    }

    @Override
    public void onError(Exception e) {

    }
}
