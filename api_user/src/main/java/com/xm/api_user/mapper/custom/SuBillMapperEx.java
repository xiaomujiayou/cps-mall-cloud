package com.xm.api_user.mapper.custom;

import com.xm.api_user.utils.MyMapper;
import com.xm.comment_serialize.module.user.dto.OrderBillDto;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuRoleEntity;
import com.xm.comment_serialize.module.user.ex.RolePermissionEx;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuBillMapperEx extends MyMapper<SuBillEntity> {

    /**
     * 获取
     * @param userId
     * @param type
     * @param platformType
     * @param state
     * @return
     */
    List<OrderBillDto> getOrderBill(Integer userId,Integer type,Integer platformType,Integer state);

}
