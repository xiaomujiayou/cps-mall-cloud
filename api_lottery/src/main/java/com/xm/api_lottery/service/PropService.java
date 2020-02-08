package com.xm.api_lottery.service;

import com.xm.comment_serialize.module.lottery.vo.SlPropVo;

import java.util.List;

/**
 * 道具服务
 */
public interface PropService {

    /**
     * 获取道具列表
     * @param userId
     * @return
     */
    public List<SlPropVo> getPropVo(Integer userId);

}
