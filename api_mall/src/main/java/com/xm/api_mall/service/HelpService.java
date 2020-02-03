package com.xm.api_mall.service;

import com.xm.comment_serialize.module.mall.entity.SmHelpEntity;

public interface HelpService {

    /**
     * 获取页面帮助信息
     * @param userId
     * @param url
     * @return
     */
    public SmHelpEntity getHelp(Integer userId,String url);
}
