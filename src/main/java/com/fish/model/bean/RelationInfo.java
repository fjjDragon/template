package com.fish.model.bean;

import java.util.HashSet;

/**
 * @author: fjjdragon
 * @date: 2021-05-06 23:18
 */
public class RelationInfo {
    private int uid;
    // 唯一邀请者id（上级）
    private int inviterId;
    // 下级雇员（非必要）
    private HashSet employees;

    public RelationInfo(int uid, int inviterId) {
        this.uid = uid;
        this.inviterId = inviterId;
    }

    public HashSet getEmployees() {
        return employees;
    }

    public void setEmployees(HashSet employees) {
        this.employees = employees;
    }
}