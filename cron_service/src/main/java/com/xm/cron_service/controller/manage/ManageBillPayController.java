package com.xm.cron_service.controller.manage;

import cn.hutool.core.util.ObjectUtil;
import com.xm.comment_serialize.module.cron.entity.ScWaitPayBillEntity;
import com.xm.comment_serialize.module.cron.form.BillPayForm;
import com.xm.cron_service.mapper.ScWaitPayBillMapper;
import com.xm.cron_service.mapper.custom.ScBillPayMapperEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

@RestController
@RequestMapping("/manage/bill/pay")
public class ManageBillPayController {

    @Autowired
    private ScBillPayMapperEx scBillPayMapperEx;

    /**
     * 账单金额统计
     */
    @GetMapping("/profit")
    public Integer getBillProfit(BillPayForm form) {
        Integer result = scBillPayMapperEx.getBillProfit(form);
        return result == null ? 0 : result;
    }

    private Example formToExample(BillPayForm form) {
        Example example = new Example(ScWaitPayBillEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if (form.getUserId() != null)
            criteria.andEqualTo("userId", form.getUserId());
        if (form.getBillId() != null)
            criteria.andEqualTo("billId", form.getBillId());
        if (form.getState() != null)
            criteria.andEqualTo("state", form.getState());
        if (ObjectUtil.isAllNotEmpty(form.getCreateStart(), form.getCreateEnd()))
            criteria.andBetween("createTime", form.getCreateStart(), form.getCreateEnd());
        return example;
    }
}
