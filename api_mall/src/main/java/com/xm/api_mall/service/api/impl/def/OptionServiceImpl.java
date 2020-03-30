package com.xm.api_mall.service.api.impl.def;

import com.xm.api_mall.service.api.OptionService;
import com.xm.comment_serialize.module.mall.entity.SmOptEntity;
import com.xm.comment_serialize.module.mall.ex.OptEx;
import com.xm.comment_serialize.module.mall.form.OptionForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("optionService")
public class OptionServiceImpl implements OptionService {
    @Override
    public List<OptEx> list(OptionForm optionForm) {
        return null;
    }

    @Override
    public List<SmOptEntity> childList(OptionForm optionForm) {
        return null;
    }

    @Override
    public List<SmOptEntity> allParentList(OptionForm optionForm) {
        return null;
    }

    @Override
    public boolean check(String optId) {
        return false;
    }
}
