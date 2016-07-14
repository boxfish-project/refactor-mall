package com.boxfishedu.online.order.mappers;

import com.boxfishedu.online.order.app.common.BaseMapper;
import com.boxfishedu.online.order.entity.OrderLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * Created by malu on 16/7/11.
 */
public interface OrderLogMapper extends BaseMapper<OrderLog> {
    @Delete("delete from order_log t where t.order_id = #{orderId}")
    Integer deleteOrderLogs(@Param("orderId") Long orderId);
}
