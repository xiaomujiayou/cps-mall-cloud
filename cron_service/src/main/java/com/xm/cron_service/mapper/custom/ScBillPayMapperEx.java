package com.xm.cron_service.mapper.custom;

import com.xm.comment_serialize.module.cron.entity.ScBillPayEntity;
import com.xm.cron_service.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ScBillPayMapperEx extends MyMapper<ScBillPayEntity> {

    Integer totalGenPayBill(Integer minMoney,@Param("timeline") Date timeline);

    List<ScBillPayEntity> genPayBill(Integer minMoney,Integer start, Integer end, Date timeline);

}
