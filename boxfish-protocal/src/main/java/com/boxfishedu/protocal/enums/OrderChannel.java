package com.boxfishedu.protocal.enums;

/**
 * Created by malu on 16/7/13.
 */
public enum OrderChannel {
    STANDARD,       //标准付费订单
    EXPERIENCE,     //免费体验订单
    ADJUST;         //管理端补录订单

    public static boolean contains(OrderChannel value) {
        switch (value) {
            case EXPERIENCE:
                return true;
            case STANDARD:
                return true;
            case ADJUST:
                return true;
            default:
                return false;
        }
    }
}
