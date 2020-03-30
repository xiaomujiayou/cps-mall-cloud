package com.xm.api_mall.service;

import com.xm.comment_serialize.form.BaseForm;
import com.xm.comment_serialize.module.mall.entity.SmMenuEntity;

import java.util.List;

public interface MenuService {

    /**
     * 获取底部导航菜单
     * @param baseForm
     * @return
     */
    public List<SmMenuEntity> mainTabs(BaseForm baseForm);

    /**
     * 获取设置菜单
     * @param baseForm
     * @return
     */
    public List<SmMenuEntity> setting(BaseForm baseForm);
}
