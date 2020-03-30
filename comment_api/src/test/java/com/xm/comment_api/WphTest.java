package com.xm.comment_api;

import cn.hutool.core.collection.CollUtil;
import com.vip.adp.api.open.service.CommonParams;
import com.vip.adp.api.open.service.GoodsInfoRequest;
import com.vip.adp.api.open.service.UnionGoodsServiceHelper;
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

public class WphTest {
    public static void main(String[] args) throws OspException {

        //2、设置系统级调用参数
        InvocationContext instance = InvocationContext.Factory.getInstance();
        instance.setAppKey("7a28a37b");//替换为你的appKey
        instance.setAppSecret("27C64736068C19749BDA267AD428F087");//替换为你的appSecret
        instance.setAppURL("https://gw.vipapis.com/");//正式环境

        ChannelPumaServiceHelper.ChannelPumaServiceClient channelPumaServiceClient = new ChannelPumaServiceHelper.ChannelPumaServiceClient();
        ProductQueryRequest request = new ProductQueryRequest();
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setSize(10);
        request.setPagination(pagination);
        request.setQuery_types(CollUtil.newHashSet(1,2,3,4,5));

        channelPumaServiceClient.getPumaProducts(request);


    }
}
