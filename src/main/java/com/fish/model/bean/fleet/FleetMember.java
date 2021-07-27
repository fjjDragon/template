package com.fish.model.bean.fleet;

/**
 * @author: fjjdragon
 * @date: 2021-05-22 22:59
 */
public class FleetMember {

    private int uid;

    private String nick;
    private String avatar;
    // 今日贡献
    private long todayCont;
    // 总贡献
    private long totalCont;
    // 邀请时间戳
    private int inviteTime;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getTodayCont() {
        return todayCont;
    }

    public void setTodayCont(long todayCont) {
        this.todayCont = todayCont;
    }

    public long getTotalCont() {
        return totalCont;
    }

    public void setTotalCont(long totalCont) {
        this.totalCont = totalCont;
    }

    public int getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(int inviteTime) {
        this.inviteTime = inviteTime;
    }
}