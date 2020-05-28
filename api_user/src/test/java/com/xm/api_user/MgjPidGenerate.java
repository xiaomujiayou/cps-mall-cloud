//package com.xm.api_user;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.mogujie.openapi.exceptions.ApiException;
//import com.mogujie.openapi.response.MgjResponse;
//import com.xm.api_mall.mapper.SmPidMapper;
//import com.xm.comment_api.client.MyMogujieClient;
//import com.xm.comment_api.module.mgj.XiaodianCpsdataChannelgroupGetListRequest;
//import com.xm.comment_api.module.mgj.XiaodianCpsdataChannelgroupSaveRequest;
//import com.xm.comment_serialize.module.mall.entity.SmPidEntity;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import tk.mybatis.mapper.entity.Example;
//
//import java.utils.List;
//import java.utils.concurrent.Executors;
//import java.utils.concurrent.LinkedBlockingQueue;
//import java.utils.concurrent.ThreadPoolExecutor;
//import java.utils.concurrent.TimeUnit;
//import java.utils.stream.Collectors;
//
//public class MgjPidGenerate extends ApiMallApplicationTests {
//
//    @Autowired
//    private SmPidMapper smPidMapper;
//
//    @Autowired
//    private MyMogujieClient myMogujieClient;
//
//    private LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue(10000);
//
//    /**
//     * 生成pid
//     */
//    @Test
//    public void generatePid() throws InterruptedException {
//        Example example = new Example(SmPidEntity.class);
//        example.createCriteria().andIsNull("mgj");
//        List<Integer> ids = smPidMapper.selectByExample(example).stream().map(SmPidEntity::getId).collect(Collectors.toList());
//        ids.stream().forEach(o->{
//            try {
//                queue.put(o);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//
////        for (int i = 0; i < 10000; i++) {
////            queue.put(i+1);
////        }
//
//        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
//        Runnable task = ()->{
//            while (!queue.isEmpty()){
//                try {
//                    Integer id = queue.take();
//                    System.out.println(id);
//                    XiaodianCpsdataChannelgroupSaveRequest request = new XiaodianCpsdataChannelgroupSaveRequest(id + "");
//                    MgjResponse<String> response = myMogujieClient.execute(request);
//                    System.out.println(response.getResult().getData());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        executor.execute(task);
////        executor.execute(task);
////        executor.execute(task);
//        executor.shutdown();
//        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//
////        for (int i = 0; i < 10000; i++) {
////            XiaodianCpsdataChannelgroupSaveRequest request = new XiaodianCpsdataChannelgroupSaveRequest((i+1)+"");
////            try {
////                MgjResponse<String> response = myMogujieClient.execute(request);
////                System.out.println(response.getResult().getData());
////            } catch (ApiException e) {
////                e.printStackTrace();
////            }
////        }
//    }
//
//    @Test
//    public void getList() throws ApiException {
//        Example example = new Example(SmPidEntity.class);
//        example.createCriteria().andIsNull("mgj");
//        List<Integer> ids = smPidMapper.selectByExample(example).stream().map(SmPidEntity::getId).collect(Collectors.toList());
//
//        XiaodianCpsdataChannelgroupGetListRequest request = new XiaodianCpsdataChannelgroupGetListRequest();
//        MgjResponse<String> response = myMogujieClient.execute(request);
//        System.out.println(response.getResult().getData());
//        JSONArray array = JSON.parseArray(response.getResult().getData());
//        array.stream().forEach(o->{
//            JSONObject json = (JSONObject)o;
//            Integer id = json.getInteger("name");
//            String pid = json.getString("id");
//            System.out.println(id);
//            if(ids.contains(id)){
//                SmPidEntity smPidEntity = new SmPidEntity();
//                smPidEntity.setId(id);
//                smPidEntity.setMgj(pid);
//                smPidMapper.updateByPrimaryKeySelective(smPidEntity);
//            }
//        });
//    }
//
//}
