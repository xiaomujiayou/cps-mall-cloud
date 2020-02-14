package com.xm.comment.exception;

import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_utils.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 默认异常处理器
 */
@Slf4j
//@ControllerAdvice
public class DefaultExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Msg handle(Exception e){
        if (e instanceof GlobleException){
            GlobleException globleException = (GlobleException)e;
//            log.error("【自定义异常】{}",((GlobleException) e).getMsgEnum().getMsg());
            log.error("【自定义异常】{}",e);
            return R.error(globleException.getMsgEnum(),globleException.getMessage());
        }else {
            log.error("【系统异常】{}",e);
            return R.error(MsgEnum.UNKNOWN_ERROR);
        }
    }
}
