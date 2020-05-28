//package com.xm.api_user;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.pdd.pop.sdk.http.PopClient;
//import com.pdd.pop.sdk.http.PopHttpClient;
//import com.pdd.pop.sdk.http.api.request.PddDdkGoodsPidGenerateRequest;
//import com.pdd.pop.sdk.http.api.request.PddGoodsOptGetRequest;
//import com.pdd.pop.sdk.http.api.response.PddDdkGoodsPidGenerateResponse;
//import com.pdd.pop.sdk.http.api.response.PddGoodsOptGetResponse;
//import com.xm.comment_utils.sql.MySqlUtil;
//
//import javax.sql.RowSet;
//import java.sql.Date;
//import java.sql.SQLException;
//import java.utils.ArrayList;
//import java.utils.List;
//
//public class PidGenerate {
//    private static String clientId = "022c61074e3d46dd946a45f3024a75d8";
//    private static String clientSecret = "702bee4684121e512392cb0405b9292fbc624234";
//    private static PopClient client = new PopHttpClient(clientId, clientSecret);
//    private static MySqlUtil mysql = MySqlUtil.getInstance(new MySqlUtil.Config("47.92.6.56","share_user","root","xm123456"));
//    private static String sql = "insert into su_pid (name,pdd,create_time) values(?,?,?)";
//    public static void main(String[] args) throws Exception {
//
//        PddDdkGoodsPidGenerateRequest request = new PddDdkGoodsPidGenerateRequest();
//        List<String> pids = new ArrayList<>();
//        request.setNumber(2L);
//        request.setPIdNameList(pids);
//        pids.add("1");
//        pids.add("2");
//        System.out.println(JSON.toJSONString(client.syncInvoke(request), SerializerFeature.PrettyFormat));
//
////        for (int i = 0; i < 500000; i++) {
////            pids.add((i+1)+"");
////            if(pids.size() >= 100){
////                PddDdkGoodsPidGenerateResponse response = client.syncInvoke(request);
////                System.out.println("还剩："+response.getPIdGenerateResponse().getRemainPidCount());
////                response.getPIdGenerateResponse().getPIdList().forEach(o->{
////                    save(o.getPidName(),o.getPId());
////                });
////                pids.clear();
////            }
////        }
//    }
//
//    private static void save(String name,String pid){
//        try {
//            mysql.update(sql,name,pid,new Date(System.currentTimeMillis()));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//