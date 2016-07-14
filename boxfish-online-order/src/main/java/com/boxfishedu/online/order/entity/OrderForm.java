package com.boxfishedu.online.order.entity;


import com.boxfishedu.protocal.enums.Flag;
import com.boxfishedu.protocal.enums.OrderChannel;
import com.boxfishedu.protocal.enums.OrderSource;
import com.boxfishedu.protocal.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Objects.isNull;

/**
 * Created by liuzhihao on 16/3/1.
 * <p>
 * 订单VO
 */
@Data(staticConstructor = "createInstance")
@ToString(exclude = {"orderDetails", "orderLogs"})
@EqualsAndHashCode(callSuper = true)
@Entity
public class OrderForm extends BaseEntity{

    private static final long serialVersionUID = 4577610801121966443L;

//    public enum OrderSource {
//        ANDROID,        //安卓
//        IOS,            //IOS
//        ADMIN;          //管理端
//
//        public static boolean contains(OrderSource value) {
//            switch (value) {
//                case ADMIN:
//                    return true;
//                case ANDROID:
//                    return true;
//                case IOS:
//                    return true;
//                default:
//                    return false;
//            }
//        }
//    }

//    public enum OrderChannel {
//        STANDARD,       //标准付费订单
//        EXPERIENCE,     //免费体验订单
//        ADJUST;         //管理端补录订单
//
//        public static boolean contains(OrderChannel value) {
//            switch (value) {
//                case EXPERIENCE:
//                    return true;
//                case STANDARD:
//                    return true;
//                case ADJUST:
//                    return true;
//                default:
//                    return false;
//            }
//        }
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    private Long userId;

    private String orderCode;

    private Long payFee;                    //订单实付金额

    private Long orderFee;                  //订单应付金额(优惠后:将订单子项的实际价格相加)

    private Long couponFee;                 //优惠金额

    private String inviteCode;

    private String remark;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private OrderSource orderSource;        //订单来源:android/ios/admin

    private String payChannel;          //支付方式:alipay/wechat/parent

    @Enumerated(EnumType.STRING)
    private OrderChannel orderChannel;      //订单类型:standard/experience/adjust

    @Enumerated(EnumType.STRING)
    private Flag flagDropped = Flag.NO;    //删除标识:YES/NO

    @Transient
//    @OneToMany(targetEntity = OrderLog.class, mappedBy = "order", fetch = FetchType.EAGER,
//            cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    private List<OrderLog> orderLogs;

    @Transient
//    @OneToMany(targetEntity = OrderDetail.class, mappedBy = "order", fetch = FetchType.EAGER,
//            cascade = CascadeType.ALL, orphanRemoval = true)    //使用孤值删除时注解的属性必须是private setter
//    @JsonManagedReference
    private List<OrderDetail> orderDetails;

    public void addOrderDetail(OrderDetail orderDetail) {
        if (isNull(this.orderDetails)) {
            this.orderDetails = newArrayList();
        }
        orderDetail.setOrderId(this.getId());
        this.orderDetails.add(orderDetail);
    }

    public void addOrderLog(OrderLog orderLog) {
        if (isNull(this.orderLogs)) {
            this.orderLogs = newArrayList();
        }
        orderLog.setOrderId(this.getId());
        this.orderLogs.add(orderLog);
    }
}
