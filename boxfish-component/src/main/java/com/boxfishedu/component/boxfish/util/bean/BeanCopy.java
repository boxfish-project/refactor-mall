package com.boxfishedu.component.boxfish.util.bean;


import org.apache.commons.beanutils.BeanUtils;


public interface BeanCopy {

    default Object copy() throws Exception {
        return BeanUtils.cloneBean(this);
    }

}
