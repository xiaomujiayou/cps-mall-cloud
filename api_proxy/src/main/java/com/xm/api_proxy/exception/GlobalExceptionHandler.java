package com.xm.api_proxy.exception;

import com.xm.comment.exception.DefaultExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 异常处理器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends DefaultExceptionHandler {

}