package com.xm.api_user.controller;

import com.xm.api_user.mapper.custom.SuBillMapperEx;
import com.xm.api_user.service.BillService;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_serialize.module.lottery.ex.SlPropSpecEx;
import com.xm.comment_serialize.module.user.bo.SuBillToPayBo;
import com.xm.comment_serialize.module.user.dto.BillOrderDto;
import com.xm.comment_serialize.module.user.dto.OrderBillDto;
import com.xm.comment_serialize.module.user.entity.SuBillEntity;
import com.xm.comment_serialize.module.user.vo.BillVo;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_utils.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping
    public PageBean<BillVo> get(@LoginUser Integer userId, Integer state, Integer type, Integer pageNum, Integer pageSize){
        return billService.getList(userId,state,type,pageNum,pageSize);
    }

    /**
     * 生成道具购买账单，供道具服务调用
     * @param slPropSpecEx
     * @return
     */
    @PostMapping("/create/prop")
    public SuBillToPayBo createByProp(@RequestBody SlPropSpecEx slPropSpecEx){
        if(slPropSpecEx == null)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        return billService.createByProp(slPropSpecEx);
    }

    @GetMapping("/info")
    public List<BillOrderDto> getBillInfo(Integer userId, @RequestParam("billIds") List<String> billIds){
        return billService.getBillInfo(userId,billIds);
    }
}
