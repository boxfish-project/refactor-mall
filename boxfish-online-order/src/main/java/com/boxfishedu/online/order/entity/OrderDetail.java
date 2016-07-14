package com.boxfishedu.online.order.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by liuzhihao on 16/3/1.
 * <p>
 * 订单子项VO
 */
@Data(staticConstructor = "createInstance")
@EqualsAndHashCode(callSuper = true)
@Entity
public final class OrderDetail extends BaseEntity{

    private static final long serialVersionUID = -5630277033222434844L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

//    @ManyToOne(targetEntity = OrderForm.class, cascade = {CascadeType.REFRESH})
//    @JoinColumn(name = "order_id")
//    @JsonBackReference
    @Transient
    private OrderForm orderForm;

    private Long orderId;

    private Long productId;

    private String productName;

    private Long price;

    private Integer amount;

    private String description;

    private String productInfo;         //json格式的商品信息

    //下面两个属性仅传递给教学中心使用,不进行持久化
    @Transient
    private String comboCycle;          //课程总周期(月)

    @Transient
    private String countInMonth;        //每月总次数(次)

}
