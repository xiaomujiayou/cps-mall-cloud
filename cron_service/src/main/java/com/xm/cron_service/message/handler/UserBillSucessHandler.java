package com.xm.cron_service.message.handler;

import cn.hutool.core.collection.CollUtil;
import com.xm.comment_mq.handler.MessageHandler;
import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.impl.UserBillStateChangeMessage;
import com.xm.comment_serialize.module.cron.entity.ScBillPayEntity;
import com.xm.comment_serialize.module.cron.entity.ScWaitPayBillEntity;
import com.xm.comment_serialize.module.user.constant.BillStateConstant;
import com.xm.comment_serialize.module.user.constant.BillTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.cron_service.mapper.ScBillPayMapper;
import com.xm.cron_service.mapper.ScWaitPayBillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 监听达到发放标准的账单
 * 只接受购买商品所产生的交易账单
 */
@Component
public class UserBillSucessHandler implements MessageHandler {

    @Autowired
    private ScWaitPayBillMapper scWaitPayBillMapper;

    @Override
    public List<Class> getType() {
        return CollUtil.newArrayList(
                UserBillStateChangeMessage.class
        );
    }

    @Override
    public void handle(AbsUserActionMessage message) {
        if(!(message instanceof UserBillStateChangeMessage))
            return;
        UserBillStateChangeMessage userBillStateChangeMessage = (UserBillStateChangeMessage)message;
        SuBillEntity bill = userBillStateChangeMessage.getOldBill();
        if(CollUtil.newArrayList(BillTypeConstant.BUY_NORMAL,BillTypeConstant.PROXY_PROFIT,BillTypeConstant.BUY_SHARE,BillTypeConstant.SHARE_PROFIT).contains(bill.getType()) && bill.getState() != BillStateConstant.WAIT || userBillStateChangeMessage.getNewState() != BillStateConstant.READY)
            return;
        ScWaitPayBillEntity scWaitPayBillEntity = new ScWaitPayBillEntity();
        scWaitPayBillEntity.setBillId(userBillStateChangeMessage.getOldBill().getId());
        if(scWaitPayBillMapper.selectCount(scWaitPayBillEntity) > 0)
            return;
        scWaitPayBillEntity.setMoney(userBillStateChangeMessage.getOldBill().getMoney());
        scWaitPayBillEntity.setUserId(userBillStateChangeMessage.getUserId());
        scWaitPayBillEntity.setOpenId(userBillStateChangeMessage.getSuUserEntity().getOpenId());
        scWaitPayBillEntity.setState(1);
        scWaitPayBillEntity.setCreateTime(new Date());
        scWaitPayBillMapper.insertSelective(scWaitPayBillEntity);
    }

    @Override
    public void onError(Exception e) {

    }
}
