package com.xm.comment_api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mogujie.openapi.MogujieClient;
import com.mogujie.openapi.exceptions.ApiException;
import com.mogujie.openapi.request.MgjRequest;
import com.xm.comment_api.module.mgj.*;

public class MgjTest {

    public static void main(String[] args) throws ApiException {
        MogujieClient client = new MogujieClient("102139","FF7E20C91590054EACEFC1D645C8C5AE","https://openapi.mogujie.com/invoke");
        client.setIgnoreSSLCheck(true);
        String token = "1AEF7EF2E317335A3092C40674DC9D8F";
        PromInfoQueryBean promInfoQueryBean = new PromInfoQueryBean();
        promInfoQueryBean.setKeyword("女装");
//        MgjRequest<XiaoDianCpsdataPromItemGetResponse> mgjRequest = new XiaoDianCpsdataPromItemGetRequest(promInfoQueryBean);
//        System.out.println(JSON.toJSONString(JSON.parse(client.execute(mgjRequest,token).getResult().getData()), SerializerFeature.PrettyFormat));
//        MgjRequest<XiaoDianCpsdataItemGetResponse> mgjRequest = new XiaoDianCpsdataItemGetRequest("https://shop.mogujie.com/detail/1m7bjww?ptp=31.LqLXfb.0.0.UjoG4vyS");
//        System.out.println(JSON.toJSONString(JSON.parse(client.execute(mgjRequest,token).getResult().getData()), SerializerFeature.PrettyFormat));

        WxCodeParamBean wxCodeParamBean = new WxCodeParamBean();
        wxCodeParamBean.setItemId("1m7bjww");
        wxCodeParamBean.setPromId("1hv62yll8");
        wxCodeParamBean.setUid("1et2402");
        MgjRequest<XiaoDianCpsdataWxcodeGetResponse> mgjRequest = new XiaoDianCpsdataWxcodeGetRequest(wxCodeParamBean);
        System.out.println(JSON.toJSONString(JSON.parse(client.execute(mgjRequest,token).getResult().getData()), SerializerFeature.PrettyFormat));

    }
}
