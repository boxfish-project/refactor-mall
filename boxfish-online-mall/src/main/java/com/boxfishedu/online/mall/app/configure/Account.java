package com.boxfishedu.online.mall.app.configure;

import lombok.Data;

@Data
public class Account {

    private Long userId;

    private String username;

    private String password;

    private String accessToken;

    private String role;
}
