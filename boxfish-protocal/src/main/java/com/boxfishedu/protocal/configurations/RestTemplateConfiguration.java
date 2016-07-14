package com.boxfishedu.protocal.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by lauzhihao on 2016/05/20.
 * 注入些杂七杂八的Bean
 */
@Configuration
public class RestTemplateConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);       //10秒连接超时
        factory.setReadTimeout(5000);          //5秒读数据超时

        return factory;
    }

    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate(this.simpleClientHttpRequestFactory());
    }
}