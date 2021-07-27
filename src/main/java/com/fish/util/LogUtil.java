package com.fish.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: fjjdragon
 * @date: 2021-07-26 15:45
 */
@Slf4j
public class LogUtil {
    public static void logRequestOperation(String str) {
        log.info(str);
    }

}