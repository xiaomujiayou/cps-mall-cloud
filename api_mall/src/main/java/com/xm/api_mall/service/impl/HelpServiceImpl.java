package com.xm.api_mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.xm.api_mall.mapper.SmHelpMapper;
import com.xm.api_mall.service.HelpService;
import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmConfigEntity;
import com.xm.comment_serialize.module.mall.entity.SmHelpEntity;
import com.xm.comment_serialize.module.mall.vo.HelpVo;
import com.xm.comment_serialize.module.user.entity.SuConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service("helpService")
public class HelpServiceImpl implements HelpService {

    @Autowired
    private MallFeignClient mallFeignClient;
    @Autowired
    private SmHelpMapper smHelpMapper;

    @Override
    public SmHelpEntity getHelp(Integer userId, String url) {
        boolean helpConfig = false;
        SmConfigEntity configEntity = mallFeignClient.getOneConfig(userId, ConfigEnmu.PAGE_HELP_STATE.getName(), ConfigTypeConstant.SELF_CONFIG).getData();
        if(configEntity == null)
            return null;
        helpConfig = configEntity.getVal().equals("1");

        if(!helpConfig)
            return null;

        PageHelper.startPage(1,1).count(false);
        Example example = new Example(SmHelpEntity.class);
        example.createCriteria().andLike("url","%" + url + "%")
                .andEqualTo("state",1);
        SmHelpEntity smHelpEntity = smHelpMapper.selectOneByExample(example);
        return smHelpEntity;
    }
}
