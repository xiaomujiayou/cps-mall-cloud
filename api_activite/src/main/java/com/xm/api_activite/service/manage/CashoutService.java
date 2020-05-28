package com.xm.api_activite.service.manage;


import java.util.List;

public interface CashoutService {
    //提现审批
    void approval(List<Integer> cashoutRecordIds);
}
