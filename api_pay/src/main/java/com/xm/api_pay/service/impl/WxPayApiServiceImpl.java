package com.xm.api_pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.pagehelper.PageHelper;
import com.sun.media.jfxmedia.logging.Logger;
import com.xm.api_pay.config.WxPayPropertiesEx;
import com.xm.api_pay.mapper.SpWxEntPayOrderInMapper;
import com.xm.api_pay.mapper.SpWxOrderInMapper;
import com.xm.api_pay.mapper.SpWxOrderNotifyMapper;
import com.xm.api_pay.service.WxPayApiService;
import com.xm.comment_mq.message.config.BillMqConfig;
import com.xm.comment_mq.message.config.PayMqConfig;
import com.xm.comment_mq.message.config.UserActionConfig;
import com.xm.comment_mq.message.impl.PayOrderCreateMessage;
import com.xm.comment_mq.message.impl.PayOrderSucessMessage;
import com.xm.comment_serialize.module.pay.entity.SpWxEntPayOrderInEntity;
import com.xm.comment_serialize.module.pay.entity.SpWxOrderInEntity;
import com.xm.comment_serialize.module.pay.entity.SpWxOrderNotifyEntity;
import com.xm.comment_serialize.module.pay.message.EntPayMessage;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.product.GenNumUtil;
import com.xm.comment_utils.response.MsgEnum;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.transaction.TransactionHook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service("wxPayApiService")
public class WxPayApiServiceImpl implements WxPayApiService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SpWxOrderInMapper spWxOrderInMapper;
    @Autowired
    private SpWxOrderNotifyMapper spWxOrderNotifyMapper;
    @Autowired
    private WxPayService wxService;
    @Resource(name = "wxPayPropertiesEx")
    private WxPayPropertiesEx wxPayPropertiesEx;
    @Autowired
    private SpWxEntPayOrderInMapper spWxEntPayOrderInMapper;
    @Value("${spring.profiles.active}")
    private String active;

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public WxPayOrderResultVo collection(SuBillToPayBo suBillToPayBo) throws WxPayException {
        WxPayUnifiedOrderRequest request = createWxOrderRequest(suBillToPayBo);
        WxPayMpOrderResult res = null;
        SpWxOrderInEntity spWxOrderInEntity = null;
        WxPayOrderResultVo wxPayOrderResultVo = null;
        res = wxService.createOrder(request);
        if(StrUtil.isBlank(res.getPackageValue()) || !res.getPackageValue().contains("prepay_id"))
            throw new GlobleException(MsgEnum.WX_PAY_ORDER_CREATE_FAIL);
        spWxOrderInEntity = saveOrder(suBillToPayBo,request,null,null,null,res.getPackageValue());
        wxPayOrderResultVo = new WxPayOrderResultVo();
        wxPayOrderResultVo.setPackageValue(res.getPackageValue());
        wxPayOrderResultVo.setNonceStr(res.getNonceStr());
        wxPayOrderResultVo.setPaySign(res.getPaySign());
        wxPayOrderResultVo.setTimeStamp(res.getTimeStamp());
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new PayOrderCreateMessage(suBillToPayBo.getUserId(),suBillToPayBo,spWxOrderInEntity,suBillToPayBo,wxPayOrderResultVo));
        return wxPayOrderResultVo;
    }

    /**
     * 保存记录
     * @param request
     * @param returnMsg
     * @param errCode
     * @param errCodeDes
     */
    private SpWxOrderInEntity saveOrder(SuBillToPayBo suBillToPayBo,WxPayUnifiedOrderRequest request, String returnMsg, String errCode, String errCodeDes,String packageVal) {
        SpWxOrderInEntity spWxOrderInEntity = new SpWxOrderInEntity();
        spWxOrderInEntity.setReqBo(JSON.toJSONString(suBillToPayBo));
        BeanUtil.copyProperties(request,spWxOrderInEntity);
        spWxOrderInEntity.setState(0);
        spWxOrderInEntity.setPackageVal(packageVal);
        JSONObject errMsgJson = new JSONObject();
        errMsgJson.put("returnMsg",returnMsg);
        errMsgJson.put("errCode",errCode);
        errMsgJson.put("errCodeDes",errCodeDes);
        spWxOrderInEntity.setErrMsg(errMsgJson.isEmpty()?null:errMsgJson.toJSONString());
        spWxOrderInEntity.setCreateTime(new Date());
        spWxOrderInMapper.insertSelective(spWxOrderInEntity);
        return  spWxOrderInEntity;
    }


    /**
     * 根据账单生成同一下单订单
     * @param suBillToPayBo
     * @return
     */
    private WxPayUnifiedOrderRequest createWxOrderRequest(SuBillToPayBo suBillToPayBo){
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
        wxPayUnifiedOrderRequest.setAppid(wxPayPropertiesEx.getAppId());
        wxPayUnifiedOrderRequest.setMchId(wxPayPropertiesEx.getMchId());
        wxPayUnifiedOrderRequest.setBody(suBillToPayBo.getDes());
        Map<String,Object> attach = new HashMap<>();
        attach.put("billId",suBillToPayBo.getId());
        attach.put("userId",suBillToPayBo.getUserId());
        wxPayUnifiedOrderRequest.setAttach(JSON.toJSONString(attach));
        wxPayUnifiedOrderRequest.setOutTradeNo(GenNumUtil.genOrderNum());
        wxPayUnifiedOrderRequest.setTotalFee(suBillToPayBo.getMoney());
        wxPayUnifiedOrderRequest.setSpbillCreateIp(suBillToPayBo.getClientIp());
        wxPayUnifiedOrderRequest.setNotifyUrl(wxPayPropertiesEx.getNotifyUrl());
        wxPayUnifiedOrderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
        wxPayUnifiedOrderRequest.setOpenid(suBillToPayBo.getOpenId());
        return wxPayUnifiedOrderRequest;
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void payment(EntPayMessage entPayMessage) throws WxPayException {
        SpWxEntPayOrderInEntity record = new SpWxEntPayOrderInEntity();
        record.setBillPayId(entPayMessage.getScBillPayEntity().getId());
        if(spWxEntPayOrderInMapper.selectCount(record) > 0)
            return;
        EntPayRequest request = new EntPayRequest();
        request.setMchAppid(wxPayPropertiesEx.getAppId());
        request.setMchId(wxPayPropertiesEx.getMchId());
        request.setPartnerTradeNo(StrUtil.isBlank(entPayMessage.getRetryTradeNo()) ? GenNumUtil.genWxEntPayOrderNum() : entPayMessage.getRetryTradeNo());
        request.setOpenid(entPayMessage.getScBillPayEntity().getOpenId());
        request.setCheckName("NO_CHECK");
        request.setDescription(entPayMessage.getDesc());
        request.setSpbillCreateIp(entPayMessage.getIp());
        request.setAmount(entPayMessage.getScBillPayEntity().getTotalMoney());
        EntPayResult result = null;
        SpWxEntPayOrderInEntity spWxEntPayOrderInEntity = null;
        try {
            if(CollUtil.newArrayList(active.split(",")).contains("dev")){
                result = new EntPayResult();
            }
            if(CollUtil.newArrayList(active.split(",")).contains("prod")){
                result = wxService.getEntPayService().entPay(request);
            }
            spWxEntPayOrderInEntity = entPay(entPayMessage,request,result,null);
        } catch (WxPayException e) {
            spWxEntPayOrderInEntity = entPay(entPayMessage,request,null,e);
            log.error("微信企业付款失败 信息：{} error：{}",JSON.toJSONString(spWxEntPayOrderInEntity),e);
        } finally {
            rabbitTemplate.convertAndSend(PayMqConfig.EXCHANGE_ENT,"",spWxEntPayOrderInEntity);
        }
    }

    //保存企业付款记录
    private SpWxEntPayOrderInEntity entPay(EntPayMessage entPayMessage,EntPayRequest request,EntPayResult result,WxPayException e){
        SpWxEntPayOrderInEntity entity = new SpWxEntPayOrderInEntity();
        BeanUtil.copyProperties(request,entity);
        entity.setBillPayId(entPayMessage.getScBillPayEntity().getId());
        entity.setBillIds(entPayMessage.getScBillPayEntity().getBillIds());
        entity.setDes(request.getDescription());
        entity.setCreateTime(new Date());
        if(!(result == null)) {
            BeanUtil.copyProperties(result, entity);
            entity.setState(1);
        }
        if(e != null){
            entity.setReturnCode(e.getReturnCode());
            entity.setReturnMsg(e.getReturnMsg());
            entity.setResultCode(e.getResultCode());
            entity.setErrCode(e.getErrCode());
            entity.setErrCodeDes(e.getErrCodeDes());
            entity.setState(2);
        }
        spWxEntPayOrderInMapper.insertUseGeneratedKeys(entity);
        return entity;
    }

    @Override
    public void orderNotify(WxPayOrderNotifyResult notifyResult) {
        //判断是否已处理
        SpWxOrderNotifyEntity record = new SpWxOrderNotifyEntity();
        record.setOutTradeNo(notifyResult.getOutTradeNo());
        if(spWxOrderNotifyMapper.selectCount(record) > 0){
            log.debug("微信支付回调：该支付信息已被处理 单号：[{}]",notifyResult.getOutTradeNo());
            return;
        }
        SpWxOrderNotifyEntity spWxOrderNotifyEntity = new SpWxOrderNotifyEntity();
        BeanUtil.copyProperties(notifyResult,spWxOrderNotifyEntity);
        spWxOrderNotifyEntity.setCreateTime(new Date());
        spWxOrderNotifyMapper.insertSelective(spWxOrderNotifyEntity);
        rabbitTemplate.convertAndSend(PayMqConfig.EXCHANGE,PayMqConfig.KEY_WX_NOTIFY,spWxOrderNotifyEntity);
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public void onPaySucess(SpWxOrderNotifyEntity spWxOrderNotifyEntity) {
        Integer userId = JSON.parseObject(spWxOrderNotifyEntity.getAttach()).getInteger("userId");
        PageHelper.startPage(1,1).count(false);
        SpWxOrderInEntity spWxOrderInEntity = new SpWxOrderInEntity();
        spWxOrderInEntity.setOutTradeNo(spWxOrderNotifyEntity.getOutTradeNo());
        spWxOrderInEntity = spWxOrderInMapper.selectOne(spWxOrderInEntity);
        SuBillToPayBo suBillToPayBo = JSON.parseObject(spWxOrderInEntity.getReqBo(),SuBillToPayBo.class);
        rabbitTemplate.convertAndSend(BillMqConfig.EXCHANGE,BillMqConfig.KEY_PAY_SUCESS,suBillToPayBo);
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new PayOrderSucessMessage(userId,suBillToPayBo,spWxOrderNotifyEntity));
    }

}
