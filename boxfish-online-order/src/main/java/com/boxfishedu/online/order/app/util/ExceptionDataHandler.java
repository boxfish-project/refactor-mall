package com.boxfishedu.online.order.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class ExceptionDataHandler {

    public static void saveTemporary(Long orderId) {
        saveToFile("order_id", orderId.toString());
    }

    public static void saveTemporary(String orderCode) {
        saveToFile("order_code", orderCode);
    }

    private static void saveToFile(final String keyName, final String keyValue) {
        FileOutputStream fileOutputStream = null;
        try {
            File exceptionFile = new File("./exception-data.json");

            fileOutputStream = new FileOutputStream(exceptionFile);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
            String json = "{\"" + keyName + "\":\"" + keyValue + "\",\"timestamp\":\"" + simpleDateFormat.format(Calendar.getInstance().getTime()) + "\"}\n";
            fileOutputStream.write(json.getBytes());
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!Objects.isNull(fileOutputStream)) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
