package com.xm.api_activite.mapper.custom;

import com.xm.api_activite.utils.MyMapper;
import com.xm.comment_serialize.module.active.bo.BillActiveBo;
import com.xm.comment_serialize.module.active.entity.SaBillEntity;

import java.util.List;

public interface SaBillMapperEx extends MyMapper<SaBillEntity> {

    /**
     * 获取总收益
     * @return
     */
    public Integer totalProfit(SaBillEntity saBillEntity);

    /**
     * 获取活动收益列表
     * @param saBillEntity
     * @return
     */
    public List<BillActiveBo> getList(SaBillEntity saBillEntity);
}
