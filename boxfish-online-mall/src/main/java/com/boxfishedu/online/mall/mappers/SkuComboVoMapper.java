package com.boxfishedu.online.mall.mappers;

import com.boxfishedu.online.mall.app.common.BaseMapper;
import com.boxfishedu.online.mall.entity.SkuValueVo;
import com.boxfishedu.protocal.enums.Flag;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * Created by malu on 16/7/27.
 */
public interface SkuComboVoMapper extends BaseMapper<SkuValueVo> {
    List<SkuValueVo>querySkusWithPage(Map<String, Object> param);

    @Update("update product_sku_value v set v.sku_name = #{skuName}, v.flag_enable = #{flagEnable}, " +
            "v.flag_visible = #{flagVisible}, v.original_price = #{originalPrice}, v.description = #{description} where v.id = #{id}")
    Integer updateSkuCombo(@Param("id") Long id, @Param("skuName") String serviceName, @Param("flagEnable")Flag flagEnable,
                           @Param("flagVisible")Flag flagVisible, @Param("originalPrice") Long originalPrice, @Param("description") String description);

    @Select("select count(0) from product_sku_value where sku_name = #{skuName} and id != #{skuId}")
    Integer queryByNameAndName(@Param("skuName") String skuName, @Param("skuId") Long skuId);

    @Select("select count(0) from product_sku_value v where v.sku_name = #{skuName}")
    Integer querySkuComboByName(@Param("skuName") String skuName);
}
