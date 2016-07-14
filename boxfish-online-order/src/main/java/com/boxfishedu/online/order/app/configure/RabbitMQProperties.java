package com.boxfishedu.online.order.app.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMQProperties {

    private String host;

    private Integer port;

    private String username;

    private String password;

    private String virtualHost;
}
