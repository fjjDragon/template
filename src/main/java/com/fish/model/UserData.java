package com.fish.model;

import com.fish.model.bean.GameInfo;

/**
 * @author: fjjdragon
 * @date: 2021-04-10 14:54
 */
public class UserData {

    private int uid;

    private String openid;

    private long uTime;

    private GameInfo gameInfo;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public long getuTime() {
        return uTime;
    }

    public void setuTime(long uTime) {
        this.uTime = uTime;
    }
}