package com.xm.api_user.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.xm.api_user.mapper.SuSearchMapper;
import com.xm.api_user.mapper.SuShareMapper;
import com.xm.api_user.service.ShareService;
import com.xm.comment.module.mall.feign.MallFeignClient;
import com.xm.comment.utils.GoodsPriceUtil;
import com.xm.comment_serialize.module.mall.bo.ProductIndexBo;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_serialize.module.user.entity.SuShareEntity;
import com.xm.comment_serialize.module.user.vo.ShareVo;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("shareService")
public class ShareServiceImpl implements ShareService {

    @Autowired
    private SuShareMapper suShareMapper;
    @Autowired
    private MallFeignClient mallFeignClient;

    @Override
    public void shareOne(Integer userId, String goodsId, Integer platformType) {
        SuShareEntity example = new SuShareEntity();
        example.setUserId(userId);
        example.setGoodsId(goodsId);
        example.setPlatformType(platformType);

        example = suShareMapper.selectOne(example);
        if(example == null){
            //创建新的分享
            example = new SuShareEntity();
            example.setUserId(userId);
            example.setGoodsId(goodsId);
            example.setPlatformType(platformType);
            example.setSell(0);
            example.setWatch(1);
            example.setDel(1);
            example.setCreateTime(new Date());
            example.setUpdateTime(new Date());
            suShareMapper.insertSelective(example);
        }
//        else {
//            //更新状态
//            example.setDel(1);
//            example.setUpdateTime(new Date());
//            suShareMapper.updateByPrimaryKeySelective(example);
//        }

    }

    @Async("myExecutor")
    @Override
    public void show(Integer userId, String goodsId, Integer platformType) {
        SuShareEntity example = new SuShareEntity();
        example.setUserId(userId);
        example.setGoodsId(goodsId);
        example.setPlatformType(platformType);
        example = suShareMapper.selectOne(example);
        if(example != null && example.getId() != null){
            example.setWatch(example.getWatch() + 1);
            example.setUpdateTime(new Date());
            suShareMapper.updateByPrimaryKeySelective(example);
        }else {
            shareOne(userId,goodsId,platformType);
        }
    }

    @Async("myExecutor")
    @Override
    public void buy(SuOrderEntity suOrderEntity) {
        if(suOrderEntity.getShareUserId() == null)
            return;
        SuShareEntity example = new SuShareEntity();
        example.setUserId(suOrderEntity.getShareUserId());
        example.setGoodsId(suOrderEntity.getProductId());
        example.setPlatformType(suOrderEntity.getPlatformType());
        example = suShareMapper.selectOne(example);
        if(example != null && example.getId() != null){
            String userShareRate = mallFeignClient.getOneConfig(suOrderEntity.getShareUserId(), ConfigEnmu.PRODUCT_SHARE_USER_RATE.getName(),ConfigTypeConstant.SELF_CONFIG).getData().getVal();
            Integer willGetMoney = GoodsPriceUtil.type(suOrderEntity.getPlatformType()).calcUserShareProfit(Double.valueOf(suOrderEntity.getPromotionAmount()),Double.valueOf(userShareRate)).intValue();
            example.setWillMakeMoney(example.getWillMakeMoney() + willGetMoney);
            example.setSell(example.getSell() + 1);
            example.setUpdateTime(new Date());
            suShareMapper.updateByPrimaryKeySelective(example);
        }
    }

    @Override
    public void buyFail(SuOrderEntity suOrderEntity) {

    }

    @Override
    public PageBean<ShareVo> getList(Integer userId, Integer orderType, Integer order, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        StringBuilder sb = new StringBuilder();
        if (order != 0) {
            switch (orderType){
                case 1:{
                    sb.append("watch ");
                    break;
                }
                case 2:{
                    sb.append("sell ");
                    break;
                }
                case 3:{
                    sb.append("will_make_money ");
                    break;
                }
                case 4:{
                    sb.append("create_time ");
                    break;
                }
            }
            if(order == 1) {
                sb.append("asc");
            }else {
                sb.append("desc");
            }
        }else {
            sb.append("update_time desc");
        }
        OrderByHelper.orderBy(sb.toString());
        SuShareEntity example = new SuShareEntity();
        example.setUserId(userId);
        example.setDel(1);
        List<SuShareEntity> suShareEntities = suShareMapper.select(example);
        PageBean pageBean = new PageBean(suShareEntities);
         String userBuyRate = mallFeignClient.getOneConfig(userId, ConfigEnmu.PRODUCT_BUY_RATE.getName(),ConfigTypeConstant.PROXY_CONFIG).getData().getVal();
        String userShareRate = mallFeignClient.getOneConfig(userId, ConfigEnmu.PRODUCT_SHARE_USER_RATE.getName(),ConfigTypeConstant.SELF_CONFIG).getData().getVal();
        List<SmProductEntity> smProductEntities = mallFeignClient.getProductDetails(suShareEntities.stream().map(o->{
            ProductIndexBo productIndexBo = new ProductIndexBo();
            productIndexBo.setGoodsId(o.getGoodsId());
            productIndexBo.setPlatformType(o.getPlatformType());
            return productIndexBo;
        }).collect(Collectors.toList())).getData();

        List<ShareVo> shareVos = suShareEntities.stream().map(o ->{
            SmProductEntity smProductEntity = smProductEntities.stream().filter(j->{return o.getGoodsId().equals(j.getGoodsId());}).findFirst().get();
            if(smProductEntity == null)
                return null;
            ShareVo shareVo = convertVo(o,smProductEntity,Double.valueOf(userBuyRate),Double.valueOf(userShareRate));
            return shareVo;
        }).collect(Collectors.toList());
        shareVos.remove(null);
        pageBean.setList(shareVos);
        return pageBean;
    }

    private ShareVo convertVo(SuShareEntity suShareEntity,SmProductEntity smProductEntity,Double userBuyRate,Double userShareRate){
        ShareVo shareVo = new ShareVo();
        shareVo.setId(suShareEntity.getId());
        shareVo.setGoodsId(smProductEntity.getGoodsId());
        shareVo.setPlatformType(suShareEntity.getPlatformType());
        shareVo.setGoodsImg(smProductEntity.getGoodsThumbnailUrl());
        shareVo.setTitle(smProductEntity.getName());
        shareVo.setOriginalPrice(smProductEntity.getOriginalPrice());
        shareVo.setCoupon(smProductEntity.getCouponPrice());
        shareVo.setRed(GoodsPriceUtil.type(suShareEntity.getPlatformType()).calcUserBuyProfit(smProductEntity,userBuyRate).intValue());
        shareVo.setShareMoney(GoodsPriceUtil.type(suShareEntity.getPlatformType()).calcUserShareProfit(smProductEntity,userShareRate).intValue());
        shareVo.setShow(suShareEntity.getWatch());
        shareVo.setSellOut(suShareEntity.getSell());
        shareVo.setWillMakeMoney(suShareEntity.getWillMakeMoney());
        shareVo.setCreateTime(DateUtil.format(suShareEntity.getCreateTime(),"MM-dd HH:mm"));
        return shareVo;
    }

    @Override
    public void del(Integer userId, Integer shareId) {
        SuShareEntity example = new SuShareEntity();
        example.setUserId(userId);
        example.setId(shareId);
        example.setDel(0);
        suShareMapper.updateByPrimaryKeySelective(example);
    }
}
