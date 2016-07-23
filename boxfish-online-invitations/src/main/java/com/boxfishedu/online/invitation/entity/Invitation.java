package com.boxfishedu.online.invitation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by lauzhihao on 2016/05/16.
 * <p>
 * 邀请码实体类
 */
@Data(staticConstructor = "createInstance")
@EqualsAndHashCode(callSuper = true)
public class Invitation extends BaseEntity {

    private String content;

    private String userId;

    private String statusCode;

    private Date createTime;

    private Date updateTime;

    private Long operator;

    private String orderCode;
}
