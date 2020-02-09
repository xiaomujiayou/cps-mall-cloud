package com.xm.api_lottery.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.xm.api_lottery.mapper.SlPropMapper;
import com.xm.api_lottery.mapper.SlPropSpecMapper;
import com.xm.api_lottery.service.PayService;
import com.xm.comment.module.pay.feign.PayFeignClient;
import com.xm.comment.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.lottery.entity.SlPropEntity;
import com.xm.comment_serialize.module.lottery.entity.SlPropSpecEntity;
import com.xm.comment_serialize.module.lottery.ex.SlPropSpecEx;
import com.xm.comment_serialize.module.pay.vo.WxPayOrderResultVo;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.entity.SuUserEntity;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.MsgEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("/payService")
public class PayServiceImpl implements PayService {

    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private PayFeignClient payFeignClient;
    @Autowired
    private SlPropMapper slPropMapper;
    @Autowired
    private SlPropSpecMapper slPropSpecMapper;

    @Override
    public WxPayOrderResultVo wxPay(SuUserEntity suUserEntity, Integer propSpecId, String clientIp) {
        SlPropSpecEntity slPropSpecEntity = slPropSpecMapper.selectByPrimaryKey(propSpecId);
        if(slPropSpecEntity == null )
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR, "propSpecId无效");
        SlPropEntity slPropEntity = slPropMapper.selectByPrimaryKey(slPropSpecEntity.getPropId());
        SlPropSpecEx slPropSpecEx = new SlPropSpecEx();
        BeanUtil.copyProperties(slPropSpecEntity,slPropSpecEx);
        slPropSpecEx.setSuUserEntity(suUserEntity);
        slPropSpecEx.setClientIp(clientIp);
        slPropSpecEx.setSlPropEntity(slPropEntity);
        //保存账单
        SuBillToPayBo suBillToPayBo = userFeignClient.createByProp(slPropSpecEx).getData();
        //统一下单
        return payFeignClient.wxPay(suBillToPayBo).getData();
    }
}
