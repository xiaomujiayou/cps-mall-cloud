package com.xm.api_user.service;

import com.xm.comment_serialize.module.user.entity.SuSearchEntity;
import com.xm.comment_utils.mybatis.PageBean;

public interface SearchService {

    public PageBean<SuSearchEntity> get(Integer userId, Integer pageNum, Integer pageSize);

    public void add(Integer userId,String keyword);

    public void deleteAll(Integer userId);
}
