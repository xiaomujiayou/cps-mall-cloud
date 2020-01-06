package com.xm.cron_service.service.impl;

import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddDdkOrderListIncrementGetRequest;
import com.pdd.pop.sdk.http.api.request.PddTimeGetRequest;
import com.pdd.pop.sdk.http.api.response.PddDdkOrderListIncrementGetResponse;
import com.pdd.pop.sdk.http.api.response.PddTimeGetResponse;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.cron_service.service.TaskService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("pddTaskService")
public class PddTaskServiceImpl implements TaskService {

    @Autowired
    private PopHttpClient popHttpClient;

    @Override
    public PageBean<SuOrderEntity> getOrderByIncrement(Date startUpdateTime, Date endUpdateTime, Integer pageNum, Integer pageSize) throws Exception {
        PddDdkOrderListIncrementGetRequest request = new PddDdkOrderListIncrementGetRequest();
        request.setStartUpdateTime(startUpdateTime.getTime()/1000);
        request.setEndUpdateTime(endUpdateTime.getTime()/1000);
        request.setPage(pageNum);
        request.setPageSize(pageSize);
        request.setReturnCount(true);
        PddDdkOrderListIncrementGetResponse response = popHttpClient.syncInvoke(request);
        List<PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem> listItem = response.getOrderListGetResponse().getOrderList();
        List<SuOrderEntity> orderEntities = null;
        if(listItem != null) {
            orderEntities = listItem.stream().map(o -> {
                return convertOrder(o);
            }).collect(Collectors.toList());
        }
        PageBean<SuOrderEntity> pageBean = new PageBean<>(orderEntities);
        pageBean.setList(orderEntities);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        pageBean.setTotal(response.getOrderListGetResponse().getTotalCount());
        return pageBean;
    }

    @Override
    public Date getTime() throws Exception {
        PddTimeGetRequest request = new PddTimeGetRequest();
        PddTimeGetResponse response = popHttpClient.syncInvoke(request);
        return DateUtils.parseDate(response.getTimeGetResponse().getTime(),"yyyy-MM-dd HH:mm:ss");
    }

    private SuOrderEntity convertOrder(PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem item){
        SuOrderEntity orderEntity = new SuOrderEntity();
        orderEntity.setOrderSn(item.getOrderSn());
        orderEntity.setProductId(item.getGoodsId().toString());
        orderEntity.setProductName(item.getGoodsName());
        orderEntity.setImgUrl(item.getGoodsThumbnailUrl());
        orderEntity.setPlatformType(PlatformTypeConstant.PDD);
        orderEntity.setState(item.getOrderStatus());
        orderEntity.setFailReason(item.getFailReason());
        orderEntity.setPId(item.getPId());
        orderEntity.setOriginalPrice(item.getGoodsPrice().intValue());
        orderEntity.setQuantity(item.getGoodsQuantity().intValue());
        orderEntity.setAmount(item.getOrderAmount().intValue());
        orderEntity.setPromotionRate(item.getPromotionRate().intValue());
        orderEntity.setPromotionAmount(item.getPromotionAmount().intValue());
        orderEntity.setType(item.getType());
        orderEntity.setCustomParameters(item.getCustomParameters());
        orderEntity.setOrderModifyAt(new Date(item.getOrderModifyAt() * 1000));
        return orderEntity;
    }
}
