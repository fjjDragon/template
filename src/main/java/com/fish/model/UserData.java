package com.fish.model;

import com.fish.model.bean.GameInfo;

/**
 * @author: fjjdragon
 * @date: 2021-04-10 14:54
 */
public class UserData {

    private int uid;

    private String openid;

    private long cTime;

    private long uTime;

    private GameInfo gameInfo;

    public long getcTime() {
        return cTime;
    }

    public void setcTime(long cTime) {
        this.cTime = cTime;
    }

    public long getuTime() {
        return uTime;
    }

    public void setuTime(long uTime) {
        this.uTime = uTime;
    }
}