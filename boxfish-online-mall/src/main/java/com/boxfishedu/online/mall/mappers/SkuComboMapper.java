package com.boxfishedu.online.mall.mappers;


import com.boxfishedu.online.mall.app.common.BaseMapper;
import com.boxfishedu.online.mall.entity.ProductSkuCombo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by malu on 16/6/15.
 */
public interface SkuComboMapper extends BaseMapper<ProductSkuCombo> {
//    @Select("select c.*, v.*, v.id as id2, v.create_time as createTime2, v.update_time as updateTime2, " +
//            "v.original_price as originalPrice2 from product_sku_combo c, product_sku_value v where c.sku_id = v.id")
    List<ProductSkuCombo> selectAllCom();
}
