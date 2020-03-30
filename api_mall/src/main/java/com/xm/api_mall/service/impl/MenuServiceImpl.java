package com.xm.api_mall.service.impl;

import com.xm.api_mall.mapper.SmMenuMapper;
import com.xm.api_mall.service.MenuService;
import com.xm.comment_serialize.form.BaseForm;
import com.xm.comment_serialize.module.mall.constant.MenuTypeContant;
import com.xm.comment_serialize.module.mall.entity.SmMenuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.List;

@Service("menuService")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private SmMenuMapper smMenuMapper;

    @Override
    public List<SmMenuEntity> mainTabs(BaseForm baseForm) {
        OrderByHelper.orderBy("sort desc");
        SmMenuEntity record = new SmMenuEntity();
        record.setAppType(baseForm.getAppType());
        record.setDisable(1);
        record.setType(MenuTypeContant.MAIN_TABS);
        return smMenuMapper.select(record);
    }

    @Override
    public List<SmMenuEntity> setting(BaseForm baseForm) {
        OrderByHelper.orderBy("sort desc");
        SmMenuEntity record = new SmMenuEntity();
        record.setAppType(baseForm.getAppType());
        record.setDisable(1);
        record.setType(MenuTypeContant.SETTING);
        return smMenuMapper.select(record);
    }
}
