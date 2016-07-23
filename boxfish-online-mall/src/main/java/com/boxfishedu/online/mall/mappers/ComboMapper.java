package com.boxfishedu.online.mall.mappers;
import com.boxfishedu.online.mall.app.common.BaseMapper;
import com.boxfishedu.online.mall.entity.ComboVo;
import com.boxfishedu.online.mall.entity.ProductSkuCombo;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by malu on 16/7/15.
 */
public interface ComboMapper extends BaseMapper<ComboVo>{
    //查找套餐是否已经存在(按课时数判断)
    @Select("select count(0) from product_sku_combo c where c.sku_id = #{skuId} and c.sku_amount = #{skuAmount}")
    Integer checkComboByAmount(@Param("skuId") Long skuId, @Param("skuAmount") Integer skuAmout);
    //按条件分页查询函数(ComboMapper.xml)
    List<ComboVo> queryComboWithPage(Map<String, Object> param);
    //修改套餐价格
    @Update("update product_sku_combo c set c.actual_price = #{actualPrice} where c.id = #{comboId}")
    Integer updateComboById(@Param("comboId") Long comboId, @Param("actualPrice") Long actualPrice);
    //按套餐id删除指定记录
    @Delete("delete from product_sku_combo where id = #{comboId}")
    Integer deleteComboById(@Param("comboId") Long comboId);
    //创建页面下拉框数据查询
    @Select("select id as id, service_name as serviceName from product_sku_key order by id")
    List<ComboVo> queryServiceData();

    @Select("select id as id, sku_name as skuName from product_sku_value where service_id = #{serviceId} order by id")
    List<ComboVo> querySkuData(@Param("serviceId") Long serviceId);

    @Select("select original_price as originalPrice from product_sku_value where id = #{skuId}")
    Long queryUnitPrice(@Param("skuId") Long skuId);
    //查询树的父节点(key表/服务类型)
    @Select("select id as id, service_name as serviceName from product_sku_key")
    List<ComboVo> queryParNode();
    //查询指定节点的子节点(value表/服务名)
    @Select("select id as id, sku_name as skuName from product_sku_value where service_id = #{serviceId}")
    List<ComboVo> queryChildNode(@Param("serviceId") Long serviceId);

}
