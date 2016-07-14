package com.boxfishedu.online.order.app.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by lauzhihao on 2016/04/29.
 * 处理一对多懒加载的配置,由于Controller返回的都是JsonResultModel,使用的序列化配置不是下面这个配置.
 * 只有直接返回对象类型时下列配置才生效
 */
@Configuration
public class JsonConfiguration extends WebMvcConfigurerAdapter {
//    private MappingJackson2HttpMessageConverter jacksonMessageConverter() {
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setObjectMapper(this.objectMapper());
//        return converter;
//    }
//
//    private ObjectMapper objectMapper() {
//        return new ObjectMapper()
//                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//                //.setSerializationInclusion(JsonInclude.Include.NON_NULL)
//                .setTimeZone(TimeZone.getTimeZone("GMT+8"))
//                .registerModule(new JodaModule())     //也可以通过下面的方式进行模块查找和注册
//                .registerModule(this.jodaDateTimeModule());
//        //.findAndRegisterModules();
//    }
//
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(jacksonMessageConverter());
//        super.configureMessageConverters(converters);
//    }
//
//    @Bean
//    public Module jodaDateTimeModule() {
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(DateTime.class, new DateTimeSerializer
//                (new JacksonJodaDateFormat(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))));
//        return module;
//    }
}
