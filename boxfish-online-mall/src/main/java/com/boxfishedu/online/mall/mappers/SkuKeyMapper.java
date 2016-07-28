package com.boxfishedu.online.mall.mappers;

import com.boxfishedu.online.mall.app.common.BaseMapper;
import com.boxfishedu.online.mall.entity.ProductSkuKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update("update product_sku_key k set k.service_name = #{serviceName}, k.flag_enable = #{flagEnable}, k.description = #{description} where k.id = #{serviceId}")
    Integer updateById(@Param("serviceId") Long serviceId, @Param("serviceName") String serviceName, @Param("flagEnable") String flagEnable, @Param("description") String description);

    @Select("select count(0) from product_sku_key where service_name = #{serviceName} and id != #{serviceId}")
    Integer queryCountByNameAndId(@Param("serviceName") String serviceName, @Param("serviceId") Long serviceId);

    @Select("select count(0) from product_sku_key where service_name = #{serviceName}")
    Integer queryCountByName(@Param("serviceName") String serviceName);
}
