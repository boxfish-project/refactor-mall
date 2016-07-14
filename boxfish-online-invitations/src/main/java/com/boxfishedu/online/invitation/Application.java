package com.boxfishedu.online.invitation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Created by malu on 16/6/15.
 */
@SpringBootApplication
@ServletComponentScan
public class Application {
    public static void main(String[] args) { SpringApplication.run(Application.class);}
}
