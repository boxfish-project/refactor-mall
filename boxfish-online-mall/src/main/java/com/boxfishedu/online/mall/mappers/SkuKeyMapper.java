package com.boxfishedu.online.mall.mappers;

import com.boxfishedu.online.mall.app.common.BaseMapper;
import com.boxfishedu.online.mall.entity.ProductSkuKey;

import java.util.List;

/**
 * Created by malu on 16/6/22.
 */
public interface SkuKeyMapper extends BaseMapper<ProductSkuKey> {
    List<ProductSkuKey> selectAllKey();
}
