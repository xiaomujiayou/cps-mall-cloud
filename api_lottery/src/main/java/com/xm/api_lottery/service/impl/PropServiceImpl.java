package com.xm.api_lottery.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.sun.org.apache.bcel.internal.generic.FALOAD;
import com.xm.api_lottery.mapper.SlPropMapper;
import com.xm.api_lottery.mapper.SlPropSpecMapper;
import com.xm.api_lottery.mapper.SlUserPropMapMapper;
import com.xm.api_lottery.mapper.SlUserPropSpecMapMapper;
import com.xm.api_lottery.service.PropService;
import com.xm.comment_serialize.module.lottery.entity.SlPropEntity;
import com.xm.comment_serialize.module.lottery.entity.SlPropSpecEntity;
import com.xm.comment_serialize.module.lottery.entity.SlUserPropMapEntity;
import com.xm.comment_serialize.module.lottery.entity.SlUserPropSpecMapEntity;
import com.xm.comment_serialize.module.lottery.vo.SlPropVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("propService")
public class PropServiceImpl implements PropService {

    @Autowired
    private SlPropMapper slPropMapper;
    @Autowired
    private SlPropSpecMapper slPropSpecMapper;
    @Autowired
    private SlUserPropMapMapper slUserPropMapMapper;
    @Autowired
    private SlUserPropSpecMapMapper slUserPropSpecMapMapper;

    @Override
    public List<SlPropVo> getPropVo(Integer userId) {
        List<SlPropEntity> slPropEntities = slPropMapper.selectAll();
        System.out.println(slPropEntities);
        return slPropEntities.stream().map(o ->{
            SlPropSpecEntity record = new SlPropSpecEntity();
            record.setPropId(o.getId());
            List<SlPropSpecEntity> slPropSpecEntities = slPropSpecMapper.select(record);
            return toSlPropVo(userId,o,slPropSpecEntities);
        }).collect(Collectors.toList());
    }

    /**
     * 转换道具到vo
     * @param slPropEntity
     * @param slPropSpecEntities
     * @return
     */
    private SlPropVo toSlPropVo(Integer userId,SlPropEntity slPropEntity, List<SlPropSpecEntity> slPropSpecEntities) {
        SlPropVo slPropVo = new SlPropVo();
        slPropVo.setName(slPropEntity.getName());
        slPropVo.setPrice(slPropEntity.getPrice());
        slPropVo.setHas(hasProp(userId,slPropEntity));
        slPropVo.setSpecDes(slPropEntity.getSpecDes());
        slPropVo.setDes(slPropEntity.getDes());
        slPropVo.setImgUrl(slPropEntity.getImgUrl());
        List<SlPropVo.SpecBean> specBeans = slPropSpecEntities.stream().map(o ->{
            return toPropSpecVo(userId,o);
        }).collect(Collectors.toList());
        slPropVo.setSpec(specBeans);
        return slPropVo;
    }

    /**
     * 当前是否拥有该道具
     * @param userId
     * @param slPropEntity
     * @return
     */
    private boolean hasProp(Integer userId, SlPropEntity slPropEntity) {
        if(userId == null)
            return false;
        SlUserPropMapEntity slUserPropMapEntity = new SlUserPropMapEntity();
        slUserPropMapEntity.setUserId(userId);
        slUserPropMapEntity.setPropId(slPropEntity.getId());
        slUserPropMapEntity = slUserPropMapMapper.selectOne(slUserPropMapEntity);
        if(slUserPropMapEntity == null)
            return false;
        return slUserPropMapEntity.getPropUnitCurrent() > 0;
    }

    /**
     * 转换规格到vo
     * @param slPropSpecEntity
     * @return
     */
    private SlPropVo.SpecBean toPropSpecVo(Integer userId, SlPropSpecEntity slPropSpecEntity) {
        SlPropVo.SpecBean specBean = new SlPropVo.SpecBean();
        specBean.setId(slPropSpecEntity.getId());
        specBean.setName(slPropSpecEntity.getName());
        specBean.setPrice(slPropSpecEntity.getPrice());
        specBean.setOriginalPrice(slPropSpecEntity.getOriginalPrice());
        specBean.setDes(slPropSpecEntity.getDes());
        specBean.setChoose(slPropSpecEntity.getChoose()==1?true:false);
        specBean.setName(slPropSpecEntity.getName());
        specBean.setDisable(false);
        specBean.setDisableTips("");
        setDisable(userId,slPropSpecEntity,specBean);
        return specBean;
    }

    /**
     * 设置禁用属性
     * @param userId
     * @param slPropSpecEntity
     */
    private void setDisable(Integer userId, SlPropSpecEntity slPropSpecEntity,SlPropVo.SpecBean specBean) {
        if(userId == null){
            specBean.setDisable(false);
            return;
        }
        SlUserPropSpecMapEntity slUserPropSpecMapEntity = new SlUserPropSpecMapEntity();
        slUserPropSpecMapEntity.setUserId(userId);
        slUserPropSpecMapEntity.setPropSpecId(slPropSpecEntity.getId());
        PageHelper.startPage(1,1).count(false);
        OrderByHelper.orderBy("create_time desc");
        slUserPropSpecMapEntity = slUserPropSpecMapMapper.selectOne(slUserPropSpecMapEntity);
        if(slUserPropSpecMapEntity == null) {
            specBean.setDisable(false);
        }else {
            long day = DateUtil.betweenDay(slUserPropSpecMapEntity.getCreateTime(),new Date(),true);
            if(day <= 0){
                specBean.setDisable(true);
                specBean.setDisableTips("每天限购一次");
            }else {
                specBean.setDisable(false);
            }
        }
    }
}
