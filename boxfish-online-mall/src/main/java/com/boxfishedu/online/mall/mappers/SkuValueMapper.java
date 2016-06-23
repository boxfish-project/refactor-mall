package com.boxfishedu.online.mall.mappers;

import com.boxfishedu.online.mall.app.common.BaseMapper;
import com.boxfishedu.online.mall.entity.ProductSkuValue;

import java.util.List;

/**
 * Created by malu on 16/6/21.
 */
public interface SkuValueMapper extends BaseMapper<ProductSkuValue> {
    List<ProductSkuValue> selectAllSKU();
}
