package com.xm.api_pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.xm.api_pay.config.WxPayPropertiesEx;
import com.xm.api_pay.mapper.SpWxOrderInMapper;
import com.xm.api_pay.service.WxPayService;
import com.xm.comment_serialize.module.pay.entity.SpWxOrderInEntity;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("wxPayService")
public class WxPayServiceImpl implements WxPayService {

    @Autowired
    private SpWxOrderInMapper spWxOrderInMapper;
    @Autowired
    private com.github.binarywang.wxpay.service.WxPayService wxService;
    @Autowired
    private WxPayPropertiesEx wxPayPropertiesEx;

    @Override
    public WxPayUnifiedOrderResult collection(SuBillToPayBo suBillToPayBo) {
        WxPayUnifiedOrderRequest request = createWxOrderRequest(suBillToPayBo);
        WxPayUnifiedOrderResult res = null;
        try {
            res = wxService.createOrder(request);
            if(res.getReturnCode().equals("SUCCESS") && res.getResultCode().equals("SUCCESS")){
                return res;
            }
        } catch (WxPayException e) {
            e.printStackTrace();
        }finally {
            saveOrder(request,res.getReturnMsg(),res.getErrCode(),res.getErrCodeDes());
        }
        throw new GlobleException(MsgEnum.WX_PAY_ORDER_CREATE_FAIL);
    }

    /**
     * 保存记录
     * @param request
     * @param returnMsg
     * @param errCode
     * @param errCodeDes
     */
    private void saveOrder(WxPayUnifiedOrderRequest request, String returnMsg, String errCode, String errCodeDes) {
        SpWxOrderInEntity spWxOrderInEntity = new SpWxOrderInEntity();
        BeanUtil.copyProperties(request,spWxOrderInEntity);
        spWxOrderInEntity.setState(0);
        JSONObject errMsgJson = new JSONObject();
        errMsgJson.put("returnMsg",returnMsg);
        errMsgJson.put("errCode",errCode);
        errMsgJson.put("errCodeDes",errCodeDes);
        spWxOrderInEntity.setErrMsg(errMsgJson.toJSONString());
        spWxOrderInMapper.insertSelective(spWxOrderInEntity);
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
//        wxPayUnifiedOrderRequest.setNonceStr(RandomUtil.randomString(32));
        wxPayUnifiedOrderRequest.setBody(suBillToPayBo.getDes());
        wxPayUnifiedOrderRequest.setAttach(suBillToPayBo.getId()+"");
        wxPayUnifiedOrderRequest.setOutTradeNo(DateUtil.format(new Date(),"yyyyMMddHHmmss")+RandomUtil.randomNumbers(3));
        wxPayUnifiedOrderRequest.setTotalFee(suBillToPayBo.getMoney());
        wxPayUnifiedOrderRequest.setSpbillCreateIp(suBillToPayBo.getClientIp());
        wxPayUnifiedOrderRequest.setNotifyUrl(wxPayPropertiesEx.getNotifyUrl());
        wxPayUnifiedOrderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
        wxPayUnifiedOrderRequest.setOpenid(suBillToPayBo.getOpenId());
//        wxPayUnifiedOrderRequest.setSign(SignUtils.createSign(wxPayUnifiedOrderRequest,"MD5",wxPayPropertiesEx.getSignKey(),null));
        return wxPayUnifiedOrderRequest;
    }

    @Override
    public void payment(List<SuBillEntity> suBillEntities) {

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
