package com.boxfishedu.online.mall.entity;

import com.boxfishedu.component.boxfish.util.bean.BeanToJson;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lauzhihao on 2016/04/18.
 * <p>
 * 用于描述单项Boxfish收费服务在不同次数时的优惠价格
 * <p>
 * 默认由每个{@link ProductSkuValue}记录的原始单价自动生成4/8/16/28次课程/1/3/6/12个月的总价格后再由人工调整,如果需要的话
 */
@Data(staticConstructor = "createInstance")
@Entity
public class ProductSkuCombo implements Serializable, BeanToJson {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Transient
    private ProductSkuValue serviceSKU;

    private Long skuId;

    private Integer skuAmount;              //服务次数

    private Integer skuCycle;               //总服务周期,单位:月

    private Long actualPrice;               //金额单位:分

    private Long originalPrice;

}
