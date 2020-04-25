package com.xm.cron_service.service.impl;

import com.xm.cron_service.CronServiceApplicationTests;
import com.xm.cron_service.service.TaskService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class WphTaskServiceImplTest extends CronServiceApplicationTests {

    @Resource
    private TaskService wphTaskService;

    @Test
    public void test() {
//        String xiaDan = "{\n" +
//                "\t\"channelTag\": \"663fede9d45d67b93a055c00e04f8305\",\n" +
//                "\t\"commission\": \"0.0\",\n" +
//                "\t\"commissionEnterTime\": 31507200000,\n" +
//                "\t\"detailList\": [{\n" +
//                "\t\t\"commCode\": \"338\",\n" +
//                "\t\t\"commName\": \"自营|男上装\",\n" +
//                "\t\t\"commission\": \"0.00\",\n" +
//                "\t\t\"commissionRate\": \"0.00\",\n" +
//                "\t\t\"commissionTotalCost\": \"49.00\",\n" +
//                "\t\t\"goodsCount\": 1,\n" +
//                "\t\t\"goodsId\": \"6918670965253259734\",\n" +
//                "\t\t\"goodsName\": \"2020夏季新款韩版创意印花潮流条纹袖元素男式t恤短袖t恤男\",\n" +
//                "\t\t\"goodsThumb\": \"https://a.vpimg2.com/upload/merchandise/pdcvis/2020/04/22/11/7b476196-0b3d-44f7-bba1-9989417e4dea_750x750_50.jpg\",\n" +
//                "\t\t\"orderSource\": \"其它\",\n" +
//                "\t\t\"sizeId\": \"6918708378963165526\",\n" +
//                "\t\t\"status\": 1\n" +
//                "\t}],\n" +
//                "\t\"isPrepay\": 0,\n" +
//                "\t\"lastUpdateTime\": 1587765469000,\n" +
//                "\t\"newCustomer\": 2,\n" +
//                "\t\"orderSn\": \"20042521766169\",\n" +
//                "\t\"orderSource\": \"其它\",\n" +
//                "\t\"orderSubStatusName\": \"已付款\",\n" +
//                "\t\"orderTime\": 1587765461000,\n" +
//                "\t\"pid\": \"663fede9d45d67b93a055c00e04f8305\",\n" +
//                "\t\"selfBuy\": 1,\n" +
//                "\t\"settled\": 0,\n" +
//                "\t\"status\": 1\n" +
//                "}";
//        wphTaskService.test(xiaDan);
        String shiXiao = "{\n" +
                "\t\"channelTag\": \"663fede9d45d67b93a055c00e04f8305\",\n" +
                "\t\"commission\": \"0.0\",\n" +
                "\t\"commissionEnterTime\": 31507200000,\n" +
                "\t\"detailList\": [{\n" +
                "\t\t\"commCode\": \"338\",\n" +
                "\t\t\"commName\": \"自营|男上装\",\n" +
                "\t\t\"commission\": \"0.00\",\n" +
                "\t\t\"commissionRate\": \"0.00\",\n" +
                "\t\t\"commissionTotalCost\": \"49.00\",\n" +
                "\t\t\"goodsCount\": 1,\n" +
                "\t\t\"goodsId\": \"6918670965253259734\",\n" +
                "\t\t\"goodsName\": \"2020夏季新款韩版创意印花潮流条纹袖元素男式t恤短袖t恤男\",\n" +
                "\t\t\"goodsThumb\": \"https://a.vpimg2.com/upload/merchandise/pdcvis/2020/04/22/11/7b476196-0b3d-44f7-bba1-9989417e4dea_750x750_50.jpg\",\n" +
                "\t\t\"orderSource\": \"其它\",\n" +
                "\t\t\"sizeId\": \"6918708378963165526\",\n" +
                "\t\t\"status\": 0\n" +
                "\t}],\n" +
                "\t\"isPrepay\": 0,\n" +
                "\t\"lastUpdateTime\": 1587767470000,\n" +
                "\t\"newCustomer\": 2,\n" +
                "\t\"orderSn\": \"20042521766169\",\n" +
                "\t\"orderSource\": \"其它\",\n" +
                "\t\"orderSubStatusName\": \"已失效\",\n" +
                "\t\"orderTime\": 1587765461000,\n" +
                "\t\"pid\": \"663fede9d45d67b93a055c00e04f8305\",\n" +
                "\t\"selfBuy\": 1,\n" +
                "\t\"settled\": 0,\n" +
                "\t\"status\": 0\n" +
                "}";
        wphTaskService.test(shiXiao);
    }
}