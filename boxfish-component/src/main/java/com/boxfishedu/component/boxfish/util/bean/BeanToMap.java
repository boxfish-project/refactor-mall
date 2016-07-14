package com.boxfishedu.component.boxfish.util.bean;


import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;


public interface BeanToMap {

    default Map<String, Object> toMap() throws Exception {
        Map<String, Object> map = new TreeMap<>();

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            map.put(f.getName(), f.get(this));
        }
        return map;
    }

}
