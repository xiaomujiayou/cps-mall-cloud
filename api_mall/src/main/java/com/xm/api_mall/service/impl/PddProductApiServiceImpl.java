package com.xm.api_mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.PageUtil;
import com.alibaba.fastjson.JSON;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.*;
import com.pdd.pop.sdk.http.api.response.*;
import com.xm.api_mall.service.ConfigService;
import com.xm.api_mall.service.ProductApiService;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment.utils.GoodsPriceUtil;
import com.xm.comment_serialize.module.mall.bo.PddGoodsListItem;
import com.xm.comment_serialize.module.mall.bo.PddThemeBo;
import com.xm.comment_serialize.module.mall.bo.ProductCriteriaBo;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.constant.*;
import com.xm.comment_serialize.module.mall.entity.SmConfigEntity;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.vo.SmProductSimpleVo;
import com.xm.comment_utils.enu.EnumUtils;
import com.xm.comment_utils.mybatis.PageBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        request.setPid(criteria.getPid());
        request.setSortType(sortConfig.getVal() == null?null:Integer.valueOf(sortConfig.getVal()));
        request.setMerchantType(shopTypeConfig.getVal()==null?null:Integer.valueOf(shopTypeConfig.getVal()));
        request.setPage(criteria.getPageNum());
        request.setPageSize(criteria.getPageSize());
        request.setOptId(criteria.getOptionId() != null?criteria.getOptionId().longValue():null);
        request.setKeyword(criteria.getKeyword());
        request.setGoodsIdList(criteria.getGoodsIdList());
        request.setSortType(criteria.getOrderBy(PlatformTypeConstant.PDD));
        request.setActivityTags(criteria.getActivityTags());
        request.setWithCoupon(criteria.getHasCoupon());
        //筛选器
        List<Map<String,Object>> rangeList = new ArrayList<>();
        //价格区间
        if(criteria.getMaxPrice() != null && criteria.getMinPrice() != null){
            Map<String,Object> range = new HashMap<>();
            range.put("range_id",0);
            range.put("range_from",criteria.getMinPrice());
            range.put("range_to",criteria.getMaxPrice().equals(0)?null:criteria.getMaxPrice());
            rangeList.add(range);
        }

        if(!rangeList.isEmpty()){
            request.setRangeList(JSON.toJSONString(rangeList));
        }
        PddDdkGoodsSearchResponse response = popHttpClient.syncInvoke(request);
        List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> goodsList = response.getGoodsSearchResponse().getGoodsList();
        List<SmProductEntity> smProductEntityList = goodsList.stream().map(o ->{return convertGoodsList(o);}).collect(Collectors.toList());
        return packageToPageBean(
                smProductEntityList,
                response.getGoodsSearchResponse().getTotalCount().intValue(),
                criteria.getPageNum(),
                criteria.getPageSize());

    }

    @Override
    public List<SmProductEntity> details(List<String> goodsIds) throws Exception {
        PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
        request.setGoodsIdList(goodsIds.stream().map(o->Long.valueOf(o)).collect(Collectors.toList()));
        PddDdkGoodsSearchResponse response = popHttpClient.syncInvoke(request);
        List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> goodsList = response.getGoodsSearchResponse().getGoodsList();
        List<SmProductEntity> smProductEntityList = goodsList.stream().map(o ->{return convertGoodsList(o);}).collect(Collectors.toList());
        return smProductEntityList;
    }

    @Override
    public SmProductEntity detail(String goodsId,String pid) throws Exception {
        PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
        request.setGoodsIdList(Arrays.asList(Long.valueOf(goodsId)));
        request.setPid(pid);
        PddDdkGoodsDetailResponse response = popHttpClient.syncInvoke(request);
        if (response.getGoodsDetailResponse().getGoodsDetails() == null || response.getGoodsDetailResponse().getGoodsDetails().size() <= 0)
            throw new GlobleException(MsgEnum.DATA_ALREADY_NOT_EXISTS,"找不到拼多多商品："+goodsId);
        return convertDetail(response.getGoodsDetailResponse().getGoodsDetails().get(0));
    }

    @Override
    public SmProductSimpleVo basicDetail(Long goodsId) throws Exception {
        PddDdkGoodsBasicInfoGetRequest request = new PddDdkGoodsBasicInfoGetRequest();
        request.setGoodsIdList(Arrays.asList(goodsId));
        PddDdkGoodsBasicInfoGetResponse response = popHttpClient.syncInvoke(request);
        SmProductSimpleVo smProductSimpleVo = new SmProductSimpleVo();
        smProductSimpleVo.setGoodsId(response.getGoodsBasicDetailResponse().getGoodsList().get(0).getGoodsId().toString());
        smProductSimpleVo.setGoodsThumbnailUrl(response.getGoodsBasicDetailResponse().getGoodsList().get(0).getGoodsPic());
        smProductSimpleVo.setName(response.getGoodsBasicDetailResponse().getGoodsList().get(0).getGoodsName());
        smProductSimpleVo.setOriginalPrice(response.getGoodsBasicDetailResponse().getGoodsList().get(0).getMinGroupPrice().intValue());
        smProductSimpleVo.setType(PlatformTypeConstant.PDD);
        return smProductSimpleVo;
    }

    @Override
    public ShareLinkBo getShareLink(String customParams,String pId, String goodsId,String couponId) throws Exception {
        PddDdkGoodsPromotionUrlGenerateRequest request = new PddDdkGoodsPromotionUrlGenerateRequest();
        request.setPId(pId);
        request.setGoodsIdList(Arrays.asList(Long.valueOf(goodsId)));
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
    public Date getTime() throws Exception {
        PddTimeGetRequest request = new PddTimeGetRequest();
        PddTimeGetResponse response = popHttpClient.syncInvoke(request);
        return DateUtils.parseDate(response.getTimeGetResponse().getTime(),"yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public PageBean<SmProductEntity> getTopGoodsList(Integer type,String pid, Integer pageNum, Integer pageSize) throws Exception {
        PddDdkTopGoodsListQueryRequest request = new PddDdkTopGoodsListQueryRequest();
        request.setOffset(PageUtil.getStart(pageNum,pageSize));
        request.setLimit(pageSize);
        request.setSortType(type);
        request.setPId(pid);
        PddDdkTopGoodsListQueryResponse response = popHttpClient.syncInvoke(request);
        List<SmProductEntity> list = response.getTopGoodsListGetResponse().getList().stream().map( o ->{
            return convertGoodsList(o);
        }).collect(Collectors.toList());
        return packageToPageBean(
                list,
                response.getTopGoodsListGetResponse().getTotal().intValue(),
                pageNum,
                pageSize);
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
    public PageBean<SmProductEntity> getThemeGoodsList(Integer themeId,String pid) throws Exception {
        PddDdkThemeGoodsSearchRequest request = new PddDdkThemeGoodsSearchRequest();
        request.setThemeId(themeId.longValue());
        PddDdkThemeGoodsSearchResponse response = popHttpClient.syncInvoke(request);
        List<SmProductEntity> list = response.getThemeListGetResponse().getGoodsList().stream().map(o ->{
            return convertGoodsList(o);
        }).collect(Collectors.toList());
        return packageToPageBean(
                list,
                response.getThemeListGetResponse().getTotal().intValue(),
                1,
                response.getThemeListGetResponse().getTotal().intValue());
    }

    @Override
    public PageBean<SmProductEntity> getRecommendGoodsList(String pid,Integer channelType,Integer pageNum,Integer pageSize) throws Exception {
        PddDdkGoodsRecommendGetRequest request = new PddDdkGoodsRecommendGetRequest();
        request.setChannelType(channelType);
        request.setOffset(PageUtil.getStart(pageNum,pageSize));
        request.setLimit(pageSize);
        request.setPid(pid);
        PddDdkGoodsRecommendGetResponse response = popHttpClient.syncInvoke(request);
        List<SmProductEntity> list = response.getGoodsBasicDetailResponse().getList().stream().map(o->{
            return convertGoodsList(o);
        }).collect(Collectors.toList());
        return packageToPageBean(
                list,
                response.getGoodsBasicDetailResponse().getTotal().intValue(),
                pageNum,
                pageSize);
    }

    private PageBean<SmProductEntity> packageToPageBean(List<SmProductEntity> list,Integer total,Integer pageNum,Integer pageSize){
        PageBean<SmProductEntity> pageBean = new PageBean<>(list);
        pageBean.setTotal(total);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }

    private SmProductEntity convertGoodsList(Object listItem){
        PddGoodsListItem pddGoodsListItem = new PddGoodsListItem();
        BeanUtil.copyProperties(listItem,pddGoodsListItem);
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.PDD);
        smProductEntity.setGoodsId(pddGoodsListItem.getGoodsId().toString());
        smProductEntity.setGoodsThumbnailUrl(pddGoodsListItem.getGoodsThumbnailUrl());
        smProductEntity.setName(pddGoodsListItem.getGoodsName());
        smProductEntity.setOriginalPrice(pddGoodsListItem.getMinGroupPrice().intValue());
        smProductEntity.setCouponPrice(pddGoodsListItem.getCouponDiscount().intValue());
        smProductEntity.setMallName(pddGoodsListItem.getMallName());
        smProductEntity.setSalesTip(pddGoodsListItem.getSalesTip());
        smProductEntity.setMallCps(pddGoodsListItem.getMallCps());
        smProductEntity.setPromotionRate(pddGoodsListItem.getPromotionRate().intValue());
        smProductEntity.setHasCoupon(pddGoodsListItem.getHasCoupon()?1:0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.PDD).calcProfit(smProductEntity).intValue());
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
            } catch (Exception e) {
                log.error("{}",e);
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
        } catch (Exception e) {
            log.error("{}",e);
        }
        return null;
    }

}
