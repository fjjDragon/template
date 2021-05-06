package com.fish.cache;

import com.fish.model.UserData;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: fjjdragon
 * @date: 2021-04-25 23:00
 */
public class UserCache {
    //玩家数据缓存<uid ,< uid,userInfo >>
    private static ConcurrentHashMap<Integer, UserData> userCache = new ConcurrentHashMap<>();

    public static UserData getUserById(int uid) {
        return userCache.get(uid);
    }

    public void addCache(UserData userData) {
        userCache.put(userData.getUid(), userData);
    }

}