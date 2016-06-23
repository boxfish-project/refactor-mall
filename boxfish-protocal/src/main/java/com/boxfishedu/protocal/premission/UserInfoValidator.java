package com.boxfishedu.protocal.premission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Component
public class UserInfoValidator {

    private static final String TOKEN_VALIDATE_API = "http://114.55.58.184:8099/user/me?access_token=";

    public Long getUserId(String accessToken) {
        UserInfo userInfo = this.getUserInfo(accessToken);
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return userInfo.getId();
    }

    @Autowired
    private RestTemplate restTemplate;

    public UserInfo getUserInfo(String accessToken) {
        if (isEmpty(accessToken)) {
            return null;
        }
        //FIXME 调试期间留的后门
        if (accessToken.contentEquals("liuzhihao1")) {
            return UserInfo.createUserInfo();
        }
        try {
            String url = TOKEN_VALIDATE_API + accessToken;

            return this.restTemplate.getForObject(url, UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
