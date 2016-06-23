package com.boxfishedu.online.mall.service;

import com.boxfishedu.online.mall.entity.ProductSkuKey;
import com.boxfishedu.online.mall.entity.ProductSkuValue;
import com.boxfishedu.online.mall.mappers.SkuKeyMapper;
import com.boxfishedu.online.mall.mappers.SkuValueMapper;
import com.boxfishedu.protocal.enums.Flag;
import com.boxfishedu.protocal.enums.ServiceType;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * Created by malu on 16/6/18.
 */
@Service
public class ProductSkuService {
    @Autowired
    private SkuValueMapper skuValueMapper;
    @Autowired
    private SkuKeyMapper skuKeyMapper;

    public List<ProductSkuKey> getAllServices(){ return this.skuKeyMapper.selectAllKey();}

    public List<ProductSkuValue> getAllSKU(){
        List<ProductSkuValue> all = this.skuValueMapper.selectAllSKU();
        if (all == null || all.isEmpty()) {
            ProductSkuValue sku = ProductSkuValue.createInstance();
            sku.setServiceType(ServiceType.CHINESE_TEACHER);
            sku.setSkuName("中教在线授课");
            sku.setSkuCode("100101");
            sku.setCreateTime(Calendar.getInstance().getTime());
            sku.setDescription("明星计划7+1");
            sku.setOriginalPrice(10000L);
            sku.setServiceBasic(this.getAllServices().get(0));
            sku.setValidDay(365);

            sku.setFlagEnable(Flag.ENABLE);
            sku.setFlagVisible(Flag.ENABLE);

            int row = this.skuValueMapper.insert(sku);
            if(row > 0){
                all = Lists.newArrayList(sku);
            }
        }
        return this.skuValueMapper.selectAllSKU();
    }
}
