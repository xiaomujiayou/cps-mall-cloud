package com.xm.api_mall.service.impl;

import com.xm.api_mall.mapper.SmOptMapper;
import com.xm.api_mall.service.ConfigService;
import com.xm.api_mall.service.OptionService;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmOptEntity;
import com.xm.comment_serialize.module.mall.ex.OptEx;
import com.xm.comment_serialize.module.user.constant.UserTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Service("optionService")
public class OptionServiceImpl implements OptionService {

    @Autowired
    private SmOptMapper smOptMapper;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public List<OptEx> getOption(){
        OrderByHelper.orderBy("sort asc");
        SmOptEntity criteria = new SmOptEntity();
        criteria.setLevel(1);
        List<SmOptEntity> smOptEntities = smOptMapper.select(criteria);
        List<OptEx> optExes = new ArrayList<>();
        for (SmOptEntity smOptEntity : smOptEntities) {
            OrderByHelper.orderBy("sort asc");
            SmOptEntity record = new SmOptEntity();
            record.setParentId(smOptEntity.getId());
            record.setLevel(2);
            List<SmOptEntity> childOpts = smOptMapper.select(record);
            OptEx optEx = new OptEx();
            BeanUtils.copyProperties(smOptEntity,optEx);
            optEx.setChilds(childOpts.stream().map(item ->{
                OptEx childOptEx = new OptEx();
                BeanUtils.copyProperties(smOptEntity,childOptEx);
                return childOptEx;
            }).collect(Collectors.toList()));
            optExes.add(optEx);
        }
        return optExes;
    }

    @Override
    public List<SmOptEntity> getChildOption(Integer userId,Integer parentId) {
        SuUserEntity suUserEntity = null;
        if(userId != null)
            suUserEntity = userFeignClient.superUser(userId, UserTypeConstant.SELF).getData();
        List<SmOptEntity> smOptEntities = null;
        Example example = new Example(SmOptEntity.class);
        if(parentId == null || parentId == 0){
            example.createCriteria().andEqualTo("level",1);
            smOptEntities = smOptMapper.selectByExample(example);

            String sortStr = null;
            if(suUserEntity == null || suUserEntity.getSex() == 0){
                sortStr = configService.getConfig(userId, ConfigEnmu.MAIN_OPTION_SORT, ConfigTypeConstant.PROXY_CONFIG).getVal();
            }else {
                sortStr = configService.getConfig(userId, suUserEntity.getSex() == 1?ConfigEnmu.MAIN_OPTION_SORT_MAN:ConfigEnmu.MAIN_OPTION_SORT_WOMAN, ConfigTypeConstant.PROXY_CONFIG).getVal();
            }

            List<Integer> sort = Arrays.asList(sortStr.split(",")).stream().map(o->{return Integer.valueOf(o);}).collect(Collectors.toList());
            List<SmOptEntity> sorted = new ArrayList<>();
            for (Integer id: sort) {
                SmOptEntity smOptEntity = smOptEntities.stream().filter(o->{return o.getId().equals(id);}).findFirst().orElse(null);
                if(smOptEntity != null){
                    sorted.add(smOptEntity);
                }
            }
            smOptEntities = sorted;
        }else{
            OrderByHelper.orderBy("sort asc");
            example.createCriteria().andEqualTo("parentId",parentId);
            smOptEntities = smOptMapper.selectByExample(example);
        }
        return smOptEntities;
    }
}
