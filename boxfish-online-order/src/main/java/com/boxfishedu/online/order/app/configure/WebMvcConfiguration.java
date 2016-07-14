package com.boxfishedu.online.order.app.configure;

import com.boxfishedu.online.order.app.interceptors.UpdateOrderInterceptor;
import org.jdto.DTOBinder;
import org.jdto.spring.SpringDTOBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by lauzhihao on 2016/05/20.
 * 注入些杂七杂八的Bean
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public DTOBinder dtoBinder() {
        return new SpringDTOBinder();
    }

    @Bean
    public UpdateOrderInterceptor updateOrderInterceptor() {
        //把拦截类作为单例注入到容器
        return new UpdateOrderInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(this.updateOrderInterceptor()).addPathPatterns("/order/updateStatusByCode");
        super.addInterceptors(registry);
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(10);
        pool.setMaxPoolSize(100);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }


}