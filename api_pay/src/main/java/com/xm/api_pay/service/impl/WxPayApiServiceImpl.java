package com.xm.api_pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.pagehelper.PageHelper;
import com.xm.api_pay.config.WxPayPropertiesEx;
import com.xm.api_pay.mapper.SpWxOrderInMapper;
import com.xm.api_pay.mapper.SpWxOrderNotifyMapper;
import com.xm.api_pay.service.WxPayApiService;
import com.xm.comment_mq.config.UserActionConfig;
import com.xm.comment_mq.message.impl.PayOrderCreateMessage;
import com.xm.comment_mq.message.impl.PayOrderSucessMessage;
import com.xm.comment_serialize.module.pay.entity.SpWxOrderInEntity;
import com.xm.comment_serialize.module.pay.entity.SpWxOrderNotifyEntity;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public WxPayOrderResultVo collection(SuBillToPayBo suBillToPayBo) throws WxPayException {
        WxPayUnifiedOrderRequest request = createWxOrderRequest(suBillToPayBo);
        WxPayUnifiedOrderResult res = null;
        SpWxOrderInEntity spWxOrderInEntity = null;
        WxPayOrderResultVo wxPayOrderResultVo = null;
        res = wxService.createOrder(request);
        spWxOrderInEntity = saveOrder(suBillToPayBo,request,res.getReturnMsg(),res.getErrCode(),res.getErrCodeDes());
        if(res.getReturnCode().equals("SUCCESS") && res.getResultCode().equals("SUCCESS")){
            wxPayOrderResultVo = new WxPayOrderResultVo();
            wxPayOrderResultVo.setPrepayId(res.getPrepayId());
            rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new PayOrderCreateMessage(suBillToPayBo.getUserId(),suBillToPayBo,spWxOrderInEntity,suBillToPayBo,wxPayOrderResultVo));
            return wxPayOrderResultVo;
        }
        throw new GlobleException(MsgEnum.WX_PAY_ORDER_CREATE_FAIL,"a:b","c:d");
    }

    /**
     * 保存记录
     * @param request
     * @param returnMsg
     * @param errCode
     * @param errCodeDes
     */
    private SpWxOrderInEntity saveOrder(SuBillToPayBo suBillToPayBo,WxPayUnifiedOrderRequest request, String returnMsg, String errCode, String errCodeDes) {
        SpWxOrderInEntity spWxOrderInEntity = new SpWxOrderInEntity();
        spWxOrderInEntity.setReqBo(JSON.toJSONString(suBillToPayBo));
        BeanUtil.copyProperties(request,spWxOrderInEntity);
        spWxOrderInEntity.setState(0);
        JSONObject errMsgJson = new JSONObject();
        errMsgJson.put("returnMsg",returnMsg);
        errMsgJson.put("errCode",errCode);
        errMsgJson.put("errCodeDes",errCodeDes);
        spWxOrderInEntity.setErrMsg(errMsgJson.toJSONString());
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
        wxPayUnifiedOrderRequest.setOutTradeNo(DateUtil.format(new Date(),"yyyyMMddHHmmss")+RandomUtil.randomNumbers(3));
        wxPayUnifiedOrderRequest.setTotalFee(suBillToPayBo.getMoney());
        wxPayUnifiedOrderRequest.setSpbillCreateIp(suBillToPayBo.getClientIp());
        wxPayUnifiedOrderRequest.setNotifyUrl(wxPayPropertiesEx.getNotifyUrl());
        wxPayUnifiedOrderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
        wxPayUnifiedOrderRequest.setOpenid(suBillToPayBo.getOpenId());
        return wxPayUnifiedOrderRequest;
    }

    @Override
    public void payment(List<SuBillEntity> suBillEntities) {

    }

    @Override
    public void orderNotify(WxPayOrderNotifyResult notifyResult) {
        Integer userId = JSON.parseObject(notifyResult.getAttach()).getInteger("userId");
        SpWxOrderNotifyEntity spWxOrderNotifyEntity = new SpWxOrderNotifyEntity();
        BeanUtil.copyProperties(notifyResult,spWxOrderNotifyEntity);
        spWxOrderNotifyMapper.insertSelective(spWxOrderNotifyEntity);

        PageHelper.startPage(1,1).count(false);
        SpWxOrderInEntity spWxOrderInEntity = new SpWxOrderInEntity();
        spWxOrderInEntity.setOutTradeNo(spWxOrderNotifyEntity.getOutTradeNo());
        spWxOrderInEntity = spWxOrderInMapper.selectOne(spWxOrderInEntity);
        SuBillToPayBo suBillToPayBo = JSON.parseObject(spWxOrderInEntity.getReqBo(),SuBillToPayBo.class);
        rabbitTemplate.convertAndSend(UserActionConfig.EXCHANGE,"",new PayOrderSucessMessage(userId,suBillToPayBo,spWxOrderNotifyEntity));
    }

    public static String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }
}
