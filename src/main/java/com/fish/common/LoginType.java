package com.fish.common;

/**
 * @author: fjjdragon
 * @date: 2021-07-27 0:07
 */
public enum LoginType {
    /**
     * 外部浏览器
     */
    NULL(0),
    WX_AUTH(1),
    QQ_AUTH(2),
    PHONE_AUTH(3),
    ;
    private int value;

    LoginType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
