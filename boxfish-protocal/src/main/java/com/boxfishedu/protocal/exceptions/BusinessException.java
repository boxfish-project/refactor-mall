package com.boxfishedu.protocal.exceptions;

import com.boxfishedu.component.boxfish.util.bean.BeanToJson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException implements BeanToJson {

    private static final long serialVersionUID = -9148423083343320486L;

    public final Integer returnCode = HttpStatus.BAD_REQUEST.value();

    private String returnMsg;

    public BusinessException(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public BusinessException() {
        this.returnMsg = "错误的请求参数,请检查后重试!";
    }
}
