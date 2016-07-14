package com.boxfishedu.protocal.exceptions;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

import static com.boxfishedu.component.boxfish.util.string.FormatUtil.toJson;

public class InvalidInputResponseBuilder {

    private InvalidInputResponseBuilder() {

    }

    public static HttpServletResponse build(HttpServletResponse response, String message) throws IOException {
        HttpServletResponse responseWrapper = new HttpServletResponseWrapper(response);

        responseWrapper.setHeader("content-type", "application/json");
        responseWrapper.setStatus(HttpStatus.BAD_REQUEST.value());
        ErrorResult errorResult = ErrorResult.newErrorResult(message);
        responseWrapper.getWriter().write(toJson(errorResult));

        return responseWrapper;
    }

    public static HttpServletResponse unauthorized(HttpServletResponse response, String message) throws IOException {
        HttpServletResponse responseWrapper = new HttpServletResponseWrapper(response);

        responseWrapper.setHeader("content-type", "application/json");
        responseWrapper.setStatus(HttpStatus.UNAUTHORIZED.value());
        ErrorResult errorResult = ErrorResult.newErrorResult(message);
        errorResult.setReturnCode(HttpStatus.UNAUTHORIZED.value());
        responseWrapper.getWriter().write(toJson(errorResult));

        return responseWrapper;
    }
}
