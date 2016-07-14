package com.boxfishedu.online.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by malu on 16/7/7.
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.boxfishedu")
@ServletComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
