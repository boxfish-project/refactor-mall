package com.boxfishedu.online.mall.mappers;

import com.boxfishedu.online.mall.app.common.BaseMapper;
import com.boxfishedu.online.mall.entity.ProductSkuValue;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by malu on 16/6/21.
 */

public interface SkuValueMapper extends BaseMapper<ProductSkuValue> {

    @Select("select t.id as id," +
            "t.create_time as createTime," +
            "t.update_time as updateTime," +
            "t.sku_code as skuCode," +
            "t.sku_name as skuName," +
            "t.count_unit as countUnit," +
            "t.original_price as originalPrice," +
            "t.description as description," +
            "t.deadline as deadline," +
            "t.service_type as serviceType," +
            "t.flag_enable as flagEnable," +
            "t.flag_visible as flagVisible," +
            "t.valid_day as validDay" +
            " from" +
            " product_sku_value t where t.id = #{id}")
    ProductSkuValue selectById(@Param("id") Long id);

    @Select("select t.id as id," +
            "t.create_time as createTime," +
            "t.update_time as updateTime," +
            "t.sku_code as skuCode," +
            "t.sku_name as skuName," +
            "t.count_unit as countUnit," +
            "t.original_price as originalPrice," +
            "t.description as description," +
            "t.deadline as deadline," +
            "t.service_type as serviceType," +
            "t.flag_enable as flagEnable," +
            "t.flag_visible as flagVisible," +
            "t.valid_day as validDay" +
            " from" +
            " product_sku_value t where t.service_id = #{serviceId}")
    List<ProductSkuValue> selectByServiceId(@Param("serviceId") Long serviceId);
}
