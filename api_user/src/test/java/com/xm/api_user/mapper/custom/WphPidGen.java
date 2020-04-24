package com.xm.api_user.mapper.custom;

import com.alibaba.fastjson.JSON;
import com.vip.adp.api.open.service.*;
import com.vip.osp.sdk.context.ClientInvocationContext;
import com.vip.osp.sdk.exception.OspException;
import com.xm.comment_utils.sql.MySqlUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WphPidGen {

    private static String appId = "7a28a37b";
    private static String key = "27C64736068C19749BDA267AD428F087";
    private static String url = "https://gw.vipapis.com/";
    
    private static MySqlUtil mysql = new MySqlUtil(new MySqlUtil.Config("47.92.6.56","share_user","root","xm123456"));

    public static void main(String[] args) throws OspException, SQLException {
//        create(9899);
        updateDatabase();
    }

    private static void updateDatabase() throws OspException, SQLException {
        String sql = "update su_pid set wph = ? where id = ?";
        int i = 0;
        int j = 0;
        while (true){
            UnionPidServiceHelper.UnionPidServiceClient client = new UnionPidServiceHelper.UnionPidServiceClient();
            client.setClientInvocationContext(createContext());
            PidQueryRequest request = new PidQueryRequest();
            request.setRequestId(UUID.randomUUID().toString());
            request.setPage(++i);
            request.setPageSize(100);
            CpsUnionPidQueryResponse response = client.queryPid(request);
            for (PidInfo pidInfo : response.getPidInfoList()) {
                mysql.update(sql,pidInfo.getPid(),++j);
                System.out.println(j+":"+pidInfo.getPid());
            }
        }
    }

    private static void create(int i) {
        int max = 100;
        int num = (i / max) + (i % max > 0 ? 1 : 0);
        for (int j = 0; j < num; j++) {
            createPid( j * max,max);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createPid(int i,int j) {
        UnionPidServiceHelper.UnionPidServiceClient client = new UnionPidServiceHelper.UnionPidServiceClient();
        client.setClientInvocationContext(createContext());
        PidGenRequest request = new PidGenRequest();
        List<String> pidNames = new ArrayList<>();
        for (int k = 0; k < j; k++) {
            pidNames.add((i + k)+"");
        }
        request.setPidNameList(pidNames);
        request.setRequestId(UUID.randomUUID().toString());
        try {
            CpsUnionPidGenResponse response = client.genPid(request);
            System.out.println(JSON.toJSONString(response.getRemainPidCount()));
        } catch (OspException e) {
            e.printStackTrace();
        }
    }

    private static ClientInvocationContext createContext() {
        ClientInvocationContext instance = new ClientInvocationContext();
        instance.setAppKey(appId);
        instance.setAppSecret(key);
        instance.setAppURL(url);
        return instance;
    }
}
