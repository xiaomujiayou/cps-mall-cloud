package com.xm.cron_service.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_feign.module.user.feign.UserFeignClient;
import com.xm.comment_serialize.module.cron.entity.ScBillPayEntity;
import com.xm.comment_serialize.module.cron.vo.ScBillPayVo;
import com.xm.comment_serialize.module.user.constant.BillTypeConstant;
import com.xm.comment_serialize.module.user.dto.OrderBillDto;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.mybatis.PageBean;
import com.xm.comment_utils.number.NumberUtils;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.cron_service.mapper.ScBillPayMapper;
import com.xm.cron_service.service.BillPayService;
import com.xm.cron_service.utils.BillInfoExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bill")
public class BillPayController {

    @Autowired
    private BillPayService billPayService;
    @Autowired
    private ScBillPayMapper scBillPayMapper;
    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping("/commission")
    public void commission(){
        billPayService.commission();
    }


    @GetMapping("/list")
    public PageBean<ScBillPayVo> list(@LoginUser Integer userId, Integer pageNum, Integer pageSize){
        return billPayService.list(userId,pageNum,pageSize);
    }

    /**
     * 打款记录excel
     * @param userId
     * @param id
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping("/download/excel")
    public List<OrderBillDto> excel(@LoginUser Integer userId, Integer id, HttpServletResponse response) throws IOException {
        if(id == null)
            throw new GlobleException(MsgEnum.PARAM_VALID_ERROR);
        ScBillPayEntity scBillPayEntity = scBillPayMapper.selectByPrimaryKey(id);
        if(scBillPayEntity == null)
            throw new GlobleException(MsgEnum.DATA_INVALID_ERROR);
        List<OrderBillDto> orderBillDtos = userFeignClient.getBillInfo(userId, CollUtil.newArrayList(scBillPayEntity.getBillIds().split(",")));
        List<BillInfoExcel> billInfoExcels = orderBillDtos.stream().map(this::convertExcel).collect(Collectors.toList());
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(scBillPayEntity.getName(), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), BillInfoExcel.class).sheet(scBillPayEntity.getName().replace(":","-")).doWrite(billInfoExcels);
        return orderBillDtos;
    }

    private BillInfoExcel convertExcel(OrderBillDto orderBillDto){
        BillInfoExcel excel = new BillInfoExcel();
        excel.setBillSn(orderBillDto.getBillSn());
        excel.setBillType(orderBillDto.getType() == BillTypeConstant.BUY_NORMAL ? "自购" : orderBillDto.getType() == BillTypeConstant.PROXY_PROFIT ? "代理收益" : orderBillDto.getType() == BillTypeConstant.BUY_SHARE ? "自购" : orderBillDto.getType() == BillTypeConstant.SHARE_PROFIT ? "分享收益" : "其他");
        excel.setMoney(NumberUtils.fen2yuan(orderBillDto.getMoney()));
        if(orderBillDto.getSuOrderEntity() != null && orderBillDto.getType() != BillTypeConstant.PROXY_PROFIT){
            excel.setOrderSn(orderBillDto.getSuOrderEntity().getOrderSn());
            excel.setProductName(orderBillDto.getSuOrderEntity().getProductName());
        }else {
            excel.setOrderSn("******");
            excel.setProductName("******");
        }
        if(orderBillDto.getSuUserEntity() != null)
            excel.setUserName(orderBillDto.getSuUserEntity().getNickname());
        excel.setCreateTime(orderBillDto.getCreateTime());
        return excel;
    }
}

