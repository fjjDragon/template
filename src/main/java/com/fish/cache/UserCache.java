package com.fish.cache;

import com.fish.model.UserData;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

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


    private class Test {

        private final ReentrantLock lock = new ReentrantLock();


        public void test() {
            lock.lock();
            try {
                Thread.sleep(1000L);
                System.out.println(Thread.currentThread().getName() + lock.getHoldCount());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }

        public int getHoldCount() {
            return lock.getHoldCount();
        }

        public int getQueueLength() {
            return lock.getQueueLength();
        }

        public boolean hasQueuedThread(Thread thread) {
            return lock.hasQueuedThread(thread);
        }
    }


    public static void main(String[] args) throws Exception {
        Test test = new UserCache().new Test();
        Runnable runnable = () -> test.test();
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(runnable);
            threads[i].setName("thread-" + i);
        }
        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }
        boolean flag = true;
        while (true) {
            for (int i = 0; i < 10; i++) {
                if (threads[i].isAlive()) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                break;
            }
        }
        System.out.println(test.getQueueLength() + " 个线程等待获取锁");
        System.out.println(test.hasQueuedThread(threads[3]));
        System.out.println("finished.");
    }


}