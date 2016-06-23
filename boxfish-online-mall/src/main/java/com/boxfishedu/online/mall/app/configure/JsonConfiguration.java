package com.boxfishedu.online.mall.app.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class JsonConfiguration extends WebMvcConfigurerAdapter {
//
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
//                .enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
//                .enable(SerializationFeature.WRITE_ENUMS_USING_INDEX)       //需要开启这个配置将枚举类型的字段以序号进行序列化和反序列化
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
