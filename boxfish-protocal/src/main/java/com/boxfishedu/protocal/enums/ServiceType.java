package com.boxfishedu.protocal.enums;

public enum ServiceType {

    FORIEIGN_TEACHER,
    CHINESE_TEACHER;

    @Override
    public String toString() {
        return "" + super.ordinal();
    }
}
