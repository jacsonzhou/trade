package com.ybb.trade.system;

import com.ybb.trade.common.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public MessageResult errorHandler(Exception ex) {
        ex.printStackTrace();
        log.info(">>>拦截异常>>",ex);
        MessageResult result = MessageResult.error(ex.getMessage());
        return result;
    }
}
