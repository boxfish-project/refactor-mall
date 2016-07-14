package com.boxfishedu.component.boxfish.util.string;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;


public class FormatUtil {

    // For json
    static ObjectMapper objectMapper = new ObjectMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)       // 属性为空（“”）或者为 NULL 都不序列化
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" + ".SSS"))
            .registerModule(new JodaModule());

    // For xml
    static XmlMapper xmlMapper = (XmlMapper) new XmlMapper()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" + ".SSS"));


    static public String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * 适用于日志输出时使用
     */
    static public String toJsonNoException(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    static public <T> T fromJson(String json, Class<T> type) throws IOException {
        return objectMapper.readValue(json, type);
    }


    static public String toXml(Object object) throws JsonProcessingException {
        return xmlMapper.writeValueAsString(object);
    }

    static public <T> T fromXml(String xml, Class<T> type) throws IOException {
        return xmlMapper.readValue(xml, type);
    }

    static public String getProperty(String json, String propName) {
        try {
            if (StringUtils.isEmpty(json)) {
                return "";
            }
            HashMap properties = objectMapper.readValue(json, HashMap.class);
            Object o = properties.get(propName);
            if (o == null) {
                return "";
            }
            return o.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
