package com.fish.dao.redis;

import com.fish.model.UserData;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * @author: fjjdragon
 * @date: 2021-04-24 16:35
 */
@Slf4j
public class RedisDao {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> RedisDao.getInstance().shutdown()));
        if (!RedisDao.getInstance().init()) {
            System.exit(0);
        }

        RedisDao.getInstance().test();
//        RedisDao.getInstance().testBucket();
//        RedisDao.getInstance().testKeys();
//        RedisDao.getInstance().testAtomicLong();
//        RedisDao.getInstance().testTopic();

        System.exit(0);
    }

    public static RedisDao getInstance() {
        return RedisDaoHolder.instance;
    }

    static class RedisDaoHolder {
        private static RedisDao instance = new RedisDao();
    }

    public boolean init() {
        try {
            Config config = Config.fromYAML(RedisDao.class.getClassLoader().getResource("redis.yaml"));
            redisson = Redisson.create(config);
            return true;
        } catch (Exception e) {
            log.error("", e);
            return false;
        }
    }

    private RedissonClient redisson;

    private RedisDao() {
    }


    public RedissonClient getRedisson() {
        return redisson;
    }

    public boolean acquire(String lockName) {
        //声明key对象
        String key = RedisKey.storeKey(RedisKey.Lock.LOCK_TITLE, lockName);
        //获取锁对象
        RLock mylock = redisson.getLock(key);
        //加锁，并且设置锁过期时间，防止死锁的产生
        mylock.lock(2, TimeUnit.MINUTES);
        log.warn("======lock======" + Thread.currentThread().getName());
        //加锁成功
        return true;
    }
    //锁的释放

    public void release(String lockName) {
        //必须是和加锁时的同一个key
        String key = RedisKey.storeKey(RedisKey.Lock.LOCK_TITLE, lockName);
        //获取所对象
        RLock mylock = redisson.getLock(key);
        //释放锁（解锁）
        mylock.unlock();
        log.warn("======unlock======" + Thread.currentThread().getName());
    }

    public void test() {
        RBucket testRedisson_1 = redisson.getBucket("testRedisson_str1");
        Object o = testRedisson_1.get();
        if (null != o) {
            System.out.println(o);
        }
        RKeys keys = redisson.getKeys();
        RType type = keys.getType("testRedisson_str1");
        System.out.println("testRedisson_2 ：" + type);

    }

    public void testBucket() {
        RBucket<Object> testRedisson_bucket = redisson.getBucket("testRedisson_bucket");
        UserData userData = new UserData();
        userData.setUid(1234);
        userData.setuTime(1622192656479L);
        userData.setOpenid("ecXPh48CuEl04bbig81U");
//        testRedisson_bucket.set(userData);

        UserData o = (UserData) testRedisson_bucket.get();
        System.out.println(o);
    }

    public void testKeys() {
        RKeys keys = redisson.getKeys();
        long count = keys.count();
        System.out.println(count);

        RType testRedisson_bucket = keys.getType("testRedisson_bucket");
        System.out.println(testRedisson_bucket);

//        int i = 0;
//        Iterator<String> iterator = keys.getKeys().iterator();
//        while (iterator.hasNext()) {
//            i++;
//            String next = iterator.next();
//            System.out.println(keys.getType(next) + ":" + next);
//        }

//        RType test = keys.getType("test");
//        System.out.println(test);
//        System.out.println(i);
    }

    public void testAtomicLong() {
        RAtomicLong testRedisson_atomicLong = redisson.getAtomicLong("testRedisson_atomicLong");
        System.out.println("=============testRedisson_atomicLong======================================");
        long l = testRedisson_atomicLong.get();
        System.out.println(l);
        long l1 = testRedisson_atomicLong.incrementAndGet();
        System.out.println(l1);
        System.out.println("===================================================");

        RLongAdder testRedisson_longAdder = redisson.getLongAdder("testRedisson_longAdder");
        System.out.println("=============testRedisson_longAdder======================================");
        long sum = testRedisson_longAdder.sum();
        System.out.println(sum);
        testRedisson_longAdder.increment();
        sum = testRedisson_longAdder.sum();
        System.out.println(sum);
        System.out.println("===================================================");
    }

    public void testTopic() {
        RTopic testRedisson_topic = redisson.getTopic("testRedisson_topic");
        System.out.println("=============testRedisson_topic======================================");

        UserData userData = new UserData();
        userData.setUid(6666);
        userData.setuTime(1622194033808L);
        userData.setOpenid("ecXPh4812456788545L");


        RTopic topic = redisson.getTopic("testRedisson_topic");
        topic.addListenerAsync(UserData.class, (channel, msg) -> {
            System.out.println(channel);
            System.out.println(msg);
        });

        testRedisson_topic.publish(userData);
        System.out.println("===================================================");
    }

    public void shutdown() {
        if (null != redisson) {
            redisson.shutdown();
        }
    }

}