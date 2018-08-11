package com.newland.jxh.store.common.exception;

import com.newland.jxh.store.common.jsonResult.ServerCode;
import com.newland.jxh.store.common.jsonResult.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author qyw
 * @Description 全局异常处理
 * @Date Created in 20:26 2018/8/11
 */
@RestControllerAdvice(basePackages = "com.newland.jxh.store.demo.web.controller")
@Slf4j
public class GlobalExceptionHandlerAdvice {
    /**
     * Handle exceptions thrown by handlers.
     */
    @ExceptionHandler(value = Exception.class)
    public ServerResponse handleBadRequest(Exception exception, HttpServletResponse response) {
        ServerResponse errorDTO = null;

        if (exception instanceof ApiException) {//api异常
            ApiException apiException = (ApiException) exception;
            errorDTO = ServerResponse.createByError(apiException.getErrorCode(), apiException.getMessage());
            return errorDTO;
        }

        if (exception instanceof BindException) {
            /*注意：此处的BindException 是 Spring 框架抛出的Validation异常*/
            BindingResult bindingResult = ((BindException) exception).getBindingResult();
            if (null != bindingResult && bindingResult.hasErrors()) {
                List<Object> jsonList = new ArrayList<>();
                bindingResult.getFieldErrors().stream().forEach(fieldError -> {
                    Map<String, Object> jsonObject = new HashMap<>(2);
                    jsonObject.put("field", fieldError.getField());
                    jsonObject.put("msg", fieldError.getDefaultMessage());
                    jsonList.add(jsonObject);
                });
                errorDTO = ServerResponse.createByError(ServerCode.ILLEGAL_ARGUMENTS.getCode(), ServerCode.ILLEGAL_ARGUMENTS.getDesc(), jsonList);
                return errorDTO;
            }
        }
        /**
         * 系统内部未知异常，打印异常消息
         */
        log.error("Error: handleUnKnowBadRequest message : {}", exception);
        errorDTO = ServerResponse.createByError(ServerCode.UNKNOW_EXCEPTION.getCode(), exception.getMessage());
        return errorDTO;
    }
}
