package com.boxfishedu.online.mall.mappers;

import com.boxfishedu.online.mall.app.common.BaseMapper;
import com.boxfishedu.online.mall.entity.ProductSkuKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by malu on 16/6/22.
 */
public interface SkuKeyMapper extends BaseMapper<ProductSkuKey> {
    @Select("SELECT" +
            " t.id as id," +
            " t.create_time as createTime," +
            " t.update_time as updateTime," +
            " t.service_code as serviceCode," +
            " t.service_name as serviceName," +
            " t.description as description," +
            " t.deadline as deadline," +
            " t.flag_enable as flagEnable" +
            " FROM" +
            " product_sku_key t" +
            " WHERE" +
            " t.id = #{id}")
    ProductSkuKey selectById(@Param("id") Long id);
}
