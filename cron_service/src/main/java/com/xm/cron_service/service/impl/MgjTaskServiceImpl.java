package com.xm.cron_service.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mogujie.openapi.exceptions.ApiException;
import com.mogujie.openapi.response.MgjResponse;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddDdkOrderListIncrementGetRequest;
import com.pdd.pop.sdk.http.api.request.PddTimeGetRequest;
import com.pdd.pop.sdk.http.api.response.PddDdkOrderListIncrementGetResponse;
import com.pdd.pop.sdk.http.api.response.PddTimeGetResponse;
import com.xm.comment_api.client.MyMogujieClient;
import com.xm.comment_api.module.mgj.OrderInfoQueryBean;
import com.xm.comment_api.module.mgj.XiaoDianCpsdataOrderListGetRequest;
import com.xm.comment_api.module.mgj.XiaoDianCpsdataOrderListGetResponse;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.user.bo.OrderCustomParameters;
import com.xm.comment_serialize.module.user.constant.OrderStateConstant;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.number.MathUtils;
import com.xm.comment_utils.number.NumberUtils;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.cron_service.service.TaskService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("mgjTaskService")
public class MgjTaskServiceImpl implements TaskService {

    @Autowired
    private MyMogujieClient myMogujieClient;

    @Override
    public PageBean<SuOrderEntity> getOrderByIncrement(Date startUpdateTime, Date endUpdateTime, Integer pageNum, Integer pageSize) throws Exception {
        Integer startTime = Integer.valueOf(DateUtil.format(startUpdateTime,"yyyyMMdd"));
        Integer endTime = Integer.valueOf(DateUtil.format(endUpdateTime,"yyyyMMdd"));
        OrderInfoQueryBean queryBean = new OrderInfoQueryBean();
        queryBean.setStart(startTime);
        queryBean.setEnd(endTime);
        queryBean.setPage(pageNum);
        queryBean.setPagesize(pageSize);
        MgjResponse<String> res = myMogujieClient.execute(new XiaoDianCpsdataOrderListGetRequest(queryBean));
        JSONObject jsonResult = JSON.parseObject(res.getResult().getData());
        JSONArray listItem = jsonResult.getJSONArray("orders");
        List<SuOrderEntity> orderEntities = null;
        if(listItem != null) {
            orderEntities = listItem.stream().map(o -> {
                return convertOrder((JSONObject) o);
            }).collect(Collectors.toList());
        }
        PageBean<SuOrderEntity> pageBean = new PageBean<>(orderEntities);
        pageBean.setList(orderEntities);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        pageBean.setTotal(jsonResult.getInteger("total"));
        return pageBean;
    }

    @Override
    public SuOrderEntity getOrderByNum(String orderNum) throws ApiException {
        OrderInfoQueryBean queryBean = new OrderInfoQueryBean();
        queryBean.setOrderNo(Long.valueOf(orderNum));
        try {
            MgjResponse<String> res = myMogujieClient.execute(new XiaoDianCpsdataOrderListGetRequest(queryBean));
            JSONObject jsonResult = JSON.parseObject(res.getResult().getData());
            JSONArray listItem = jsonResult.getJSONArray("orders");
            return convertOrder(listItem.getJSONObject(0));
        }catch (ApiException e){
            throw new GlobleException(MsgEnum.ORDER_INVALID_ERROR,"蘑菇街 无效单号：",orderNum);
        }
    }

    @Override
    public Date getTime() throws Exception {
        return null;
    }

    private SuOrderEntity convertOrder(JSONObject item){
        SuOrderEntity orderEntity = new SuOrderEntity();
        JSONObject goodsInfo = item.getJSONArray("products").getJSONObject(0);
        orderEntity.setOrderSn(item.getString("orderNo"));
        orderEntity.setProductId(goodsInfo.getString("productNo"));
        orderEntity.setProductName(goodsInfo.getString("name"));
        orderEntity.setImgUrl(goodsInfo.getString("productUrl"));
        orderEntity.setPlatformType(PlatformTypeConstant.MGJ);
        convertOrderState(item.getString("paymentStatus"),orderEntity);
        orderEntity.setPId(item.getString("groupId"));
        orderEntity.setOriginalPrice(new Double(goodsInfo.getDouble("price") * 100d).intValue());
        orderEntity.setQuantity(goodsInfo.getInteger("amount"));
        orderEntity.setAmount(orderEntity.getOriginalPrice());
        orderEntity.setPromotionRate(new Double(Double.valueOf(goodsInfo.getString("commission").replace("%",""))*100d).intValue());
        orderEntity.setPromotionAmount(new Double(item.getDouble("expense") * 100d).intValue());
        orderEntity.setType(0);
//        orderEntity.setCustomParameters(item.getString("feedback"));
        OrderCustomParameters orderCustomParameters = new OrderCustomParameters();
        orderCustomParameters.setPid(item.getString("groupId"));
        orderEntity.setCustomParameters(JSON.toJSONString(orderCustomParameters));
        orderEntity.setOrderModifyAt(new Date(item.getInteger("updateTime") * 1000));
        return orderEntity;
    }

    private void convertOrderState(String mgjOrderState,SuOrderEntity suOrderEntity){
        switch (mgjOrderState){
            case "10000":{
                suOrderEntity.setState(OrderStateConstant.UN_PAY);
                break;
            }
            case "20000":{
                suOrderEntity.setState(OrderStateConstant.PAY);
                break;
            }
            case "30000":{
                suOrderEntity.setState(OrderStateConstant.CHECK_FAIL);
                suOrderEntity.setFailReason("订单已退款");
                break;
            }
            case "40000":{
                suOrderEntity.setState(OrderStateConstant.CONFIRM_RECEIPT);
                break;
            }
            case "45000":{
                suOrderEntity.setState(OrderStateConstant.CHECK_SUCESS);
                break;
            }
            case "90000":{
                suOrderEntity.setState(OrderStateConstant.CHECK_FAIL);
                suOrderEntity.setFailReason("订单已取消");
                break;
            }
            case "95000":{
                suOrderEntity.setState(OrderStateConstant.CHECK_FAIL);
                suOrderEntity.setFailReason("订单被风控");
                break;
            }
        }
    }
}
