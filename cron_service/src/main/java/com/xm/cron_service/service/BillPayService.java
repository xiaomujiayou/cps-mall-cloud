package com.xm.cron_service.service;

import com.xm.comment_serialize.module.cron.entity.ScBillPayEntity;
import com.xm.comment_serialize.module.cron.vo.ScBillPayVo;
import com.xm.comment_serialize.module.pay.entity.SpWxEntPayOrderInEntity;
import com.xm.comment_utils.mybatis.PageBean;

import java.util.Date;
import java.util.List;

public interface BillPayService {

    /**
     * 系统付款结果
     * 不保证付款成功
     * @param spWxEntPayOrderInEntity
     */
    public void onEntPayResult(SpWxEntPayOrderInEntity spWxEntPayOrderInEntity);

    /**
     * 佣金支付成功
     * @param scBillPayEntity
     * @param spWxEntPayOrderInEntity
     */
    public void onEntPaySucess(ScBillPayEntity scBillPayEntity,SpWxEntPayOrderInEntity spWxEntPayOrderInEntity);
    /**
     * 佣金支付失败
     * @param scBillPayEntity
     * @param spWxEntPayOrderInEntity
     */
    public void onEntPayFail(ScBillPayEntity scBillPayEntity,SpWxEntPayOrderInEntity spWxEntPayOrderInEntity);

    /**
     * 生成准备付款账单
     * @param pageNum
     * @param pageSize
     * @param timeline  订单截至时间线
     */
    public List<ScBillPayEntity> genPayBill(String billPayName,Integer minMoney,Integer pageNum, Integer pageSize, Date timeline);

    /**
     * 发放佣金
     */
    public void commission();

    /**
     * 打款记录
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageBean<ScBillPayVo> list(Integer userId, Integer pageNum, Integer pageSize);
}
