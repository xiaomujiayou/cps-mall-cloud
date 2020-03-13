package com.xm.comment.exception;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xm.comment_utils.exception.GlobleException;
import com.xm.comment_utils.response.Msg;
import com.xm.comment_utils.response.MsgEnum;
import com.xm.comment_utils.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

/**
 * 异常处理
 * 只处理zuul -> service 之间的异常
 * service -> service 之间的异常使用 FeignClientErrorDecoder包装后层层传递
 * 需要配合Zuul ResponseFilter对异常开放绿色通道（防止多次包装请求结果）
 */
@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Msg handle(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        e.printStackTrace();
        String from = request.getHeader("from");
        if(StrUtil.isBlank(from) || !from.equals("zuul"))
            //服务间传递的异常，继续传递
            throw e;
        //处理zuul和最外层服务间的异常
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        if(e instanceof GlobleException) {
            //自定义异常，或内层服务的包装异常
            GlobleException ge = (GlobleException)e;
            //处理zuul -> 服务之间的异常
            MsgEnum msgEnum = ge.getMsgEnum();
            if (msgEnum == null)
                msgEnum = MsgEnum.UNKNOWN_ERROR;
            Msg msg = R.error(msgEnum);
            if (ge.getMsgEnum() != null && StrUtil.isNotBlank(ge.getMsg()))
                msg.setMsg(ge.getMsg());
            return msg;
        }else {
            //最外层服务发生系统异常，避免代码级异常泄露。
            return R.error(MsgEnum.UNKNOWN_ERROR);
        }

    }
}
