package com.boxfishedu.component.boxfish.util.bean;


import com.boxfishedu.component.boxfish.util.string.FormatUtil;

public interface BeanToJson {

    default String toJson() throws Exception {
        return FormatUtil.toJson(this);
    }

    default String toJsonNoException() {
        return FormatUtil.toJsonNoException(this);
    }
}
