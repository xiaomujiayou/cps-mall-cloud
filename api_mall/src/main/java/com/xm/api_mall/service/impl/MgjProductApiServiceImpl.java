package com.xm.api_mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mogujie.openapi.request.MgjRequest;
import com.mogujie.openapi.response.MgjResponse;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsPromotionUrlGenerateRequest;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsDetailResponse;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsPromotionUrlGenerateResponse;
import com.xm.api_mall.service.ProductApiService;
import com.xm.comment.utils.GoodsPriceUtil;
import com.xm.comment_api.client.MyMogujieClient;
import com.xm.comment_api.module.mgj.*;
import com.xm.comment_serialize.module.mall.bo.PddGoodsListItem;
import com.xm.comment_serialize.module.mall.bo.PddThemeBo;
import com.xm.comment_serialize.module.mall.bo.ProductCriteriaBo;
import com.xm.comment_serialize.module.mall.bo.ShareLinkBo;
import com.xm.comment_serialize.module.mall.constant.PlatformTypeConstant;
import com.xm.comment_serialize.module.mall.entity.SmProductEntity;
import com.xm.comment_serialize.module.mall.vo.SmProductSimpleVo;
import com.xm.comment_utils.mybatis.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("mgjProductApiService")
public class MgjProductApiServiceImpl implements ProductApiService {

    @Autowired
    private MyMogujieClient myMogujieClient;

    @Resource(name = "mgjProductApiService")
    private ProductApiService mgjProductApiService;

    @Override
    public PageBean<SmProductEntity> getProductByCriteria(ProductCriteriaBo criteria) throws Exception {
        PromInfoQueryBean promInfoQueryBean = new PromInfoQueryBean();
        promInfoQueryBean.setKeyword(criteria.getKeyword());
        promInfoQueryBean.setPageNo(criteria.getPageNum());
        promInfoQueryBean.setPageSize(criteria.getPageSize());
        promInfoQueryBean.setSortType(criteria.getOrderBy(PlatformTypeConstant.MGJ));
        promInfoQueryBean.setHasCoupon(criteria.getHasCoupon());
        MgjResponse<String> res = myMogujieClient.execute(new XiaoDianCpsdataPromItemGetRequest(promInfoQueryBean));
        JSONObject json = JSON.parseObject(res.getResult().getData());
        JSONArray goodsArrJson = json.getJSONArray("items");

        return packageToPageBean(
                goodsArrJson.stream().map(o -> convertGoodsList(o)).collect(Collectors.toList()),
                json.getInteger("total"),
                json.getInteger("page"),
                json.getInteger("pageSize"));
    }

    private PageBean<SmProductEntity> packageToPageBean(List<SmProductEntity> list,Integer total,Integer pageNum,Integer pageSize){
        PageBean<SmProductEntity> pageBean = new PageBean<>(list);
        pageBean.setTotal(total);
        pageBean.setPageNum(pageNum);
        pageBean.setPageSize(pageSize);
        return pageBean;
    }

    private SmProductEntity convertGoodsList(Object goodsObj){
        JSONObject goodsJson = (JSONObject) goodsObj;
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.MGJ);
        smProductEntity.setGoodsId(goodsJson.getString("itemId"));
        smProductEntity.setGoodsThumbnailUrl(goodsJson.getString("pictUrlForH5"));
        smProductEntity.setName(goodsJson.getString("title"));
        smProductEntity.setOriginalPrice((int)(goodsJson.getFloat("zkPrice") * 100));
        smProductEntity.setCouponPrice((int)(goodsJson.getFloat("couponAmount") * 100));
        smProductEntity.setMallName(goodsJson.getJSONObject("shopInfo").getString("name"));
        smProductEntity.setSalesTip(goodsJson.getString("biz30day"));
        smProductEntity.setMallCps(0);
        smProductEntity.setPromotionRate((int)(Float.valueOf(goodsJson.getString("commissionRate").replace("%",""))*10));
        smProductEntity.setHasCoupon(goodsJson.getInteger("couponLeftCount") > 0 ? 1 : 0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.MGJ).calcProfit(smProductEntity).intValue());
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }


    @Override
    public List<SmProductEntity> details(List<String> goodsIds) throws Exception {
        List<SmProductEntity> list = goodsIds.stream().map(o-> {
            try {
                return mgjProductApiService.detail(o,null);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        return CollUtil.removeNull(list);
    }

    @Override
    @Cacheable(value = "share.goods.detail.mgj",key = "#goodsId")
    public SmProductEntity detail(String goodsId, String pid) throws Exception {
        XiaoDianCpsdataItemGetRequest request = new XiaoDianCpsdataItemGetRequest("https://shop.mogujie.com/detail/" + goodsId);
        MgjResponse<String> res = myMogujieClient.execute(request);
        return convertDetail(JSON.parseObject(res.getResult().getData()));
    }

    private SmProductEntity convertDetail(JSONObject goodsJson){
        SmProductEntity smProductEntity = new SmProductEntity();
        smProductEntity.setType(PlatformTypeConstant.MGJ);
        smProductEntity.setGoodsId(goodsJson.getString("itemId"));
        smProductEntity.setGoodsThumbnailUrl(goodsJson.getString("pictUrl"));
        smProductEntity.setGoodsGalleryUrls(String.join(",",goodsJson.getString("pictUrl")));
        smProductEntity.setName(goodsJson.getString("title"));
        smProductEntity.setDes(goodsJson.getString("extendDesc"));
        smProductEntity.setOriginalPrice((int)(goodsJson.getFloat("zkPrice") * 100));
        smProductEntity.setCouponPrice((int)(goodsJson.getFloat("couponAmount") * 100));
        smProductEntity.setMallName(goodsJson.getJSONObject("shopInfo").getString("name"));
        smProductEntity.setSalesTip(goodsJson.getString("biz30day"));
        smProductEntity.setMallCps(0);
        smProductEntity.setPromotionRate((int)(Float.valueOf(goodsJson.getString("commissionRate").replace("%",""))*10));
        smProductEntity.setHasCoupon(goodsJson.getInteger("couponLeftCount") > 0 ? 1 : 0);
        smProductEntity.setCashPrice(GoodsPriceUtil.type(PlatformTypeConstant.MGJ).calcProfit(smProductEntity).intValue());
        smProductEntity.setServiceTags(String.join(",", goodsJson.getString("tag")));
        smProductEntity.setCouponId(goodsJson.getString("promId"));
        smProductEntity.setCreateTime(new Date());
        return smProductEntity;
    }

    @Override
    public SmProductSimpleVo basicDetail(Long goodsId) throws Exception {
        return null;
    }

    @Override
    public ShareLinkBo getShareLink(String customParams, String pId, Long goodsId) throws Exception {

//        PddDdkGoodsPromotionUrlGenerateRequest request = new PddDdkGoodsPromotionUrlGenerateRequest();
//        request.setPId(pId);
//        request.setGoodsIdList(Arrays.asList(goodsId));
//        request.setCustomParameters(customParams);
//        request.setGenerateShortUrl(true);
//        request.setGenerateWeApp(true);
//        PddDdkGoodsPromotionUrlGenerateResponse response = popHttpClient.syncInvoke(request);
//        ShareLinkBo shareLinkBo = new ShareLinkBo();
//        PddDdkGoodsPromotionUrlGenerateResponse.GoodsPromotionUrlGenerateResponseGoodsPromotionUrlListItem item = response.getGoodsPromotionUrlGenerateResponse().getGoodsPromotionUrlList().get(0);
//        shareLinkBo.setWeIconUrl(item.getWeAppInfo().getWeAppIconUrl());
//        shareLinkBo.setWeBannerUrl(item.getWeAppInfo().getBannerUrl());
//        shareLinkBo.setWeDesc(item.getWeAppInfo().getDesc());
//        shareLinkBo.setWeSourceDisplayName(item.getWeAppInfo().getSourceDisplayName());
//        shareLinkBo.setWePagePath(item.getWeAppInfo().getPagePath());
//        shareLinkBo.setWeUserName(item.getWeAppInfo().getUserName());
//        shareLinkBo.setWeTitle(item.getWeAppInfo().getTitle());
//        shareLinkBo.setWeAppId(item.getWeAppInfo().getAppId());
//        shareLinkBo.setShotUrl(item.getShortUrl());
//        shareLinkBo.setLongUrl(item.getUrl());
//        return shareLinkBo;

//        WxCodeParamBean wxCodeParamBean = new WxCodeParamBean();
//        wxCodeParamBean.setItemId("1m7bjww");
//        wxCodeParamBean.setPromId("1hv62yll8");
//        wxCodeParamBean.setUid("1et2402");
//        MgjRequest<XiaoDianCpsdataWxcodeGetResponse> mgjRequest = new XiaoDianCpsdataWxcodeGetRequest(wxCodeParamBean);
//        System.out.println(JSON.toJSONString(JSON.parse(client.execute(mgjRequest,token).getResult().getData()), SerializerFeature.PrettyFormat));
        return null;
    }

    @Override
    public String generatePid(Integer userId) throws Exception {
        return null;
    }

    @Override
    public Date getTime() throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> getTopGoodsList(Integer type, String pid, Integer pageNum, Integer pageSize) throws Exception {
        return null;
    }

    @Override
    public List<PddThemeBo> getThemeList() throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> getThemeGoodsList(Integer themeId, String pid) throws Exception {
        return null;
    }

    @Override
    public PageBean<SmProductEntity> getRecommendGoodsList(String pid, Integer channelType, Integer pageNum, Integer pageSize) throws Exception {
        return null;
    }
}
