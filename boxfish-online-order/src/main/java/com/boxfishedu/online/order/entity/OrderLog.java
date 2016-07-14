package com.boxfishedu.online.order.entity;


import com.boxfishedu.protocal.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liuzhihao on 16/3/1.
 * <p>
 * 订单流水VO
 */
@Data(staticConstructor = "createInstance")
@EqualsAndHashCode(callSuper = true)
@Entity
public final class OrderLog extends BaseEntity{

    private static final long serialVersionUID = 7026542938425851791L;

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

    private Long userId;

    private Long totalFee;

    private String orderCode;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public static OrderLog generateOrderLog(OrderForm orderForm) {
        OrderLog orderLog = new OrderLog();
        orderLog.setCreateTime(Calendar.getInstance().getTime());
        orderLog.setOrderStatus(orderForm.getOrderStatus());
        orderLog.setUserId(orderForm.getUserId());
        orderLog.setTotalFee(orderForm.getOrderFee());
        orderLog.setOrderCode(orderForm.getOrderCode());
        orderLog.setOrderId(orderForm.getId());

        return orderLog;
    }
}
