package com.boxfishedu.online.order.app.util;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * Created by lauzhihao on 2016/05/23.
 * <p>
 * MD5加密工具类
 */
public final class MD5 {

    private static Logger logger = LoggerFactory.getLogger(MD5.class);

    public static String encrypt(String content) {
        if (Strings.isNullOrEmpty(content)) {
            return "";
        }
        MessageDigest digestInstance;
        try {
            digestInstance = MessageDigest.getInstance("MD5");
            digestInstance.update(content.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("sign error!");
        }

        byte[] md = digestInstance.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < md.length; i++) {
            int val = (md[i]) & 0xff;
            if (val < 16)
                sb.append("0");
            sb.append(Integer.toHexString(val));
        }
        logger.info("sign={}", sb.toString().toUpperCase());
        return sb.toString().toUpperCase();
    }
}
