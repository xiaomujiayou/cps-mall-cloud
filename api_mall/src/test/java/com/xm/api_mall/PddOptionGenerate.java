package com.xm.api_mall;

import com.alibaba.fastjson.JSON;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddDdkOrderDetailGetRequest;
import com.pdd.pop.sdk.http.api.request.PddGoodsOptGetRequest;
import com.pdd.pop.sdk.http.api.response.PddDdkOrderDetailGetResponse;
import com.pdd.pop.sdk.http.api.response.PddGoodsOptGetResponse;
import com.xm.comment_utils.sql.MySqlUtil;

import javax.sql.RowSet;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class PddOptionGenerate {
    private static String clientId = "022c61074e3d46dd946a45f3024a75d8";
    private static String clientSecret = "702bee4684121e512392cb0405b9292fbc624234";
    private static PopClient client = new PopHttpClient(clientId, clientSecret);
    private static MySqlUtil mysql = MySqlUtil.getInstance(new MySqlUtil.Config("47.92.6.56","share_mall","root","xm123456"));
    private static String sql = "insert into sm_opt (name,pdd_opt_id,parent_id,level,create_time) values(?,?,?,?,?)";
    public static void main(String[] args) throws Exception {
//        create1and2();
        addLevel3();
    }

    /**
     * 创建第一第二级标签
     */
    private static void create1and2() throws Exception {
        PddGoodsOptGetRequest request = new PddGoodsOptGetRequest();
        request.setParentOptId(0);
        PddGoodsOptGetResponse response = client.syncInvoke(request);
        save(null,response.getGoodsOptGetResponse().getGoodsOptList());
    }

    /**
     * 生成第三级标签
     */
    private static void addLevel3() throws Exception {
        String sql = "select * from sm_opt where level = 2";
        String sql2 = "select * from sm_opt where level = 2";
        RowSet rowSet = mysql.query(sql);
        while(rowSet.next()){
            Integer pddOptionId = rowSet.getInt("pdd_opt_id");
            PddGoodsOptGetRequest request = new PddGoodsOptGetRequest();
            request.setParentOptId(pddOptionId);
            PddGoodsOptGetResponse response = client.syncInvoke(request);
            save(rowSet.getInt("id"),response.getGoodsOptGetResponse().getGoodsOptList());
        }
    }

    private static void save(Integer parentId, List<PddGoodsOptGetResponse.GoodsOptGetResponseGoodsOptListItem> opts){

        opts.forEach(o ->{
            try {
                System.out.println(JSON.toJSONString(o));
                if(parentId == null) {
                    //保存一级标签
                    int id = mysql.updateWithKey(sql, o.getOptName(), o.getOptId(), o.getParentOptId(), o.getLevel(), new Date(System.currentTimeMillis()));
                    //获取二级标签
                    PddGoodsOptGetRequest request = new PddGoodsOptGetRequest();
                    request.setParentOptId(o.getOptId().intValue());
                    PddGoodsOptGetResponse response = client.syncInvoke(request);
                    save(id,response.getGoodsOptGetResponse().getGoodsOptList());
                }else {
                    //保存二级标签
                    mysql.update(sql, o.getOptName(), o.getOptId(), parentId, o.getLevel(), new Date(System.currentTimeMillis()));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
