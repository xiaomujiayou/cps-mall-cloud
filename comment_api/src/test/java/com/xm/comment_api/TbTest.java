package com.xm.comment_api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.*;
import com.taobao.api.response.TbkCouponGetResponse;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import com.taobao.api.response.TbkItemInfoGetResponse;

public class TbTest {
    public static void main(String[] args) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "28225711", "edb1d724a0f70a68b82f143e8fe6fa45");
//        TbkDgMaterialOptionalRequest request = new TbkDgMaterialOptionalRequest();
//        request.setAdzoneId(110169250471L);
//        request.setMaterialId(6707L);
//        request.setQ("上衣");
//        System.out.println(JSON.toJSONString(client.execute(request),SerializerFeature.PrettyFormat));
//        TbkTpwdCreateRequest request1 = new TbkTpwdCreateRequest();
//        request1.setLogo("https://img.alicdn.com/bao/uploaded/i4/2206713766363/O1CN01jAhIVk1wsIkWQff48_!!0-item_pic.jpg_190x190q90s150.jpg_.webp");
//        request1.setText("粉饰生活-优惠券");
////        request1.setUrl("https://s.click.taobao.com/t?e=m%3D2%26s%3Dtmm7zqdvayEcQipKwQzePOeEDrYVVa64Dne87AjQPk9yINtkUhsv0JTX%2B77jyRfjh1wntvOGDCxVJEqwey6i9wnzq1%2BD7g2klOfikHiekQv2bF8%2BAJEs6BdCgjCPnTyOgvUNyyJS1KL3%2BWMkG3VUsy50LZqhEfAMFfBjyP2hsdeW3GylOTX1djXX5G1zAkzHNHpKKDaP%2BZfwHu%2FhWfi%2F3aJn5AyUbPoV&scm=null&pvid=100_11.178.150.1_95917_5841585482059958069&app_pvid=59590_11.92.49.29_14984_1585482059952&ptl=floorId:6706;originalFloorId:6706;pvid:100_11.178.150.1_95917_5841585482059958069;app_pvid:59590_11.92.49.29_14984_1585482059952&union_lens=lensId%3AMAPI%401585482060%400b5c311d_0e4b_1712618511a_09f2%4001");
//        request1.setUrl("https://uland.taobao.com/coupon/edetail?e=%2FZwilVu1JsoNfLV8niU3R5TgU2jJNKOfNNtsjZw%2F%2FoLx9Yu8EnnihKBmSHuckJp9E6aJlDOTZ6iK9WryizJgjMuRTiT9oEhV0I3XooCj8DGWpPcezeIzUX6pD8ssLyKjmMHpNfYdHdDlcS8dKkRo6hZPRhSrq8bUSZWEBdUnApGy8uZ5eYA3Wftt4RI9nFLY7Mlb4gfXPd5VbrKqp4Yn8g%3D%3D&&app_pvid=59590_11.92.49.29_14984_1585482059952&ptl=floorId:6706;app_pvid:59590_11.92.49.29_14984_1585482059952;tpp_pvid:100_11.178.150.1_95917_5841585482059958069&union_lens=lensId%3AMAPI%401585482060%400b5c311d_0e4b_1712618511a_09f2%4001");
//        System.out.println(JSON.toJSONString(client.execute(request1),SerializerFeature.PrettyFormat));



//        TbkCouponGetRequest request = new TbkCouponGetRequest();
//        request.setItemId(596397553562L);
//        request.setActivityId("4fc0ea1865764cf295e2b71ff276b4a9");
//        TbkCouponGetResponse rsp = client.execute(request);

//        TbkItemInfoGetRequest request = new TbkItemInfoGetRequest();
//        request.setNumIids("596397553562");
//        System.out.println(JSON.toJSONString(client.execute(request),SerializerFeature.PrettyFormat));

//        TbkActivityInfoGetRequest request = new TbkActivityInfoGetRequest();
//        request.setAdzoneId(110169250471L);
//        request.setActivityMaterialId("1585276486161");
//        System.out.println(JSON.toJSONString(client.execute(request),SerializerFeature.PrettyFormat));


//        TbkActivityLin request = new TbkActivityInfoGetRequest();
//        request.setAdzoneId(110169250471L);
//        request.setActivityMaterialId("1585276486161");
//        System.out.println(JSON.toJSONString(client.execute(request),SerializerFeature.PrettyFormat));
//
//        TbkDgOptimusMaterialRequest request = new TbkDgOptimusMaterialRequest();
//        request.setAdzoneId(110169250471L);
//        request.setMaterialId(13256L);
//        request.setItemId(596397553562L);
//        TbkDgOptimusMaterialResponse rsp = client.execute(request);


//        long a = System.currentTimeMillis();
//        System.out.println(1111);
//        TbkItemInfoGetRequest request = new TbkItemInfoGetRequest();
//        request.setNumIids("596397553562");
//        TbkItemInfoGetResponse rsp = client.execute(request);
//
//        TbkDgMaterialOptionalRequest request1 = new TbkDgMaterialOptionalRequest();
//        request1.setPageSize(100L);
//        request1.setAdzoneId(110169250471L);
//        request1.setMaterialId(6707L);
//        request1.setQ(rsp.getResults().get(0).getTitle());
//        TbkDgMaterialOptionalResponse rsp1 = client.execute(request1);



//        System.out.println(JSON.toJSONString(rsp,  SerializerFeature.PrettyFormat));



    }
}
