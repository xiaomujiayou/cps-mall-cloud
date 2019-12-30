package com.xm.api_mall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.*;
import com.pdd.pop.sdk.http.api.response.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Test {
    public static void main(String[] args) throws Exception {
        String clientId = "022c61074e3d46dd946a45f3024a75d8";
        String clientSecret = "702bee4684121e512392cb0405b9292fbc624234";
        PopClient client = new PopHttpClient(clientId, clientSecret);

        PddGoodsOptGetRequest request = new PddGoodsOptGetRequest();
        request.setParentOptId(1);
        System.out.println(JSON.toJSONString(client.syncInvoke(request),SerializerFeature.PrettyFormat));

//        PddDdkRpPromUrlGenerateRequest request = new PddDdkRpPromUrlGenerateRequest();
//        request.setChannelType(4);
//        request.setPIdList(Arrays.asList("8944230_121096342"));
//        request.setGenerateWeApp(true);
//        request.setGenerateWeappWebview(true);
//        System.out.println(JSON.toJSONString(client.syncInvoke(request),SerializerFeature.PrettyFormat));

//        PddDdkThemePromUrlGenerateRequest request = new PddDdkThemePromUrlGenerateRequest();
//        request.setPid("8944230_121096342");
//        request.setThemeIdList(Arrays.asList(7593l));
//        System.out.println(JSON.toJSONString(client.syncInvoke(request),SerializerFeature.PrettyFormat));


//        PddDdkWeappQrcodeUrlGenRequest request = new PddDdkWeappQrcodeUrlGenRequest();
//        request.setPId("8944230_121096342");
//        request.setGoodsIdList(Arrays.asList(72368682l));
//        System.out.println(JSON.toJSONString(client.syncInvoke(request),SerializerFeature.PrettyFormat));

//        PddDdkThemeListGetRequest request = new PddDdkThemeListGetRequest();
//        System.out.println(JSON.toJSONString(client.syncInvoke(request),SerializerFeature.PrettyFormat));

//        PddDdkThemeGoodsSearchRequest request = new PddDdkThemeGoodsSearchRequest();
//        request.setThemeId(7593l);
//        System.out.println(JSON.toJSONString(client.syncInvoke(request),SerializerFeature.PrettyFormat));


//        PddDdkOrderDetailGetRequest request = new PddDdkOrderDetailGetRequest();
//        request.setOrderSn("191124-380393554933775");
//        System.out.println(JSON.toJSONString(client.syncInvoke(request),SerializerFeature.PrettyFormat));

//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(format.format(new Date(1574769744000l - 10000000)));
//        PddDdkOrderListIncrementGetRequest request = new PddDdkOrderListIncrementGetRequest();
//        request.setPage(1);
//        request.setPageSize(10);
//        request.setReturnCount(true);
//        request.setStartUpdateTime(1574755252L);
//        request.setEndUpdateTime(1574819221L);
//        System.out.println(JSON.toJSONString(client.syncInvoke(request),SerializerFeature.PrettyFormat));

//        PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
//        request.setGoodsIdList(Arrays.asList(39747417643l));
//        System.out.println(JSON.toJSONString(client.syncInvoke(request),SerializerFeature.PrettyFormat));

//        PddDdkGoodsBasicInfoGetRequest request = new PddDdkGoodsBasicInfoGetRequest();
//        request.setGoodsIdList(Arrays.asList(18286407756l,397475984543l));
//        System.out.println(JSON.toJSONString(client.syncInvoke(request), SerializerFeature.PrettyFormat));

//        PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
//        request.setGoodsIdList(Arrays.asList(68385598599l,45793598207l));
//        System.out.println(JSON.toJSONString(client.syncInvoke(request), SerializerFeature.PrettyFormat));

//        PddDdkTopGoodsListQueryRequest request = new PddDdkTopGoodsListQueryRequest();
//        request.setGoodsIdList(Arrays.asList(64820789754l,39747417643l,3974417643l));
//        System.out.println(JSON.toJSONString(client.syncInvoke(request), SerializerFeature.PrettyFormat));


//        PddDdkOrderDetailGetRequest request = new PddDdkOrderDetailGetRequest();
//        request.setOrderSn("190804-324792485053775");
//        PddDdkOrderDetailGetResponse response = client.syncInvoke(request);
//        System.out.println(JSON.toJSONString(response));

//        PddDdkAllOrderListIncrementGetRequest request = new PddDdkAllOrderListIncrementGetRequest();
//        long startTime = 1564849000;
//        request.setStartUpdateTime(startTime);
//        request.setEndUpdateTime(startTime + 24 * 3600);
////        request.setPageSize(0);
////        request.setPage(0);
//        PddDdkAllOrderListIncrementGetResponse response = client.syncInvoke(request);
//        System.out.println(JsonUtil.transferToJson(response));

//        PddDdkOrderListIncrementGetRequest request = new PddDdkOrderListIncrementGetRequest();
//        request.setStartUpdateTime(1564849000l);
//        request.setEndUpdateTime(1564849000l+10000);
//        request.setReturnCount(false);
//        PddDdkOrderListIncrementGetResponse response = client.syncInvoke(request);
//        System.out.println(JsonUtil.transferToJson(response));

//        PddGoodsOptGetRequest request = new PddGoodsOptGetRequest();
//        request.setParentOptId(1);
//        PddGoodsOptGetResponse response = client.syncInvoke(request);
//        System.out.println(JsonUtil.transferToJson(response));

//        PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
//        PddDdkGoodsSearchResponse response = client.syncInvoke(request);
//        System.out.println(JsonUtil.transferToJson(response));

//        PddDdkGoodsDetailRequest request = new PddDdkGoodsDetailRequest();
//        List<Long> pIds = new ArrayList<>();
//        pIds.add(5901483212);l
//        request.setGoodsIdList(pIds);
//        PddDdkGoodsDetailResponse response = client.syncInvoke(request);
//        System.out.println(JsonUtil.transferToJson(response));

//        PddDdkGoodsSearchRequest request = new PddDdkGoodsSearchRequest();
//        List<Long> pIds = new ArrayList<>();
//        pIds.add(5901483212l);
//        request.setGoodsIdList(pIds);
//        PddDdkGoodsSearchResponse response = client.syncInvoke(request);
//        System.out.println(JsonUtil.transferToJson(response));

    }

    public static String getTimeZone(){
        Calendar cal = Calendar.getInstance();
        int offset = cal.get(Calendar.ZONE_OFFSET);
        cal.add(Calendar.MILLISECOND, -offset);
        Long timeStampUTC = cal.getTimeInMillis();
        Long timeStamp = System.currentTimeMillis();
        Long timeZone = (timeStamp - timeStampUTC) / (1000 * 3600); System.out.println(timeZone.intValue());
        return String.valueOf(timeZone);

    }
}
