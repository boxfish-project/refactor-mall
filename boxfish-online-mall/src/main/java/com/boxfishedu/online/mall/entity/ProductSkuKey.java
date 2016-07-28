package com.boxfishedu.online.mall.entity;

import com.boxfishedu.protocal.enums.Flag;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by lauzhihao on 2016/04/18.
 * <p>
 * 用于描述Boxfish收费服务基本信息的实体类
 */
@ToString(exclude = "serviceSKUSet")
@Data(staticConstructor = "createInstance")
@Entity
public class ProductSkuKey {

    @Transient
    private Integer page = 1;
    @Transient
    private Integer rows = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date stopTime;

    private String serviceCode;

    private String serviceName;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deadline;          //失效日期,通过计划任务每天固定时段自动修改服务的可用性

    @Enumerated(EnumType.STRING)
    private Flag flagEnable;     //是否可用

    @Transient
    private List<ProductSkuValue> serviceSKUSet = Lists.newArrayList();


}
