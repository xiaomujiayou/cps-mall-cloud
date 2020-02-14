package com.xm.api_user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.xm.api_user.mapper.SuProductMapper;
import com.xm.api_user.service.ProductService;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_feign.module.mall.feign.MallFeignClient;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.user.entity.SuProductEntity;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private SuProductMapper suProductMapper;
    @Autowired
    private MallFeignClient mallFeignClient;

    @Override
    public PageBean<SmProductEntity> getUserProduct(Integer userId, Integer pageNum, Integer pageSize, Integer suProductType) {
        SuProductEntity suProductEntity = new SuProductEntity();
        suProductEntity.setUserId(userId);
        suProductEntity.setType(suProductType);
        suProductEntity.setDel(1);
        PageHelper.startPage(pageNum,pageSize);
        OrderByHelper.orderBy("create_time desc");
        List<SuProductEntity> suProductEntities = suProductMapper.select(suProductEntity);
        Map<String, java.util.Date> timeMap = suProductEntities.stream().collect(Collectors.toMap(SuProductEntity::getGoodsId,SuProductEntity::getCreateTime));
        Map<String, Integer> idMap = suProductEntities.stream().collect(Collectors.toMap(SuProductEntity::getGoodsId,SuProductEntity::getId));
        Map<String, Integer> shareUserIdMap = suProductEntities.stream().filter(o->o.getShareUserId() != null).collect(Collectors.toMap(SuProductEntity::getGoodsId,SuProductEntity::getShareUserId));
        Map<Integer,List<SuProductEntity>> groups = suProductEntities.stream().collect(Collectors.groupingBy(SuProductEntity::getPlatformType));
        List<SmProductEntity> smProductEntities = groups.entrySet().stream().map(o->{
            List<String> goodsIds = o.getValue().stream().map(SuProductEntity::getGoodsId).collect(Collectors.toList());
            return mallFeignClient.getProductDetails(o.getKey(),goodsIds);
        }).flatMap(o-> o.stream().map(i->{
            i.setId(idMap.get(i.getGoodsId()));
            return i;
        })).collect(Collectors.toList());
        smProductEntities = smProductEntities.stream().sorted(Comparator.comparing(o->timeMap.get(o.getGoodsId()))).collect(Collectors.toList());
        Collections.reverse(smProductEntities);

        List<SmProductEntityEx> smProductEntityExes = smProductEntities.stream().map(o->{
            SmProductEntityEx smProductEntityEx = new SmProductEntityEx();
            BeanUtil.copyProperties(o,smProductEntityEx);
            smProductEntityEx.setShareUserId(shareUserIdMap.get(o.getGoodsId()));
            return smProductEntityEx;
        }).collect(Collectors.toList());

        PageBean pageBean = new PageBean(suProductEntities);
        pageBean.setList(smProductEntityExes);
        return pageBean;
    }

    @Override
    public void addHistory(Integer userId, Integer platformType, String goodsId,Integer shareUserId) {
        SuProductEntity suProductEntity = new SuProductEntity();
        suProductEntity.setUserId(userId);
        suProductEntity.setGoodsId(goodsId);
        suProductEntity.setPlatformType(platformType);
        suProductEntity.setType(2);
        suProductMapper.delete(suProductEntity);
        suProductEntity.setShareUserId(shareUserId);
        suProductEntity.setDel(1);
        suProductEntity.setCreateTime(new Date(System.currentTimeMillis()));
        suProductMapper.insert(suProductEntity);
    }

    @Override
    public void delHistory(Integer userId, Integer id, Boolean isAll) {
        Example example = new Example(SuProductEntity.class);
        SuProductEntity suProductEntity = new SuProductEntity();
        if(isAll){
            example.createCriteria()
                    .andEqualTo("userId",userId)
                    .andEqualTo("type",2);
            suProductEntity.setDel(0);
            suProductMapper.updateByExampleSelective(suProductEntity,example);
            return;
        }else {
            if(id == null)
                throw new GlobleException(MsgEnum.PARAM_VALID_ERROR,"id不能为空");
            example.createCriteria()
                    .andEqualTo("userId",userId)
                    .andEqualTo("id",id);
            suProductEntity.setDel(0);
            suProductMapper.updateByExampleSelective(suProductEntity,example);
        }
    }

    @Override
    public boolean isCollect(Integer userId, Integer platformType, String goodsId) {
        SuProductEntity suProductEntity = new SuProductEntity();
        suProductEntity.setUserId(userId);
        suProductEntity.setType(1);
        suProductEntity.setPlatformType(platformType);
        suProductEntity.setGoodsId(goodsId);
        suProductEntity.setDel(1);
        int count = suProductMapper.selectCount(suProductEntity);
        return count > 0?true:false;
    }

    @Override
    public void collect(Integer userId, Integer platformType, String goodsId,Integer shareUserId, Boolean isCollect) {
        SuProductEntity suProductEntity = new SuProductEntity();
        suProductEntity.setUserId(userId);
        suProductEntity.setType(1);
        suProductEntity.setPlatformType(platformType);
        suProductEntity.setGoodsId(goodsId);
        if(isCollect){
            suProductMapper.delete(suProductEntity);
            suProductEntity.setShareUserId(shareUserId);
            suProductEntity.setDel(1);
            suProductEntity.setCreateTime(new Date(System.currentTimeMillis()));
            suProductMapper.insertSelective(suProductEntity);
            return;
        }else {
            suProductEntity = suProductMapper.selectOne(suProductEntity);
            suProductEntity.setDel(0);
            suProductMapper.updateByPrimaryKeySelective(suProductEntity);
        }
    }
}
