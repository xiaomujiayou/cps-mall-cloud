package com.xm.api_user.message.handler;

import com.google.common.collect.Lists;
import com.xm.api_user.service.MenuTipService;
import com.xm.comment_mq.handler.MessageHandler;
import com.xm.comment_mq.message.AbsUserActionMessage;
import com.xm.comment_mq.message.impl.OrderCommissionSucessMessage;
import com.xm.comment_mq.message.impl.OrderCreateMessage;
import com.xm.comment_mq.message.impl.OrderSettlementSucessMessage;
import com.xm.comment_mq.message.impl.OrderStateChangeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 添加菜单提示
 * 修改系统菜单后需要同时修改此文件
 */
@Component
public class MenuTipsHandler implements MessageHandler {

    @Autowired
    private MenuTipService menuTipService;
    //页面“我的”id
    private static final Integer PARENT_MENU_MY = 11;

    @Override
    public List<Class> getType() {
        return Lists.newArrayList(OrderCreateMessage.class, OrderSettlementSucessMessage.class);
    }

    @Override
    public void handle(AbsUserActionMessage message) {
        //订单消息
        if(message instanceof OrderCreateMessage || message instanceof OrderSettlementSucessMessage){
            addMenuTipNum(message.getUserId(),16,PARENT_MENU_MY);
        }
        if(message instanceof OrderStateChangeMessage || message instanceof OrderCommissionSucessMessage){
            addMenuDot(message.getUserId(),16,PARENT_MENU_MY);
        }


    }

    private void addMenuTipNum(Integer userId,Integer menuId,Integer parentMenu){
        menuTipService.addNum(userId, Arrays.asList(menuId,parentMenu));
    }

    private void addMenuDot(Integer userId,Integer menuId,Integer parentMenu){
        menuTipService.addRedPoint(userId, Arrays.asList(menuId,parentMenu));
    }

    @Override
    public void onError(Exception e) {

    }
}
