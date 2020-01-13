package com.xm.api_mall.service;

import com.xm.comment_serialize.module.mall.entity.SmOptEntity;
import com.xm.comment_serialize.module.mall.ex.OptEx;

import java.util.List;

public interface OptionService {
    /**
     * 获取所有option
     * @return
     */
    List<OptEx> getOption();

    /**
     * 获取子option
     * level为0的option排序依赖系统配置
     * @param parentOptId
     * @return
     */
    List<SmOptEntity> getChildOption(Integer userId,Integer parentOptId);

    List<SmOptEntity> getAllParentOption(Integer userId,Integer childOptId);

}
