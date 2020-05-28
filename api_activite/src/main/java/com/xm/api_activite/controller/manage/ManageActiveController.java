package com.xm.api_activite.controller.manage;

import com.xm.api_activite.service.manage.CashoutService;
import com.xm.comment_serialize.form.BaseForm;
import com.xm.comment_serialize.module.active.form.CashoutApprovalForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/manage/active")
public class ManageActiveController {

    @Autowired
    private CashoutService cashoutService;

    /**
     * 提现审批
     */
    @PostMapping("/cashout/approval")
    public void approval(@Valid @RequestBody CashoutApprovalForm cashoutApprovalForm){
        cashoutService.approval(cashoutApprovalForm.getCashoutRecordIds());
    }
}
