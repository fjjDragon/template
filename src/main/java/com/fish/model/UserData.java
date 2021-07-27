package com.fish.model;

import com.fish.model.bean.GameInfo;

import java.util.StringJoiner;

/**
 * @author: fjjdragon
 * @date: 2021-04-10 14:54
 */
public class UserData {

    private int uid;

    private String openID;

    private String unionID;

    // 创号时间戳
    private long cTime;
    // 更新时间戳
    private long uTime;

    private GameInfo gameInfo;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getUnionID() {
        return unionID;
    }

    public void setUnionID(String unionID) {
        this.unionID = unionID;
    }

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

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserData.class.getSimpleName() + "[", "]")
                .add("uid=" + uid)
                .add("openID='" + openID + "'")
                .add("unionID='" + unionID + "'")
                .add("cTime=" + cTime)
                .add("uTime=" + uTime)
                .add("gameInfo=" + gameInfo)
                .toString();
    }
}