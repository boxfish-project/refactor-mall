package com.boxfishedu.online.invitation.mappers;

import com.boxfishedu.online.invitation.app.common.BaseMapper;
import com.boxfishedu.online.invitation.entity.Invitation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by malu on 16/6/15.
 */
public interface InvitationMapper extends BaseMapper<Invitation> {
    @Select("select count(0) from invitation t where t.user_id = #{userId} and t.status_code = 'unused'")
    Integer findByUserId(@Param("userId") Long userId);
}
