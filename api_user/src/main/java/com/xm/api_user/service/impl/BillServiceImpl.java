package com.xm.api_user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.xm.api_user.mapper.SuBillMapper;
import com.xm.api_user.mapper.SuOrderMapper;
import com.xm.api_user.mapper.SuUserMapper;
import com.xm.api_user.service.BillService;
import com.xm.comment_feign.module.mall.feign.MallFeignClient;
import com.xm.comment_mq.message.config.BillMqConfig;
import com.xm.comment_serialize.module.lottery.ex.SlPropSpecEx;
import com.xm.comment_serialize.module.mall.constant.ConfigEnmu;
import com.xm.comment_serialize.module.mall.constant.ConfigTypeConstant;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.constant.BillStateConstant;
import com.xm.comment_serialize.module.user.constant.BillTypeConstant;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_serialize.module.user.vo.BillVo;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.product.GenNumUtil;
import com.xm.comment_utils.project.PromotionUtils;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.*;
import java.util.stream.Collectors;

@Service("billService")
public class BillServiceImpl implements BillService {

    @Autowired
    private SuBillMapper suBillMapper;
    @Autowired
    private MallFeignClient mallFeignClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SuUserMapper suUserMapper;
    @Autowired
    private SuOrderMapper suOrderMapper;
    @Lazy
    @Autowired
    private BillService billService;

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void createByOrder(SuOrderEntity order) {
        //订单相关账单存在则返回
        List<SuBillEntity> userRelatedBills = getOrderRelatedBill(order);
        if(userRelatedBills != null && userRelatedBills.size() > 0)
            return;
        JSONObject params = JSON.parseObject(order.getCustomParameters());
        Integer shareUserId = params.getInteger("shareUserId");
        if(shareUserId == null){
            //生成正常下单账单
            createNormalOrderBill(order);
        }else {
            //生成分享订单账单
            createShareOrderBill(shareUserId,order);
        }
    }

    /**
     * 创建正常购买流程账单
     * 获取系统自购费率 -> 计算购买用户收益账单 -> 获取系统代理费率 -> 计算上级代理收益账单
     * @param order
     */
    private void createNormalOrderBill(SuOrderEntity order){
        //属于正常购买订单
        //生成购买者账单
        Integer buyUserRate = Integer.valueOf(mallFeignClient.getOneConfig(
                order.getUserId(),
                ConfigEnmu.PRODUCT_BUY_RATE.getName(),
                ConfigTypeConstant.SYS_CONFIG).getVal());
        SuBillEntity buyUserBill = createOrderBill(order.getUserId(),order,BillTypeConstant.BUY_NORMAL,buyUserRate,null);
        billService.addBill(buyUserBill);
        //生成代理账单
        //获取代理层级
        Integer proxyLevel = Integer.valueOf(mallFeignClient.getOneConfig(
                null,
                ConfigEnmu.PROXY_LEVEL.getName(),
                ConfigTypeConstant.SYS_CONFIG).getVal());
        List<Integer> proxyRate = Lists.newArrayList(mallFeignClient.getOneConfig(
                null,
                ConfigEnmu.PRODUCT_PROXY_RATE.getName(),
                ConfigTypeConstant.SYS_CONFIG).getVal().split(","))
                .stream()
                .map(o->{return Integer.valueOf(o);})
                .collect(Collectors.toList());

        List<SuUserEntity> proxyUsers = getParentUser(order.getUserId(),proxyLevel);
        for (int i = 0; i < proxyLevel; i++) {
            if(proxyUsers == null || proxyUsers.size() <= 0 || i > proxyUsers.size() - 1  || proxyUsers.get(i) == null)
                break;
            SuBillEntity proxyBill = createOrderBill(proxyUsers.get(i).getId(),order,BillTypeConstant.PROXY_PROFIT,proxyRate.get(i),i==0?order.getUserId():proxyUsers.get(i-1).getId());
            billService.addBill(proxyBill);
        }
    }

    /**
     * 创建分享订单所属账单
     * 获取系统购买者分享订单费率 -> 计算购买者收益账单 -> 获取系统分享者订单费率 -> 计算分享者收益账单
     * @param shareUserId
     * @param order
     */
    private void createShareOrderBill(Integer shareUserId,SuOrderEntity order){
        //属于分享订单
        //生成分享者账单
        Integer shareUserRate = Integer.valueOf(mallFeignClient.getOneConfig(
                shareUserId,
                ConfigEnmu.PRODUCT_SHARE_USER_RATE.getName(),
                ConfigTypeConstant.SYS_CONFIG).getVal());
        SuBillEntity shareUserBill = createOrderBill(shareUserId,order,BillTypeConstant.SHARE_PROFIT,shareUserRate,order.getUserId());
        billService.addBill(shareUserBill);
        //生成购买者订单
        Integer buyUserRate = Integer.valueOf(mallFeignClient.getOneConfig(
                order.getUserId(),
                ConfigEnmu.PRODUCT_SHARE_BUY_RATE.getName(),
                ConfigTypeConstant.SYS_CONFIG).getVal());
        SuBillEntity buyUserBill = createOrderBill(order.getUserId(),order,BillTypeConstant.BUY_SHARE,buyUserRate,null);
        billService.addBill(buyUserBill);
    }

    /**
     * 获取上级代理
     * @param userId
     * @param level
     * @return
     */
    private List<SuUserEntity> getParentUser(Integer userId, Integer level){
        List<SuUserEntity> result = new ArrayList<>();
        Integer parentId = suUserMapper.selectByPrimaryKey(userId).getParentId();
        for (int i = 0; i < level; i++) {
            if(parentId == null)
                return result;
            SuUserEntity parentUser = suUserMapper.selectByPrimaryKey(parentId);
            result.add(parentUser);
            parentId = parentUser.getParentId();
        }
        return result;
    }

    /**
     * 通过订单和费率生成账单
     * @param userId        :账单所属用户
     * @param order
     * @param rate
     * @return
     */
    private SuBillEntity createOrderBill(Integer userId,SuOrderEntity order,Integer billType,Integer rate,Integer formUserId){
        SuBillEntity bill = new SuBillEntity();
        bill.setUserId(userId);
        bill.setFromUserId(formUserId);
        bill.setMoney(PromotionUtils.calcByRate(order.getPromotionAmount(),rate));
        bill.setType(billType);
        bill.setAttach(order.getId());
        bill.setPromotionRate(rate);
        bill.setIncome(1);
        bill.setState(BillTypeConstant.BUY_NORMAL);
        bill.setFailReason(order.getFailReason());
        return bill;
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void payOutOrderBill(SuOrderEntity order) {
        List<SuBillEntity> suBillEntities = getOrderRelatedBill(order);
        suBillEntities.stream().forEach(o->{
            billService.updateBillState(o,BillStateConstant.READY,null);
            rabbitTemplate.convertAndSend(BillMqConfig.EXCHANGE,BillMqConfig.KEY,o);
        });
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void invalidOrderBill(SuOrderEntity order) {
        List<SuBillEntity> suBillEntities = getOrderRelatedBill(order);
        suBillEntities.stream().forEach(o->{
            billService.updateBillState(o,BillStateConstant.FAIL,order.getFailReason());
        });
    }
    /**
     * 获取订单相关账单
     * @param suOrderEntity
     * @return
     */
    private List<SuBillEntity> getOrderRelatedBill(SuOrderEntity suOrderEntity){
        SuBillEntity example = new SuBillEntity();
        example.setAttach(suOrderEntity.getId());
        return suBillMapper.select(example);
    }

    @Override
    public PageBean<BillVo> getList(Integer userId, Integer state, Integer type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        OrderByHelper.orderBy("create_time desc");
        List<SuBillEntity> suBillEntities = null;

        //自购订单包含分享自购
        if(type != null && type == 1){
            Example example = new Example(SuBillEntity.class);
            Example.Criteria criteria =  example.createCriteria()
                    .andEqualTo("userId",userId)
                    .andIn("type",Arrays.asList(BillTypeConstant.BUY_NORMAL,BillTypeConstant.BUY_SHARE));
            if(state != null)
                criteria.andEqualTo("state",state);
            suBillEntities = suBillMapper.selectByExample(example);

        }else {
            SuBillEntity example = new SuBillEntity();
            example.setUserId(userId);
            example.setState(state);
            example.setType(type);
            suBillEntities = suBillMapper.select(example);
        }

        PageBean suBillPageBean = new PageBean(suBillEntities);
        Map<Integer,List<SuBillEntity>> groupMap = suBillEntities.stream().collect(Collectors.groupingBy(SuBillEntity::getType));
        Map<Integer,String> orderIdMap = new HashMap<>();
        Map<Integer,String> userIdMap = new HashMap<>();
        groupMap.entrySet().forEach(o-> {
            //查询订单相关信息
            if(Arrays.asList(BillTypeConstant.BUY_NORMAL,BillTypeConstant.BUY_SHARE,BillTypeConstant.SHARE_PROFIT).contains(o.getKey())){
                o.getValue().forEach(j ->{
                    orderIdMap.put(j.getId(),j.getAttach().toString());
                });
            }else if(Arrays.asList(BillTypeConstant.PROXY_PROFIT).contains(o.getKey())){
                o.getValue().forEach(j ->{
                    userIdMap.put(j.getId(),j.getFromUserId().toString());
                });
            }
        });
        List<SuUserEntity> suUserEntities = !userIdMap.isEmpty() ? suUserMapper.selectByIds(String.join(",",String.join(",",userIdMap.values()))):new ArrayList<>();
        List<SuOrderEntity> suOrderEntities = !orderIdMap.isEmpty() ? suOrderMapper.selectByIds(String.join(",",String.join(",",orderIdMap.values()))):new ArrayList<>();
        List<BillVo> billVos = suBillEntities.stream().map(o->{
            SuOrderEntity suOrderEntity = null;
            SuUserEntity suUserEntity = null;
            suOrderEntity = orderIdMap.get(o.getId()) == null?null: suOrderEntities.stream().filter(j->{return j.getId().equals(Integer.valueOf(orderIdMap.get(o.getId())));}).findFirst().orElse(null);
            suUserEntity = userIdMap.get(o.getId()) == null?null:suUserEntities.stream().filter(j->{return j.getId().equals(Integer.valueOf(userIdMap.get(o.getId())));}).findFirst().orElse(null);
            if(ObjectUtil.isAllEmpty(suOrderEntity,suUserEntity))
                return null;
            return covertBillVo(o,suOrderEntity,suUserEntity);
        }).collect(Collectors.toList());
        CollUtil.removeNull(billVos);
        suBillPageBean.setList(billVos);
        return suBillPageBean;
    }

    @Override
    public void addBill(SuBillEntity suBillEntity) {
        suBillMapper.insertUseGeneratedKeys(completeBillInfo(suBillEntity));
    }
    private SuBillEntity completeBillInfo(SuBillEntity suBillEntity){
        suBillEntity.setBillSn(GenNumUtil.genBillNum());
        suBillEntity.setCreateTime(new Date());
        suBillEntity.setUpdateTime(suBillEntity.getCreateTime());
        return suBillEntity;
    }

    @Override
    public void updateBillState(SuBillEntity suBillEntity, Integer newState,String failReason) {
        suBillEntity.setState(newState);
        suBillEntity.setFailReason(failReason);
        suBillEntity.setUpdateTime(new Date());
        suBillMapper.updateByPrimaryKeySelective(suBillEntity);
    }

    @Override
    public SuBillToPayBo createByProp(SlPropSpecEx slPropSpecEx) {
        SuBillEntity suBillEntity = new SuBillEntity();
        suBillEntity.setUserId(slPropSpecEx.getSuUserEntity().getId());
        suBillEntity.setMoney(slPropSpecEx.getPrice());
        suBillEntity.setType(BillTypeConstant.BUY_LOTTERY);
        suBillEntity.setAttach(slPropSpecEx.getId());
        suBillEntity.setState(6);
        suBillEntity.setIncome(2);
        suBillEntity.setDes(slPropSpecEx.getSlPropEntity().getName() + "-" + slPropSpecEx.getName());
        addBill(suBillEntity);
        //订单支付超时
        rabbitTemplate.convertAndSend(BillMqConfig.EXCHANGE,BillMqConfig.KEY_PAY_OVERTIME,suBillEntity);
        SuBillToPayBo suBillToPayBo = new SuBillToPayBo();
        BeanUtil.copyProperties(suBillEntity,suBillToPayBo);
        suBillToPayBo.setClientIp(slPropSpecEx.getClientIp());
        suBillToPayBo.setOpenId(slPropSpecEx.getSuUserEntity().getOpenId());
        return suBillToPayBo;
    }

    @Override
    public void payOvertime(SuBillEntity suBillEntity) {
        SuBillEntity suBillEntity1 = suBillMapper.selectByPrimaryKey(suBillEntity.getId());
        if(suBillEntity1 == null || suBillEntity1.getState().equals(7))
            return;
        suBillMapper.deleteByPrimaryKey(suBillEntity.getId());
    }

    @Override
    public void paySucess(SuBillEntity suBillEntity) {
        SuBillEntity suBillEntity1 = suBillMapper.selectByPrimaryKey(suBillEntity.getId());
        if(suBillEntity1 == null)
            return;
        suBillEntity1.setState(7);
        suBillEntity1.setUpdateTime(new Date());
        suBillMapper.updateByPrimaryKeySelective(suBillEntity1);
    }

    private BillVo covertBillVo(SuBillEntity suBillEntity,SuOrderEntity suOrderEntity,SuUserEntity suUserEntity){
        BillVo billVo = new BillVo();
        billVo.setMoney(suBillEntity.getMoney());
        billVo.setType(suBillEntity.getType());
        billVo.setState(suBillEntity.getState());
        billVo.setIncome(suBillEntity.getIncome());
        billVo.setTime(DateUtil.format(suBillEntity.getCreateTime(),"MM-dd HH:mm"));
        billVo.setFailReason(suBillEntity.getFailReason());
        billVo.setHeadImg(suUserEntity == null?null:suUserEntity.getHeadImg());
        billVo.setNickname(suUserEntity == null?null:suUserEntity.getNickname());
        billVo.setGoodsId(suOrderEntity == null?null:suOrderEntity.getProductId());
        billVo.setGoodsName(suOrderEntity == null?null:suOrderEntity.getProductName());
        billVo.setPlatformType(suOrderEntity == null?null:suOrderEntity.getPlatformType());
        return billVo;
    }

}
