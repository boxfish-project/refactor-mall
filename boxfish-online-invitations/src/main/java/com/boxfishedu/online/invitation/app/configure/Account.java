package com.boxfishedu.online.invitation.app.configure;

import lombok.Data;

/**
 * Created by lauzhihao on 2016/06/02.
 */
@Data
public class Account {

    private Long userId;

    private String username;

    private String password;

    private String accessToken;

    private String role;
}
