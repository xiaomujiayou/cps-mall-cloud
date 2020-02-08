package com.xm.api_user.service;

import com.xm.api_user.mapper.custom.SuBillMapperEx;
import com.xm.comment_serialize.module.lottery.ex.SlPropSpecEx;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
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

    /**
     * 创建账单
     * @param suBillEntity
     */
    public void addBill(SuBillEntity suBillEntity);

    /**
     * 修改账单状态
     * @param suBillEntity
     * @param newState
     */
    public void updateBillState(SuBillEntity suBillEntity,Integer newState,String failReason);

    public SuBillToPayBo createByProp(SlPropSpecEx suBillEntity);
}
