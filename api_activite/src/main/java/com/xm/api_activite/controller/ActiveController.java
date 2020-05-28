package com.xm.api_activite.controller;

import cn.hutool.core.date.DateUtil;
import com.xm.api_activite.mapper.SaActiveMapper;
import com.xm.api_activite.mapper.SaBillMapper;
import com.xm.api_activite.mapper.custom.SaBillMapperEx;
import com.xm.api_activite.service.ActiveService;
import com.xm.api_activite.service.BillService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_mq.message.config.PayMqConfig;
import com.xm.comment_serialize.form.BaseForm;
import com.xm.comment_serialize.module.active.bo.BillActiveBo;
import com.xm.comment_serialize.module.active.constant.ActiveConstant;
import com.xm.comment_serialize.module.active.entity.SaActiveEntity;
import com.xm.comment_serialize.module.active.entity.SaBillEntity;
import com.xm.comment_serialize.module.active.entity.SaCashOutRecordEntity;
import com.xm.comment_serialize.module.active.form.ActiveBillListForm;
import com.xm.comment_serialize.module.active.form.ActiveProfitForm;
import com.xm.comment_serialize.module.active.vo.BillActiveVo;
import com.xm.comment_serialize.module.active.vo.CashoutVo;
import com.xm.comment_serialize.module.mall.ex.SmProductEntityEx;
import com.xm.comment_serialize.module.mall.form.ListForm;
import com.xm.comment_serialize.module.pay.message.ActiveAutoEntPayMessage;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.lock.DoWorkWithResult;
import com.xm.comment_utils.lock.LockUtil;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.number.LuckUtil;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/active")
public class ActiveController {

    @Autowired
    private ActiveService activeService;
    @Autowired
    private SaActiveMapper saActiveMapper;
    @Autowired
    private SaBillMapper saBillMapper;
    @Autowired
    private RedisLockRegistry redisLockRegistry;
    @Autowired
    private BillService billService;
    @Autowired
    private SaBillMapperEx saBillMapperEx;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 获取单个商品活动信息
     * @param userId
     * @param productEntityEx
     * @return
     */
    @PostMapping("/goods")
    public SmProductEntityEx goodsActiveInfo(Integer userId, @RequestBody SmProductEntityEx productEntityEx){
        return activeService.goodsInfo(userId,productEntityEx);
    }

    /**
     * 批量获取商品活动信息
     * @param userId
     * @param productEntityExes
     * @return
     */
    @PostMapping("/goods/list")
    public List<SmProductEntityEx> goodsActiveInfo(Integer userId, @RequestBody List<SmProductEntityEx> productEntityExes){
        return activeService.goodsInfo(userId,productEntityExes);
    }

    /**
     * 获取用户活动收入
     * @param activeProfitForm
     * @param bindingResult
     * @return
     */
    @GetMapping("/profit")
    public Map<String,Object> getActiveProfit(@Valid @LoginUser ActiveProfitForm activeProfitForm, BindingResult bindingResult){
        Map<String,Object> result = new HashMap<>();
        Integer profit = billService.getUserActiveProfit(
                activeProfitForm.getUserId(),
                activeProfitForm.getActiveId(),
                activeProfitForm.getState());
        if(activeProfitForm.getActiveId() != null) {
            //添加活动状态信息
            SaActiveEntity saActiveEntity = saActiveMapper.selectByPrimaryKey(activeProfitForm.getActiveId());
            if(saActiveEntity == null) {
                result.put("state", 0);
            }else {
                result.put("state",saActiveEntity.getState());
            }
        }
        result.put("profit",profit == null ? 0 : profit);
        return result;
    }


    /**
     * 领取视频奖励红包
     */
    @GetMapping("/video/red")
    public Object getVideoRed(@LoginUser BaseForm baseForm){
        SaActiveEntity saActiveEntity = saActiveMapper.selectByPrimaryKey(ActiveConstant.VIDEO_ACTIVE_ID);
        if(saActiveEntity == null || saActiveEntity.getState() != 1)
            throw new GlobleException(MsgEnum.ACTIVE_NOTFOUND_ERROR);
        Lock lock = redisLockRegistry.obtain(this.getClass().getSimpleName()+":"+"videored"+baseForm.getUserId());
        return LockUtil.lock(lock,new DoWorkWithResult<Integer>(){
            @Override
            public Integer dowork() {
                SaBillEntity entity = new SaBillEntity();
                entity.setUserId(baseForm.getUserId());
                entity.setActiveId(ActiveConstant.VIDEO_ACTIVE_ID);
                int count = saBillMapper.selectCount(entity);
                if(count > 0)
                    throw new GlobleException(MsgEnum.ACTIVE_ALREADY_USE,"只能领取一次");
                //随机红包
                int money = LuckUtil.get(
                        new LuckUtil.Config(30,40,70),
                        new LuckUtil.Config(40,60,30),
                        new LuckUtil.Config(70,100,5),
                        new LuckUtil.Config(100,200,1)).random();
                SaBillEntity bill = billService.createBill(
                        baseForm.getUserId(),
                        ActiveConstant.VIDEO_ACTIVE_ID,
                        1,
                        money,
                        1,
                        null,
                        null,
                        null);
                //付款
                billService.cashOut(bill,"粉饰生活-看视频领红包活动");
                return money;
            }
        }).get();
    }

    /**
     * 提现
     */
    @GetMapping("/cashout")
    public void cashOut(@LoginUser BaseForm baseForm){
        billService.cashOut(baseForm.getUserId(),baseForm.getOpenId(),baseForm.getIp());
    }

    /**
     * 活动收益列表
     */
    @GetMapping("/bill/list")
    public PageBean<BillActiveVo> billList(@LoginUser ActiveBillListForm listForm){
        List<BillActiveBo> list = billService.getList(listForm.getUserId(),listForm.getState(),listForm.getPageNum(),listForm.getPageSize());
        List<BillActiveVo> listVo = list.stream().map(o->{
            BillActiveVo vo = new BillActiveVo();
            vo.setBillId(o.getId());
            vo.setActiveName(o.getSaActiveEntity().getName());
            vo.setMoney(o.getMoney());
            vo.setState(o.getState());
            vo.setAttachDes(o.getAttachDes());
            vo.setFailReason(o.getFailReason());
            vo.setCreateTime(DateUtil.format(o.getCreateTime(),"MM-dd hh:mm"));
            return vo;
        }).collect(Collectors.toList());
        PageBean pageBean = new PageBean(list);
        pageBean.setList(listVo);
        return pageBean;
    }

    @GetMapping("/cashout/list")
    public PageBean<CashoutVo> cashoutList(@LoginUser ListForm listForm){
        List<SaCashOutRecordEntity> list = billService.getCashoutList(
                listForm.getUserId(),
                null,
                listForm.getPageNum(),
                listForm.getPageSize());
        List<CashoutVo> listVo = list.stream().map(o->{
            CashoutVo vo = new CashoutVo();
            vo.setId(o.getId());
            vo.setMoney(o.getMoney());
            vo.setState(o.getState());
            vo.setCreateTime(DateUtil.format(o.getCreateTime(),"MM-dd hh:mm"));
            return vo;
        }).collect(Collectors.toList());
        PageBean pageBean = new PageBean(list);
        pageBean.setList(listVo);
        return pageBean;
    }
}
