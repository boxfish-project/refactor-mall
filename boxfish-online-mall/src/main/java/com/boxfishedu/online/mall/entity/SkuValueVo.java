package com.boxfishedu.online.mall.entity;

import com.boxfishedu.protocal.enums.Flag;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

/**
 * Created by malu on 16/7/27.
 */
@Data(staticConstructor = "createInstance")
public class SkuValueVo {
    private Integer page = 1;
    private Integer rows = 20;

    private Long id;

    private Date createTime;

    private Date updateTime;

    private Long serviceId;

    private String serviceName;

    private String skuCode;

    private String skuName;

    private Integer countUnit;

    private Long originalPrice;

    private String description;

    private Date deadline;          //失效时间,通过计划任务每天固定时段自动修改sku的可用性

    private String serviceType;    //标识中教/外教

    @Enumerated(EnumType.STRING)
    private Flag flagEnable;     //是否可用

    @Enumerated(EnumType.STRING)
    private Flag flagVisible;    //对APP端是否可见

    private Integer validDay;      //有效天数
}
