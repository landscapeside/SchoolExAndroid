package com.landscape.schoolexandroid.enums;

/**
 * Created by 1 on 2016/7/7.
 */
public enum SexType {
    MALE("男"),
    FEMALE("女"),
    NONE("未知");

    String value;

    public String getValue() {
        return value;
    }

    SexType(String value) {
        this.value = value;
    }

    public static SexType getSex(String value) {
        if (value.equals("男")) {
            return MALE;
        } else if (value.equals("女")) {
            return FEMALE;
        }
        return NONE;
    }
}
