package com.xm.api_user.service;

import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_serialize.module.user.vo.BillVo;
import com.xm.comment_utils.mybatis.PageBean;

public interface BillService {

    /**
     * 通过订单创建订单收益账单
     * @param order
     */
    public void createByOrder(SuOrderEntity order);

    /**
     * 订单达到成功条件
     * @param order
     */
    public void payOutOrderBill(SuOrderEntity order);


    /**
     * 订单达到失败条件
     * @param order
     */
    public void invalidOrderBill(SuOrderEntity order);

    public PageBean<BillVo> getList(Integer userId, Integer state, Integer type, Integer pageNum, Integer pageSize);

}
