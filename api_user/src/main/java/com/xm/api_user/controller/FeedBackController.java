package com.xm.api_user.controller;

import com.xm.api_user.mapper.SuFeedbackMapper;
import com.xm.comment.annotation.LoginUser;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.R;
import com.xm.comment_serialize.module.user.entity.SuFeedbackEntity;
import com.xm.comment_serialize.module.user.form.AddFeedBackForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RequestMapping("/feedback")
@RestController
public class FeedBackController {

    @Autowired
    private SuFeedbackMapper suFeedbackMapper;

    @PostMapping
    public Msg add(@LoginUser Integer userId, @Valid @RequestBody AddFeedBackForm addFeedBackForm, BindingResult bindingResult){
        SuFeedbackEntity entity = new SuFeedbackEntity();
        entity.setUserId(userId);
        entity.setDes(addFeedBackForm.getDesc());
        entity.setImages(addFeedBackForm.getImgs() == null?null:String.join(",",addFeedBackForm.getImgs()));
        entity.setCreateTime(new Date());
        suFeedbackMapper.insertSelective(entity);
        return R.sucess();
    }
}
