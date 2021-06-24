package com.fish.dao.redis;

import com.google.common.base.Joiner;

/**
 * @descript:
 * @author: fjjDragon
 * @create: 2021-05-28 10:58
 **/
public class RedisKey {
    public static final String NAMESPACE = "temp_2021";

    private static final Joiner joiner = Joiner.on('_');

    public final static String storeKey(String prefix, String subKey) {
        return joiner.join(NAMESPACE, prefix, subKey);
    }

    public static class Lock {
        public static final String LOCK_TITLE = "redisLock_";
    }

}