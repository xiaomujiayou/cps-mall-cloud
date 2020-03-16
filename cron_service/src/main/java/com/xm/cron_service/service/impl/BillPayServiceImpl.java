package com.xm.cron_service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.DatePrinter;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.PageUtil;
import com.github.pagehelper.PageHelper;
import com.xm.comment_mq.message.config.PayMqConfig;
import com.xm.comment_serialize.module.cron.entity.ScBillPayEntity;
import com.xm.comment_serialize.module.cron.entity.ScWaitPayBillEntity;
import com.xm.comment_serialize.module.cron.vo.ScBillPayVo;
import com.xm.comment_serialize.module.pay.entity.SpWxEntPayOrderInEntity;
import com.xm.comment_serialize.module.pay.message.EntPayMessage;
import com.xm.comment_serialize.module.user.entity.SuBillPayEntity;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.cron_service.config.ProfitProperty;
import com.xm.cron_service.mapper.ScBillPayMapper;
import com.xm.cron_service.mapper.ScWaitPayBillMapper;
import com.xm.cron_service.mapper.custom.ScBillPayMapperEx;
import com.xm.cron_service.service.BillPayService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.orderbyhelper.OrderByHelper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("billPayService")
public class BillPayServiceImpl implements BillPayService {

    @Autowired
    private ScBillPayMapper scBillPayMapper;
    @Autowired
    private ScBillPayMapperEx scBillPayMapperEx;
    @Autowired
    private ScWaitPayBillMapper scWaitPayBillMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Lazy
    @Autowired
    private BillPayService billPayService;
    @Autowired
    private ProfitProperty profitProperty;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onEntPayResult(SpWxEntPayOrderInEntity spWxEntPayOrderInEntity) {
        ScBillPayEntity scBillPayEntity = scBillPayMapper.selectByPrimaryKey(spWxEntPayOrderInEntity.getBillPayId());
        if(scBillPayEntity == null || scBillPayEntity.getState() != 1)
            return;
        if(spWxEntPayOrderInEntity.getState() == 1){
            billPayService.onEntPaySucess(scBillPayEntity,spWxEntPayOrderInEntity);
        }else {
            billPayService.onEntPayFail(scBillPayEntity,spWxEntPayOrderInEntity);
        }
    }

    /**
     * 支付失败
     * @param scBillPayEntity
     * @param spWxEntPayOrderInEntity
     */
    public void onEntPayFail(ScBillPayEntity scBillPayEntity,SpWxEntPayOrderInEntity spWxEntPayOrderInEntity){
        scBillPayEntity.setState(3);
        scBillPayEntity.setFailReason(spWxEntPayOrderInEntity.getErrCodeDes());
        updateScBillPay(scBillPayEntity,spWxEntPayOrderInEntity,4);
    }

    /**
     * 支付成功
     * @param scBillPayEntity
     * @param spWxEntPayOrderInEntity
     */
    public void onEntPaySucess(ScBillPayEntity scBillPayEntity,SpWxEntPayOrderInEntity spWxEntPayOrderInEntity){
        scBillPayEntity.setState(2);
        updateScBillPay(scBillPayEntity,spWxEntPayOrderInEntity,3);
    }

    private void updateScBillPay(ScBillPayEntity scBillPayEntity,SpWxEntPayOrderInEntity spWxEntPayOrderInEntity,Integer waitPayBillState){
        scBillPayEntity.setUpdateTime(new Date());
        scBillPayEntity.setProcessId(spWxEntPayOrderInEntity.getId());
        scBillPayEntity.setProcessSn(spWxEntPayOrderInEntity.getPartnerTradeNo());
        scBillPayEntity.setPaySysSn(spWxEntPayOrderInEntity.getPaymentNo());
        scBillPayMapper.updateByPrimaryKeySelective(scBillPayEntity);
        Example example = new Example(ScWaitPayBillEntity.class);
        example.createCriteria().andIn("billId", CollUtil.newArrayList(scBillPayEntity.getBillIds().split(",")));
        ScWaitPayBillEntity record = new ScWaitPayBillEntity();
        record.setState(waitPayBillState);
        scWaitPayBillMapper.updateByExampleSelective(record,example);
    }

    @Transactional
    @Override
    public List<ScBillPayEntity> genPayBill(String billPayName,Integer minMoney, Integer pageNum, Integer pageSize, Date timeline) {
        List<ScBillPayEntity> scBillPayEntities = scBillPayMapperEx.genPayBill(minMoney,PageUtil.getStart(pageNum,pageSize),pageSize,timeline);
        scBillPayEntities.stream().forEach( o -> {
            List<String> billIds = CollUtil.newArrayList(o.getBillIds().split(","));
            Example example = new Example(ScWaitPayBillEntity.class);
            example.createCriteria().andIn("billId",billIds);
            ScWaitPayBillEntity record = new ScWaitPayBillEntity();
            record.setState(2);
            scWaitPayBillMapper.updateByExampleSelective(record,example);
            o.setState(1);
            o.setName(billPayName);
            o.setUpdateTime(new Date());
            o.setCreateTime(o.getUpdateTime());
            scBillPayMapper.insertUseGeneratedKeys(o);
        });
        return scBillPayEntities;
    }

    @Override
    public void commission() {
        //发放截至日期
        Date timeline = new Date();
//        Date timeline = DateUtil.parse(DateUtil.today());
        //支付简介，会出现在微信支付页面上
        String payDesc = profitProperty.getPayDesc().replace("{time}",DateUtil.format(timeline, "yyyy-MM-dd hh:mm"));
        int total = scBillPayMapperEx.totalGenPayBill(profitProperty.getMinMoney(),timeline);
        double count = Math.ceil(NumberUtil.div(total,profitProperty.getPageSize().intValue()));
        for (int i = 0; i < count; i++) {
            try {
                billPayService.genPayBill(payDesc,profitProperty.getMinMoney(),i+1,profitProperty.getPageSize(),timeline).stream().forEach(o->{
                    EntPayMessage entPayMessage = new EntPayMessage();
                    entPayMessage.setDesc(payDesc);
                    entPayMessage.setIp(profitProperty.getIp());
                    entPayMessage.setScBillPayEntity(o);
                    rabbitTemplate.convertAndSend(PayMqConfig.EXCHANGE,PayMqConfig.KEY_WX_ENT_PAY,entPayMessage);
                });
            }catch (Exception e){
                log.error("返佣失败 {}",e);
            }
        }
    }

    @Override
    public PageBean<ScBillPayVo> list(Integer userId, Integer pageNum, Integer pageSize) {
        ScBillPayEntity record = new ScBillPayEntity();
        record.setUserId(userId);
        PageHelper.startPage(pageNum,pageSize);
        OrderByHelper.orderBy("create_time desc");
        List<ScBillPayEntity> scBillPayEntities = scBillPayMapper.select(record);
        if(scBillPayEntities == null)
            return null;
        PageBean<ScBillPayEntity> scBillPayEntityPageBean = new PageBean<>(scBillPayEntities);
        List<ScBillPayVo> scBillPayVos = scBillPayEntities.stream().map(o->{
            ScBillPayVo scBillPayVo = new ScBillPayVo();
            BeanUtil.copyProperties(o,scBillPayVo);
//            scBillPayVo.setName("发放佣金");
            if(o.getState() == 3)
                return null;
//                scBillPayVo.setFailReason("系统出错，请稍后。。");
            return scBillPayVo;
        }).collect(Collectors.toList());
        scBillPayVos = CollUtil.removeNull(scBillPayVos);
        PageBean<ScBillPayVo> payVoPageBean = new PageBean<>(scBillPayVos);
        payVoPageBean.setPageNum(scBillPayEntityPageBean.getPageNum());
        payVoPageBean.setPageSize(scBillPayEntityPageBean.getPageSize());
        payVoPageBean.setTotal(scBillPayEntityPageBean.getTotal());
        return payVoPageBean;
    }
}
