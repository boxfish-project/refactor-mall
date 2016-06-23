package com.boxfishedu.online.invitation.app.configure;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "conf")
public class AccountProperties {

    private List<Account> accounts = Lists.newArrayList();

}
