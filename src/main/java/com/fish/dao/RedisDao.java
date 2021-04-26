package com.fish.dao;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author: fjjdragon
 * @date: 2021-04-24 16:35
 */
@Slf4j
public class RedisDao {

    public static void main(String[] args) {
        init();
    }

    public static boolean init() {
        try {

            Config config = Config.fromYAML(RedisDao.class.getClassLoader().getResource("redis.yaml"));

            // or read config from file
//            config = Config.fromYAML(new File("config-file.yaml"));
            // 2. Create Redisson instance

            // Sync and Async API
            RedissonClient redisson = Redisson.create(config);
//
//            // Reactive API
//            RedissonReactiveClient redissonReactive = redisson.reactive();
//
//            // RxJava3 API
//            RedissonRxClient redissonRx = redisson.rxJava();
            System.out.println();
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }

    }


}