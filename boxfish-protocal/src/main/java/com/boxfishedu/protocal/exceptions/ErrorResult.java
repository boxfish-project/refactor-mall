package com.boxfishedu.protocal.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class ErrorResult implements Serializable {

    private static final long serialVersionUID = 1465107315404051524L;

    private String returnMsg;

    private Integer returnCode = HttpStatus.BAD_REQUEST.value();

    public ErrorResult(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public static ErrorResult newErrorResult(String returnMsg) {
        return new ErrorResult(returnMsg);
    }
}
