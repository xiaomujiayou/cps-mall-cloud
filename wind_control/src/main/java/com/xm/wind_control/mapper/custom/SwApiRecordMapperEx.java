package com.xm.wind_control.mapper.custom;

import com.xm.comment_serialize.module.wind.entity.SwApiRecordEntity;
import com.xm.comment_serialize.module.wind.form.DelayForm;
import com.xm.wind_control.utils.MyMapper;

public interface SwApiRecordMapperEx extends MyMapper<SwApiRecordEntity> {

    /**
     * 获取api平均延时
     * @param delayForm
     * @return
     */
    Integer getApiAverage(DelayForm delayForm);
}
