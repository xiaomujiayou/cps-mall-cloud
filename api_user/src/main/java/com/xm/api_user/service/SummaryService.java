package com.xm.api_user.service;


import com.xm.comment_serialize.module.user.entity.SuSummaryEntity;

public interface SummaryService {

    /**
     * 获取用户的数据汇总信息
     * 没有则创建
     * @param userId
     * @return
     */
    public SuSummaryEntity getUserSummary(Integer userId);

    /**
     * 更新用户汇总数据
     * @param suSummaryEntity
     */
    public void updateSummary(SuSummaryEntity suSummaryEntity);

    /**
     * 创建用户汇总信息
     * @param userId
     * @return
     */
    public SuSummaryEntity createNewSummary(Integer userId);
}
