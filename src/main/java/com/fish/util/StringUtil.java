package com.fish.util;

import java.util.UUID;

/**
 * @author: fjjdragon
 * @date: 2021-04-24 15:18
 */
public class StringUtil {

    public static String uuid() {
        String uuid = UUID.randomUUID().toString();
        StringBuilder s = new StringBuilder();
        return s.append(uuid, 0, 8).append(uuid, 9, 13).append(uuid, 14, 18).append(uuid, 19, 23)
                .append(uuid, 24, uuid.length()).toString();
    }
}