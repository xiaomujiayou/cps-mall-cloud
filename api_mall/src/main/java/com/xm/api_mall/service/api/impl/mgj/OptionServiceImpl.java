package com.xm.api_mall.service.api.impl.mgj;

import cn.hutool.core.util.ObjectUtil;
import com.xm.api_mall.mapper.SmOptMapper;
import com.xm.api_mall.service.ConfigService;
import com.xm.api_mall.service.api.OptionService;
import com.xm.comment_feign.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmOptEntity;
import com.xm.comment_serialize.module.mall.ex.OptEx;
import com.xm.comment_serialize.module.mall.form.OptionForm;
import com.xm.comment_serialize.module.user.constant.UserTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("mgjOptionService")
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
