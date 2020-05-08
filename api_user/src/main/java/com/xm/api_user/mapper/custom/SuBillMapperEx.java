package com.xm.api_user.mapper.custom;

import com.xm.api_user.utils.MyMapper;
import com.xm.comment_serialize.module.user.dto.BillOrderDto;
import com.xm.comment_serialize.module.user.dto.OrderBillDto;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuRoleEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import com.xm.comment_serialize.module.user.form.BillProfitSearchForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuBillMapperEx extends MyMapper<SuBillEntity> {

    /**
     * 获取订单相关
     * @param userId
     * @param type
     * @param platformType
     * @param state
     * @return
     */
    List<OrderBillDto> getOrderBill(Integer userId,Integer type,Integer platformType,Integer state);

    /**
     * 获取账单相关
     * @param userId
     * @return
     */
    List<BillOrderDto> getBillInfo(Integer userId, List<String> billIds);

    /**
     * 获取相关账单金额
     * @param billProfitSearchForm
     * @return
     */
    Integer getBillProfit(BillProfitSearchForm billProfitSearchForm);

}
