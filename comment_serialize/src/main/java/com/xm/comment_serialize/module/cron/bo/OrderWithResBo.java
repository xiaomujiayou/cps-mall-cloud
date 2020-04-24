package com.xm.comment_serialize.module.cron.bo;

import com.xm.comment_serialize.module.cron.entity.ScOrderStateRecordEntity;
import com.xm.comment_serialize.module.user.entity.SuOrderEntity;
import lombok.Data;

@Data
public class OrderWithResBo {
    private SuOrderEntity suOrderEntity;
    private ScOrderStateRecordEntity scOrderStateRecordEntity;
}
