package com.boxfishedu.online.mall.app.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "conf")
@Data
public class ConfigProperties {

    private List<String> countNeedCreate;

    private List<String> cycleNeedCreate;

}
