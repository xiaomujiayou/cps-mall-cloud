package com.xm.api_mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.PageUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.*;
import com.pdd.pop.sdk.http.api.response.*;
import com.xm.api_mall.service.ConfigService;
import com.xm.api_mall.service.ProductApiService;
import com.xm.comment.exception.GlobleException;
import com.xm.comment.response.MsgEnum;
import com.xm.comment.utils.GoodsPriceUtil;
import com.xm.comment_serialize.module.mall.bo.PddThemeBo;
import com.xm.comment_serialize.module.mall.bo.ProductCriteriaBo;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.constant.*;
import com.xm.comment_serialize.module.mall.entity.SmConfigEntity;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_utils.enu.EnumUtils;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.mybatis.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("pddProductApiService")
public class PddProductApiServiceImpl implements ProductApiService {


    @Autowired
    private PopHttpClient popHttpClient;

    @Autowired
    private ConfigService configService;


    @Override
    public PageBean<SmProductEntity> getProductByCriteria(ProductCriteriaBo criteria) throws Exception{
        //获取排序规则
        SmConfigEntity sortConfig = configService.getConfig(criteria.getUserId(), ConfigEnmu.PDD_PRODUCT_BEST_LIST_SORT, ConfigTypeConstant.PROXY_CONFIG);
        //获取店铺类型规则
        SmConfigEntity shopTypeConfig = configService.getConfig(criteria.getUserId(), ConfigEnmu.PDD_PRODUCT_BEST_LIST_SHOP_TYPE,ConfigTypeConstant.PROXY_CONFIG);
        PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
        request.setSortType(sortConfig.getVal() == null?null:Integer.valueOf(sortConfig.getVal()));
        request.setMerchantType(shopTypeConfig.getVal()==null?null:Integer.valueOf(shopTypeConfig.getVal()));
        request.setPage(criteria.getPageNum());
        request.setPageSize(criteria.getPageSize());
        request.setOptId(criteria.getOptionId() != null?criteria.getOptionId().longValue():null);
        request.setKeyword(criteria.getKeyword());
        request.setGoodsIdList(criteria.getGoodsIdList());
        request.setSortType(criteria.getOrderBy());
        //筛选器
        List<Map<String,Object>> rangeList = new ArrayList<>();
        //价格区间
        if(criteria.getMaxPrice() != null && criteria.getMinPrice() != null){
            Map<String,Object> range = new HashMap<>();
            range.put("range_id",0);
            range.put("range_from",criteria.getMinPrice());
            range.put("range_to",criteria.getMaxPrice());
            rangeList.add(range);
        }
        if(!rangeList.isEmpty()){
            request.setRangeList(JSON.toJSONString(rangeList));
        }
        PddDdkGoodsSearchResponse response = popHttpClient.syncInvoke(request);
        List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> goodsList = response.getGoodsSearchResponse().getGoodsList();
        List<SmProductEntity> smProductEntityList = goodsList.stream().map(o ->{return convertListDetail(o);}).collect(Collectors.toList());
        PageBean<SmProductEntity> pageBean = new PageBean<>(smProductEntityList);
        pageBean.setPageNum(criteria.getPageNum());
        pageBean.setPageSize(criteria.getPageSize());
        pageBean.setTotal(response.getGoodsSearchResponse().getTotalCount());
        pageBean.setList(smProductEntityList);
        return pageBean;
    }

    @Override
    public List<SmProductEntity> details(List<Long> goodsIds) throws Exception {
        PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
        request.setGoodsIdList(goodsIds);
        PddDdkGoodsSearchResponse response = popHttpClient.syncInvoke(request);
        List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> goodsList = response.getGoodsSearchResponse().getGoodsList();
        List<SmProductEntity> smProductEntityList = goodsList.stream().map(o ->{return convertListDetail(o);}).collect(Collectors.toList());
        return smProductEntityList;
    }

    @Override
    public SmProductEntity detail(Long goodsId) throws Exception {
        PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
        request.setGoodsIdList(Arrays.asList(goodsId));
        PddDdkGoodsDetailResponse response = popHttpClient.syncInvoke(request);
        if (response.getGoodsDetailResponse().getGoodsDetails() == null || response.getGoodsDetailResponse().getGoodsDetails().size() <= 0)
            throw new GlobleException(MsgEnum.DATA_ALREADY_NOT_EXISTS,"找不到拼多多商品："+goodsId);
        return convertDetail(response.getGoodsDetailResponse().getGoodsDetails().get(00));
    }

    @Override
    public ShareLinkBo getShareLink(String customParams,String pId, Long goodsId) throws Exception {
        PddDdkGoodsPromotionUrlGenerateRequest request = new PddDdkGoodsPromotionUrlGenerateRequest();
        request.setPId(pId);
        request.setGoodsIdList(Arrays.asList(goodsId));
        request.setCustomParameters(customParams);
        request.setGenerateShortUrl(true);
        request.setGenerateWeApp(true);
        PddDdkGoodsPromotionUrlGenerateResponse response = popHttpClient.syncInvoke(request);
        ShareLinkBo shareLinkBo = new ShareLinkBo();
        PddDdkGoodsPromotionUrlGenerateResponse.GoodsPromotionUrlGenerateResponseGoodsPromotionUrlListItem item = response.getGoodsPromotionUrlGenerateResponse().getGoodsPromotionUrlList().get(0);
        shareLinkBo.setWeIconUrl(item.getWeAppInfo().getWeAppIconUrl());
        shareLinkBo.setWeBannerUrl(item.getWeAppInfo().getBannerUrl());
        shareLinkBo.setWeDesc(item.getWeAppInfo().getDesc());
        shareLinkBo.setWeSourceDisplayName(item.getWeAppInfo().getSourceDisplayName());
        shareLinkBo.setWePagePath(item.getWeAppInfo().getPagePath());
        shareLinkBo.setWeUserName(item.getWeAppInfo().getUserName());
        shareLinkBo.setWeTitle(item.getWeAppInfo().getTitle());
        shareLinkBo.setWeAppId(item.getWeAppInfo().getAppId());
        shareLinkBo.setShotUrl(item.getShortUrl());
        shareLinkBo.setLongUrl(item.getUrl());
        return shareLinkBo;
    }

    @Override
    public String generatePid(Integer userId) throws Exception {
        PddDdkGoodsPidGenerateRequest request = new PddDdkGoodsPidGenerateRequest();
        request.setNumber(1L);
        request.setPIdNameList(Arrays.asList(userId.toString()));
        PddDdkGoodsPidGenerateResponse response = popHttpClient.syncInvoke(request);
        log.info("pdd 剩余推广位：[{}]",response.getPIdGenerateResponse().getRemainPidCount());
        return response.getPIdGenerateResponse().getPIdList().get(0).getPId();
    }

    @Override
    public PageBean<SuOrderEntity> getOrderByIncrement(Date startUpdateTime, Date endUpdateTime, Integer pageNum, Integer pageSize) throws Exception {
        PddDdkOrderListIncrementGetRequest request = new PddDdkOrderListIncrementGetRequest();
        request.setStartUpdateTime(startUpdateTime.getTime()/1000);
        request.setEndUpdateTime(endUpdateTime.getTime()/1000);
        request.setPage(pageNum);
        request.setPageSize(pageSize);
        request.setReturnCount(true);
        PddDdkOrderListIncrementGetResponse response = popHttpClient.syncInvoke(request);
        List<PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem> listItem = response.getOrderListGetResponse().getOrderList();
        List<SuOrderEntity> orderEntities = null;
        if(listItem != null) {
            orderEntities = listItem.stream().map(o -> {
                return convertOrder(o);
            }).collect(Collectors.toList());
        }
        PageBean<SuOrderEntity> pageBean = new PageBean<>(orderEntities);
        pageBean.setList(orderEntities);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        pageBean.setTotal(response.getOrderListGetResponse().getTotalCount());
        return pageBean;
    }

//    private List<SuOrderEntity> moke(){
//        List<SuOrderEntity> suOrderEntities = new ArrayList<>();
//        for (int i = 0; i < 40; i++) {
//            SuOrderEntity suOrderEntity = new SuOrderEntity();
//            suOrderEntity.setNum(i+"");
//            suOrderEntities.add(suOrderEntity);
//        }
//        return suOrderEntities;
//    }

    @Override
    public Date getTime() throws Exception {
        PddTimeGetRequest request = new PddTimeGetRequest();
        PddTimeGetResponse response = popHttpClient.syncInvoke(request);
        return DateUtils.parseDate(response.getTimeGetResponse().getTime(),"yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public PageBean<SmProductEntity> getTopGoodsList(Integer type, Integer pageNum, Integer pageSize) throws Exception {
        PddDdkTopGoodsListQueryRequest request = new PddDdkTopGoodsListQueryRequest();
        request.setOffset(PageUtil.getStart(pageNum,pageSize));
        request.setLimit(pageSize);
        request.setSortType(type);
        PddDdkTopGoodsListQueryResponse response = popHttpClient.syncInvoke(request);
        List<SmProductEntity> list = response.getTopGoodsListGetResponse().getList().stream().map( o ->{
            return convertListDetail(o);
        }).collect(Collectors.toList());
        PageBean<SmProductEntity> pageBean = new PageBean<>(list);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        pageBean.setTotal(response.getTopGoodsListGetResponse().getTotal());
        return pageBean;
    }

    @Override
    public List<PddThemeBo> getThemeList() throws Exception {
        PddDdkThemeListGetRequest request = new PddDdkThemeListGetRequest();
        PddDdkThemeListGetResponse response = popHttpClient.syncInvoke(request);
        List<PddThemeBo> list = response.getThemeListGetResponse().getThemeList().stream().map(o -> {
            PddThemeBo pddThemeBo = new PddThemeBo();
            pddThemeBo.setId(o.getId().intValue());
            pddThemeBo.setName(o.getName());
            pddThemeBo.setImageUrl(o.getImageUrl());
            pddThemeBo.setGoodsNum(o.getGoodsNum().intValue());
            return pddThemeBo;
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public PageBean<SmProductEntity> getThemeGoodsList(Integer themeId) throws Exception {
        PddDdkThemeGoodsSearchRequest request = new PddDdkThemeGoodsSearchRequest();
        request.setThemeId(themeId.longValue());
        PddDdkThemeGoodsSearchResponse response = popHttpClient.syncInvoke(request);

        List<SmProductEntity> list = response.getThemeListGetResponse().getGoodsList().stream().map(o ->{
            return convertThemeGoods(o);
        }).collect(Collectors.toList());

        PageBean<SmProductEntity> pageBean = new PageBean<>(list);
        pageBean.setTotal(response.getThemeListGetResponse().getTotal());
        pageBean.setPageNum(1);
        pageBean.setPageSize(response.getThemeListGetResponse().getTotal().intValue());
        return pageBean;
    }




    private SuOrderEntity convertOrder(PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem item){
        SuOrderEntity orderEntity = new SuOrderEntity();
        orderEntity.setOrderSn(item.getOrderSn());
        orderEntity.setProductId(item.getGoodsId().toString());
        orderEntity.setProductName(item.getGoodsName());
        orderEntity.setImgUrl(item.getGoodsThumbnailUrl());
        orderEntity.setPlatformType(PlatformTypeConstant.PDD);
        orderEntity.setState(item.getOrderStatus());
        orderEntity.setFailReason(item.getFailReason());
        orderEntity.setPId(item.getPId());
        orderEntity.setOriginalPrice(item.getGoodsPrice().intValue());
        orderEntity.setQuantity(item.getGoodsQuantity().intValue());
        orderEntity.setAmount(item.getOrderAmount().intValue());
        orderEntity.setPromotionRate(item.getPromotionRate().intValue());
        orderEntity.setPromotionAmount(item.getPromotionAmount().intValue());
        orderEntity.setType(item.getType());
        orderEntity.setCustomParameters(item.getCustomParameters());
        orderEntity.setOrderModifyAt(new Date(item.getOrderModifyAt() * 1000));
        return orderEntity;
    }

    private SmProductEntity convertThemeGoods(PddDdkThemeGoodsSearchResponse.ThemeListGetResponseGoodsListItem goodsListItem) {
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.PDD);
        smProductEntity.setGoodsId(goodsListItem.getGoodsId().toString());
        smProductEntity.setGoodsThumbnailUrl(goodsListItem.getGoodsThumbnailUrl());
        smProductEntity.setName(goodsListItem.getGoodsName());
        smProductEntity.setOriginalPrice(goodsListItem.getMinGroupPrice().intValue());
        smProductEntity.setCouponPrice(goodsListItem.getCouponDiscount().intValue());
        smProductEntity.setMallName(goodsListItem.getMallName());
        smProductEntity.setSalesTip(goodsListItem.getSalesTip());
//        smProductEntity.setMallCps(goodsListItem.getMallCps());
        smProductEntity.setPromotionRate(goodsListItem.getPromotionRate().intValue());
        smProductEntity.setHasCoupon(goodsListItem.getHasCoupon()?1:0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.PDD).calcProfit(smProductEntity).intValue());
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }

    private SmProductEntity convertListDetail(PddDdkTopGoodsListQueryResponse.TopGoodsListGetResponseListItem goodsListItem) {
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.PDD);
        smProductEntity.setGoodsId(goodsListItem.getGoodsId().toString());
        smProductEntity.setGoodsThumbnailUrl(goodsListItem.getGoodsThumbnailUrl());
        smProductEntity.setName(goodsListItem.getGoodsName());
        smProductEntity.setOriginalPrice(goodsListItem.getMinGroupPrice().intValue());
        smProductEntity.setCouponPrice(goodsListItem.getCouponDiscount().intValue());
        smProductEntity.setMallName(goodsListItem.getMallName());
        smProductEntity.setSalesTip(goodsListItem.getSalesTip());
        smProductEntity.setMallCps(goodsListItem.getMallCps());
        smProductEntity.setPromotionRate(goodsListItem.getPromotionRate().intValue());
        smProductEntity.setHasCoupon(goodsListItem.getHasCoupon()?1:0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.PDD).calcProfit(smProductEntity).intValue());
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }
    private SmProductEntity convertListDetail(PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem goodsListItem) {
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.PDD);
        smProductEntity.setGoodsId(goodsListItem.getGoodsId().toString());
        smProductEntity.setGoodsThumbnailUrl(goodsListItem.getGoodsThumbnailUrl());
        smProductEntity.setName(goodsListItem.getGoodsName());
        smProductEntity.setOriginalPrice(goodsListItem.getMinGroupPrice().intValue());
        smProductEntity.setCouponPrice(goodsListItem.getCouponDiscount().intValue());
        smProductEntity.setMallName(goodsListItem.getMallName());
        smProductEntity.setSalesTip(goodsListItem.getSalesTip());
        smProductEntity.setMallCps(goodsListItem.getMallCps());
        smProductEntity.setPromotionRate(goodsListItem.getPromotionRate().intValue());
        smProductEntity.setHasCoupon(goodsListItem.getHasCoupon()?1:0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.PDD).calcProfit(smProductEntity).intValue());
//        if(goodsListItem.getServiceTags() != null)
//            smProductEntity.setServiceTags(String.join(",", getServiceTags(goodsListItem.getServiceTags().stream().map(o -> {
//                return o.intValue();
//            }).collect(Collectors.toList()))));
//        if(goodsListItem.getActivityType() != null)
//            smProductEntity.setActivityType(getActiviteType(goodsListItem.getActivityType()));
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }
    private SmProductEntity convertDetail(PddDdkGoodsDetailResponse.GoodsDetailResponseGoodsDetailsItem detailsItem){
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.PDD);
        smProductEntity.setGoodsId(detailsItem.getGoodsId().toString());
        smProductEntity.setGoodsThumbnailUrl(detailsItem.getGoodsImageUrl());
        smProductEntity.setGoodsGalleryUrls(String.join(",",detailsItem.getGoodsGalleryUrls()));
        smProductEntity.setName(detailsItem.getGoodsName());
        smProductEntity.setDes(detailsItem.getGoodsDesc());
        smProductEntity.setOriginalPrice(detailsItem.getMinGroupPrice().intValue());
        smProductEntity.setCouponPrice(detailsItem.getCouponDiscount().intValue());
        smProductEntity.setMallName(detailsItem.getMallName());
        smProductEntity.setSalesTip(detailsItem.getSalesTip());
        smProductEntity.setMallCps(2);
        smProductEntity.setPromotionRate(detailsItem.getPromotionRate().intValue());
        smProductEntity.setHasCoupon(detailsItem.getCouponTotalQuantity() > 0?1:0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.PDD).calcProfit(smProductEntity).intValue());
        if(detailsItem.getServiceTags() != null)
            smProductEntity.setServiceTags(String.join(",", getServiceTags(detailsItem.getServiceTags())));
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }

    private List<String> getServiceTags(List<Integer> serviceTags){
        if(serviceTags == null || serviceTags.size() <= 0)
            return null;
        List<String> tags = serviceTags.stream().map(o->{
            try {
                PddServiceTagEnum pddServiceTagEnum = EnumUtils.getEnum(PddServiceTagEnum.class,"tagId",o);
                if (pddServiceTagEnum != null && pddServiceTagEnum.getShow()){
                    return pddServiceTagEnum.getTagName();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (EnumConstantNotPresentException e){
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        CollUtil.removeNull(tags);
        return tags;
    }
    private String getActiviteType(Integer activiteType) {
        if(activiteType == null)
            return null;
        try {
            return EnumUtils.getEnum(PddActivityTypeEnum.class,"activityId",activiteType).getActivityName();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (EnumConstantNotPresentException e){
            log.error(e.getMessage());
        }
        return null;
    }

}
