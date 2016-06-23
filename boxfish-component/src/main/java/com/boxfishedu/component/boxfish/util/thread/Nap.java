package com.boxfishedu.component.boxfish.util.thread;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nap {

    private static Logger logger = LoggerFactory.getLogger(Nap.class);

    static public void second5() {
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            logger.error("[boxfish-component] [second5] 任务延迟异常. EXCEPTION=[{}]", e.toString());
        }
    }

    static public void second30() {
        try {
            Thread.sleep(30_000);
        } catch (InterruptedException e) {
            logger.error("[boxfish-component] [second30] 任务延迟异常. EXCEPTION=[{}]", e.toString());
        }
    }

    static public void second60() {
        try {
            Thread.sleep(60_000);
        } catch (InterruptedException e) {
            logger.error("[boxfish-component] [second60] 任务延迟异常. EXCEPTION=[{}]", e.toString());
        }
    }
}
