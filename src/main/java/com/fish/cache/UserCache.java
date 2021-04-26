package com.fish.cache;

import com.fish.model.UserData;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: fjjdragon
 * @date: 2021-04-25 23:00
 */
public class UserCache {
    //玩家数据缓存<uid ,< uid,userInfo >>
    public static ConcurrentHashMap<Integer, UserData> userCache = new ConcurrentHashMap<>();


}