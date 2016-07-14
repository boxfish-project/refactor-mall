package com.boxfishedu.protocal.model;

import com.boxfishedu.component.boxfish.util.bean.BeanToJson;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CommonResult implements Serializable, BeanToJson {

    private static final long serialVersionUID = 7091819311985647148L;

    private String returnMsg = "success";

    private Integer returnCode = HttpStatus.OK.value();

    private Object data;

    CommonResult() {
    }

    CommonResult(Object data) {
        this.data = data;
    }

    public static CommonResult createCommonResult(@NotNull Object data) {
        return new CommonResult(data);
    }
}
