package com.xm.api_user.mapper.custom;

import com.xm.api_user.utils.MyMapper;
import com.xm.comment_serialize.module.user.dto.ProxyProfitDto;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SuOrderMapperEx extends MyMapper<SuOrderEntity> {

    /**
     * 获取历史省钱总额(所有订单)
     * @param userId
     * @return
     */
    Map<String, BigDecimal> getUserOrderAbout(Integer userId);

    /**
     * 获取分享订单总额
     * @param userId
     * @return
     */
    BigDecimal getUserShareOrderAbout(Integer userId);


    /**
     * 计算收益额度
     * @param userId
     * @return
     */
    BigDecimal getUserTotalCommission(Integer userId, Integer state, Date startTime,Date endTime);

    /**
     * 获取用户历史消费
     * @param userId
     * @return
     */
    BigDecimal getUserTotalConsumption(Integer userId);



}
