package com.boxfishedu.online.order.app.common;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by malu on 16/6/15.
 */
public interface BaseMapper<T> extends MySqlMapper<T>, Mapper<T> {
}
