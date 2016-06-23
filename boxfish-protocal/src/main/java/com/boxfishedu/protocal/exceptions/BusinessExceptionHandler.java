package com.boxfishedu.protocal.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.boxfishedu.protocal.exceptions.ErrorResult.newErrorResult;

@ControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public final Object processAllException(Exception e) {
        final ErrorResult errorResult = newErrorResult("ERROR");
        errorResult.setReturnCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResult.setReturnMsg("服务器内部错误,请稍后再试!");
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResult);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    public Object businessException(BusinessException e) throws Exception {
        final ErrorResult errorResult = newErrorResult(e.getReturnMsg());
        e.printStackTrace();
        return ResponseEntity.badRequest().body(errorResult);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Object httpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResult resultModel = newErrorResult(e.getMessage());
        resultModel.setReturnMsg("提交的数据内容包含错误的类型或格式,请检查并重试!");

        return ResponseEntity.badRequest().body(resultModel);
    }
}
