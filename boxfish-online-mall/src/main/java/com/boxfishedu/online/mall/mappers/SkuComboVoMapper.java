package com.boxfishedu.online.mall.mappers;

import com.boxfishedu.online.mall.app.common.BaseMapper;
import com.boxfishedu.online.mall.entity.SkuValueVo;
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

    @Update("update product_sku_value v set v.sku_name = #{skuName} where v.id = #{id}")
    Integer updateSkuCombo(@Param("id") Long id, @Param("skuName") String serviceName);

    @Select("select count(0) from product_sku_value v where v.sku_name = #{skuName}")
    Integer checkSkuCombo(@Param("skuName") String skuName);
}
