package com.fish;

import com.fish.dao.redis.RedisDao;
import com.fish.dao.redis.RedisKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import proto.Data.User.User;

import java.lang.reflect.Array;

/**
 * @descript:
 * @author: fjjDragon
 * @create: 2021-06-24 16:37
 **/

public class UnitTest {
    @Before
    public void before() {
        System.out.println("before test...");
        if (!RedisDao.getInstance().init()) {
            System.exit(0);
        }
    }

    @Test
    public void a() {
        System.out.println("test...");
        RedissonClient redisson = RedisDao.getInstance().getRedisson();
        String key = RedisKey.storeKey("testProto", "");
        RBucket<Object> bucket = redisson.getBucket(key);

        long currentTimeMillis = System.currentTimeMillis();
        Object o = bucket.get();
        if (null == o) {
            User.UserGameData.Builder builder = User.UserGameData.newBuilder();
            builder.setUid(1234);
            builder.setGold(0);
            builder.setSilver(0);
            User.ItemData.Builder items = User.ItemData.newBuilder();
            items.addItems(User.Item.newBuilder().setId(1).setCount(1));
            items.setUpdateTime(currentTimeMillis);
            builder.setItems(items);

            User.SkillData.Builder skills = User.SkillData.newBuilder();
            skills.addSkills(User.Skill.newBuilder().setId(1));
            skills.setUpdateTime(currentTimeMillis);
            builder.setSkills(skills);

            bucket.set(builder.build().toByteArray());
        } else {
            if (o instanceof byte[]) {
                try {
                    User.UserGameData parseObj = User.UserGameData.parseFrom((byte[]) o);
                    System.out.println();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @After
    public void After() {
        System.out.println("after test...");
        RedisDao.getInstance().shutdown();
    }
}