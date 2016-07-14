package com.boxfishedu.online.order.mappers;

import com.boxfishedu.online.order.app.common.BaseMapper;
import com.boxfishedu.online.order.entity.OrderDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * Created by malu on 16/7/11.
 */
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    @Delete("delete from order_detail t where t.order_id = #{orderId}")
    Integer deleteOrderDatails(@Param("orderId") Long orderId);
}
