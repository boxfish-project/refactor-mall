package com.boxfishedu.online.invitation.mappers;

import com.boxfishedu.online.invitation.app.common.BaseMapper;
import com.boxfishedu.online.invitation.entity.Invitation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by malu on 16/6/15.
 */
public interface InvitationMapper extends BaseMapper<Invitation> {
    @Select("select count(0) from invitation t where t.user_id = #{userId} and t.status_code = 'unused'")
    Integer findByUserId(@Param("userId") Long userId);

    @Update("update invitation t set t.status_code = 'used', t.order_code = #{orderCode} where t.status_code = 'unused' and t.user_id = #{userId}")
    Integer updateStatus(@Param("userId") String userId, @Param("orderCode") String orderCode);
}
