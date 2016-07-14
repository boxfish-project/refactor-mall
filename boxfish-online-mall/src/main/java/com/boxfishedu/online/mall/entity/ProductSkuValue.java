package com.boxfishedu.online.mall.entity;

import com.boxfishedu.component.boxfish.util.bean.BeanToJson;
import com.boxfishedu.protocal.enums.Flag;
import com.boxfishedu.protocal.enums.ServiceType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by lauzhihao on 2016/04/18.
 * <p>
 * 用于描述Boxfish收费服务的最小售卖单元的JPA实体类.
 * <p>
 * 用于对 {@link ProductSkuKey}类的扩展和描述.
 */
@Data(staticConstructor = "createInstance")
@ToString(exclude = "serviceBasic")
@Entity
public class ProductSkuValue extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Transient
    private ProductSkuKey serviceBasic;

    private Long serviceId;

    private String skuCode;

    private String skuName;

    private Integer countUnit;

    private Long originalPrice;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;          //失效时间,通过计划任务每天固定时段自动修改sku的可用性

    private String serviceType;    //标识中教/外教

    @Enumerated(EnumType.STRING)
    private Flag flagEnable;     //是否可用

    @Enumerated(EnumType.STRING)
    private Flag flagVisible;    //对APP端是否可见

    private Integer validDay;      //有效天数

}
