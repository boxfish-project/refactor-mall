package com.boxfishedu.protocal.enums;

/**
 * Created by malu on 16/7/13.
 */
public enum OrderSource {
    ANDROID,        //安卓
    IOS,            //IOS
    ADMIN;          //管理端

    public static boolean contains(OrderSource value) {
        switch (value) {
            case ADMIN:
                return true;
            case ANDROID:
                return true;
            case IOS:
                return true;
            default:
                return false;
        }
    }
}
