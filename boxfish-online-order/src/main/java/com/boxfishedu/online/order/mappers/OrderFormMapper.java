package com.boxfishedu.online.order.mappers;

import com.boxfishedu.online.order.app.common.BaseMapper;
import com.boxfishedu.online.order.entity.OrderForm;
import com.boxfishedu.protocal.enums.OrderChannel;
import com.boxfishedu.protocal.enums.OrderStatus;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by malu on 16/7/11.
 */
public interface OrderFormMapper extends BaseMapper<OrderForm> {
    @Select("select count(0) from order_form t where t.user_id = #{userId} and t.order_channel = #{channel}")
    Integer countByUserIdAndOrderChannel(@Param("userId") Long userId, @Param("channel") OrderChannel channel);

    @Update("update order_form o set o.update_time = current_timestamp ,o.order_status = #{1} where o.order_code = #{2} and o.order_status = #{3}")
    Integer setFixedOrderStatusFor(OrderStatus targetStatus, String orderCode , OrderStatus sourceStatus);

    @Update("update order_form o set o.update_time = current_timestamp ,o.order_status = #{1},o.pay_channel = #{2} where o.order_code = #{3} and o.order_status = #{4}")
    Integer setOrderStatusAndPayChannelFor(OrderStatus newStatus, String payChannel, String code, OrderStatus oldStatus);
}
