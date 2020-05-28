package com.xm.api_activite.message.handler;

import com.xm.api_activite.mapper.SaActiveMapper;
import com.xm.api_activite.mapper.SaBillMapper;
import com.xm.api_activite.service.BillService;
import com.xm.comment_mq.handler.MessageHandler;
import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.impl.UserAddProxyMessage;
import com.xm.comment_serialize.module.active.constant.ActiveConstant;
import com.xm.comment_serialize.module.active.entity.SaActiveEntity;
import com.xm.comment_serialize.module.active.entity.SaBillEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ShareActiveMessageHandler implements MessageHandler {

    @Autowired
    private BillService billService;
    @Autowired
    private SaActiveMapper saActiveMapper;

    @Override
    public List<Class> getType() {
        return Arrays.asList(UserAddProxyMessage.class);
    }

    @Override
    public void handle(AbsUserActionMessage message) {
        if(message instanceof UserAddProxyMessage){
            UserAddProxyMessage userAddProxyMessage = (UserAddProxyMessage)message;
            if(userAddProxyMessage.getLevel() > 1)
                return;
            SaActiveEntity shareActive = saActiveMapper.selectByPrimaryKey(ActiveConstant.SHARE_ACTIVE_ID);
            if(shareActive == null || shareActive.getState() != 1)
                return;
            billService.createBill(
                    userAddProxyMessage.getUserId(),
                    ActiveConstant.SHARE_ACTIVE_ID,
                    1,
                    shareActive.getMoney(),
                    1,
                    userAddProxyMessage.getProxyUser().getId().toString(),
                    userAddProxyMessage.getProxyUser().getNickname(),
                    null);
        }
    }

    @Override
    public void onError(Exception e) {

    }
}
