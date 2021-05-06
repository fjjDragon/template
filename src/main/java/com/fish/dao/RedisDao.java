package com.fish.dao;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RType;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author: fjjdragon
 * @date: 2021-04-24 16:35
 */
@Slf4j
public class RedisDao {

    private RedissonClient client;

    private RedisDao() {

    }

    public static RedisDao getInstance() {
        return RedisDaoHolder.instance;
    }

    static class RedisDaoHolder {
        private static RedisDao instance = new RedisDao();

    }

    public static void main(String[] args) {
        RedisDao.getInstance().init();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> RedisDao.getInstance().shutdown()));
        System.exit(0);
    }

    public boolean init() {
        try {

            Config config = Config.fromYAML(RedisDao.class.getClassLoader().getResource("redis.yaml"));

            client = Redisson.create(config);

            RKeys keys = client.getKeys();

            int i = 0;
//            Iterator<String> iterator = keys.getKeys().iterator();
//            while (iterator.hasNext()) {
//                i++;
//                String next = iterator.next();
//                System.out.println(keys.getType(next) + ":" + next);
//            }
            RType test = keys.getType("test");
            System.out.println(test);
            System.out.println(i);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }

    }

    public void shutdown() {
        if (null != client) {
            client.shutdown();
        }
    }

}