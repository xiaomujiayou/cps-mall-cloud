package com.xm.comment_api;

import cn.hutool.core.collection.CollUtil;
import com.vip.adp.api.open.service.*;
import com.vip.osp.sdk.context.ClientInvocationContext;
import com.vip.osp.sdk.context.InvocationContext;
import com.vip.osp.sdk.exception.OspException;
import vipapis.address.AddressServiceHelper;
import vipapis.address.ProvinceWarehouse;
import vipapis.marketplace.product.Product;
import vipapis.puma.ChannelPumaServiceHelper;
import vipapis.puma.Pagination;
import vipapis.puma.ProductQueryRequest;

import java.util.List;
import java.util.UUID;

public class WphTest {
    public static void main(String[] args) throws OspException {

        InvocationContext instance = null;
        try {
//1、获取服务客户端
            UnionGoodsServiceHelper.UnionGoodsServiceClient client = new UnionGoodsServiceHelper.UnionGoodsServiceClient();
//            AddressServiceHelper.AddressServiceClient client = new AddressServiceHelper.AddressServiceClient();
//2、设置系统级调用参数，只需在程序开始调用前设置一次即可
            instance = InvocationContext.Factory.getInstance();
//            instance.setAppKey("3bba2313");//替换为你的appKey
//            instance.setAppSecret("1662F225B9B15381D660538E219B9A60");//替换为你的appSecret
            instance.setAppKey("7a28a37b");//替换为你的appKey
            instance.setAppSecret("27C64736068C19749BDA267AD428F087");//替换为你的appSecret
//            Oauth认证时必填
            instance.setAppURL("https://gw.vipapis.com/");//沙箱环境

            GoodsInfoRequest request = new GoodsInfoRequest();
            request.setChannelType(1);
            request.setPage(1);
            request.setRequestId(UUID.randomUUID().toString());
            client.goodsList(request);

            QueryGoodsRequest request1 = new QueryGoodsRequest();
            request1.setKeyword("鞋子");
            request1.setPage(1);
            request1.setRequestId(UUID.randomUUID().toString());
            client.query(request1);

//instance.setAppURL("https://gw.vipapis.com/");//正式环境
//instance.setReadTimeOut(30000);//读写超时时间，可选，默认30秒
//instance.setConnectTimeOut(5000);//连接超时时间，可选，默认5秒
//3、调用API及返回
//            List<ProvinceWarehouse> provinceWarehouse = client.getProvinceWarehouse(vipapis.address.Is_Show_GAT.SHOW_ALL);
        } catch (OspException e) {
//4、捕获异常
            System.out.println(instance.getLastInvocation().getSign());//获取最近一次调用的sign值
        }

//        //2、设置系统级调用参数
//        InvocationContext instance = InvocationContext.Factory.getInstance();
//        instance.setAppKey("3bba2313");//替换为你的appKey
//        instance.setAppSecret("1662F225B9B15381D660538E219B9A60");//替换为你的appSecret
//        instance.setAppURL("https://gw.vipapis.com/");//正式环境
//
//        ChannelPumaServiceHelper.ChannelPumaServiceClient channelPumaServiceClient = new ChannelPumaServiceHelper.ChannelPumaServiceClient();
//        ProductQueryRequest request = new ProductQueryRequest();
//        Pagination pagination = new Pagination();
//        pagination.setPage(1);
//        pagination.setSize(10);
//        request.setPagination(pagination);
//        request.setQuery_types(CollUtil.newHashSet(1,2,3,4,5));
//
//        channelPumaServiceClient.getPumaProducts(request);


    }
}
