package com.boxfishedu.online.order.mappers;

import com.boxfishedu.online.order.app.common.BaseMapper;
import com.boxfishedu.online.order.entity.OrderForm;
import com.boxfishedu.protocal.enums.OrderChannel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by malu on 16/7/11.
 */
public interface OrderFormMapper extends BaseMapper<OrderForm> {
    @Select("select count(0) from order_form t where t.user_id = #{userId} and t.order_channel = #{channel}")
    Integer countByUserIdAndOrderChannel(@Param("userId") Long userId, @Param("channel") OrderChannel channel);
}
