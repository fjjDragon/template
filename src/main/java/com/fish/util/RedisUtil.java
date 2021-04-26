//package com.fish.util;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import redis.clients.jedis.*;
//
//import java.util.*;
//
///**
// * @author: fjjdragon
// * @date: 2021-04-24 16:06
// */
//public class RedisUtil {
//
//    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
//
//    //    集群模式
//    private JedisCluster jedisCluster;
//
//    public List<String> scan(String keys) {
//        List<String> result = new ArrayList<>();
//        try {
//            String cursor = ScanParams.SCAN_POINTER_START;
//            ScanParams scanParams = new ScanParams();
//            scanParams.match(keys);// 匹配的 key
//            scanParams.count(1000);
////            while (true) {
////                //使用scan命令获取数据，使用cursor游标记录位置，下次循环使用
////                ScanResult<String> scanResult = jedisCluster.scan(cursor, scanParams);
////                cursor = scanResult.getStringCursor();// 返回0 说明遍历完成
////                List<String> list = scanResult.getResult();
////                if (null != list && list.size() > 0)
////                    result.addAll(list);
////                if ("0".equals(cursor)) {
////                    break;
////                }
////            }
////            //集群模式使用Jedis执行scan，要改成这种写法
//            for (JedisPool pool : jedisCluster.getClusterNodes().values()) {
//                String cur = ScanParams.SCAN_POINTER_START;
//                do {
//                    try (Jedis jedis = pool.getResource()) {
//                        ScanResult<String> scanResult = jedis.scan(cur, scanParams);
//                        result.addAll(scanResult.getResult());
//                        cur = scanResult.getStringCursor();
//                    }
//                    if (result.size() >= 1000) break;
//                } while (!cur.equals(ScanParams.SCAN_POINTER_START));
//                if (result.size() >= 1000) break;
//            }
//
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return result;
//    }
//
//    public String get(String key) {
//        try {
//            return jedisCluster.get(key);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return "";
//    }
//
//    public byte[] get(byte[] key) {
//        try {
//            return jedisCluster.get(key);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return null;
//    }
//
//    public String set(String key, String value) {
//        try {
//            return jedisCluster.set(key, value);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return "";
//    }
//
//    public String set(byte[] key, byte[] value) {
//        try {
//            return jedisCluster.set(key, value);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return "";
//    }
//
//    public String setex(byte[] key, byte[] value, int ex) {
//        try {
//            return jedisCluster.setex(key, ex, value);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return "";
//    }
//
//    public long del(String key) {
//        try {
//            return jedisCluster.del(key);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    public long del(byte[] key) {
//        try {
//            return jedisCluster.del(key);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    public boolean exists(String key) {
//        try {
//            return jedisCluster.exists(key);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return false;
//    }
//
//    public long incr(String key) {
//        try {
//            return jedisCluster.incr(key);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    public long incrBy(String key, long add) {
//        try {
//            return jedisCluster.incrBy(key, add);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    public boolean getbit(String key, long offset) {
//        try {
//            return jedisCluster.getbit(key, offset);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return false;
//    }
//
//    public Boolean setbit(String key, long offset, boolean value) {
//        try {
//            return jedisCluster.setbit(key, offset, value);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return false;
//    }
//
//    public long expire(String key, int seconds) {
//        try {
//            return jedisCluster.expire(key, seconds);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    public long bitcount(String key) {
//        try {
//            return jedisCluster.bitcount(key);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    // hash
//    //============================================================================
//    public String hget(String hash, String field) {
//        try {
//            return jedisCluster.hget(hash, field);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return "";
//    }
//
//    public byte[] hget(byte[] hash, byte[] field) {
//        try {
//            return jedisCluster.hget(hash, field);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return null;
//    }
//
//    public Map<String, String> hgetAll(String hash) {
//        try {
//            return jedisCluster.hgetAll(hash);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return new HashMap<>(2);
//    }
//
//    public long hset(String hash, String field, String value) {
//        try {
//            return jedisCluster.hset(hash, field, value);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    public long hset(byte[] hash, byte[] field, byte[] value) {
//        try {
//            return jedisCluster.hset(hash, field, value);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//
//    public long hdel(String hash, String field) {
//        try {
//            return jedisCluster.hdel(hash, field);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    public long hdel(byte[] hash, byte[] field) {
//        try {
//            return jedisCluster.hdel(hash, field);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    public long hlen(String hash) {
//        try {
//            return jedisCluster.hlen(hash);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    public long hlen(byte[] hash) {
//        try {
//            return jedisCluster.hlen(hash);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//
//    public Collection<byte[]> hvals(byte[] hash) {
//        Jedis jedis = null;
//        try {
//            return jedisCluster.hvals(hash);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return new ArrayList<byte[]>();
//    }
//
//    // set
//    //============================================================================
//    public long sadd(String key, String member) {
//        try {
//            return jedisCluster.sadd(key, member);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
//        try {
//            return jedisCluster.sscan(key, cursor, params);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return null;
//    }
//
//    // zset
//    //============================================================================
//    public static long low4Byte = 0x00000000ffffffffL;
//    public static long deduct = 0x01000000ffffffffL;
//    // 分数(int值)+时间戳（秒） 组合排序
//
//    public long zComposeAdd(String key, int score, int timemillis, String member) {
//        try {
//            long sc = ((long) score) << 32;
//            long time = timemillis;
//            long retime = deduct - time;
//            retime = retime & low4Byte;
//            long value = sc + retime;
//
//            return jedisCluster.zadd(key, value, member);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//
//    public long zadd(String key, double score, String member) {
//        try {
//            return jedisCluster.zadd(key, score, member);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return -1;
//    }
//
//    public Double zscore(String key, String member) {
//        try {
//            return jedisCluster.zscore(key, member);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return null;
//    }
//
//    public Long zcard(String key) {
//        try {
//            return jedisCluster.zcard(key);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return null;
//    }
//
//    public Double zincrby(String key, double score, String member) {
//        try {
//            return jedisCluster.zincrby(key, score, member);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return null;
//    }
//
//    public Set<String> zrevrange(String key, long max, long min) {
//        try {
//            return jedisCluster.zrevrange(key, max, min);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return new LinkedHashSet<String>();
//    }
//
//    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
//        try {
//            return jedisCluster.zrevrangeWithScores(key, start, end);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return new LinkedHashSet<Tuple>();
//    }
//
//    public Set<String> zrangeByScore(String key, double min, double max) {
//        try {
//            return jedisCluster.zrangeByScore(key, min, max);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return new LinkedHashSet<String>();
//    }
//
//    public Set<Tuple> zrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
//        try {
//            return jedisCluster.zrangeByScoreWithScores(key, max, min, offset, count);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return new LinkedHashSet<Tuple>();
//    }
//
//    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
//        try {
//            return jedisCluster.zrevrangeByScoreWithScores(key, max, min, offset, count);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return new LinkedHashSet<Tuple>();
//    }
//
//
//    public Long zrevrank(String key, String member) {
//        try {
//            return jedisCluster.zrevrank(key, member);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return null;
//    }
//
//    public Long zremMember(String key, String... member) {
//        try {
//            return jedisCluster.zrem(key, member);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return null;
//    }
//
//    public Long zremrangeByRank(String key, long start, long end) {
//        try {
//            return jedisCluster.zremrangeByRank(key, start, end);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return null;
//    }
//
//
//    public Long zcount(String key, double min, double max) {
//        try {
//            return jedisCluster.zcount(key, min, max);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//        return null;
//    }
//
//    // publish / subscribe
//    //============================================================================
//    public void subscribe(BinaryJedisPubSub jedisPubSub, byte[] channel) {
//        try {
//            jedisCluster.subscribe(jedisPubSub, channel);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//    }
//
//    public void publish(byte[] channel, byte[] message) {
//        try {
//            jedisCluster.publish(channel, message);
//        } catch (Exception e) {
//            logger.error("", e);
//        }
//    }
//    //============================================================================
//
//    /**
//     * =======================================================================================
//     */
////        单机模式
////    private JedisPool jedisPool;
////
////    public List<String> scan(String keys) {
////        Jedis jedis = null;
////        List<String> result = new ArrayList<>();
////        try {
////            jedis = jedisPool.getResource();
////            String cursor = ScanParams.SCAN_POINTER_START;
////            ScanParams scanParams = new ScanParams();
////            scanParams.match(keys);// 匹配的 key
////            scanParams.count(1000);
////            while (true) {
////                //使用scan命令获取数据，使用cursor游标记录位置，下次循环使用
////                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
////                cursor = scanResult.getStringCursor();// 返回0 说明遍历完成
////                List<String> list = scanResult.getResult();
////                if (null != list && list.size() > 0)
////                    result.addAll(list);
////                if ("0".equals(cursor)) {
////                    break;
////                }
////            }
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return result;
////    }
////
////    public String get(String key) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.get(key);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
//////                jedis.getClient().resetPipelinedCount();
////                jedis.close();
////            }
////        }
////        return "";
////    }
////
////    public byte[] get(byte[] key) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.get(key);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return null;
////    }
////
////    public String set(String key, String value) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.set(key, value);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return "";
////    }
////
////    public String set(byte[] key, byte[] value) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.set(key, value);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return "";
////    }
////
////    public String setex(byte[] key, byte[] value, int ex) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.setex(key, ex, value);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return "";
////    }
////
////    public long del(String key) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.del(key);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    public long del(byte[] key) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.del(key);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    public boolean exists(String key) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.exists(key);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return false;
////    }
////
////    public long incr(String key) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.incr(key);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    public long incrBy(String key, long add) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.incrBy(key, add);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    public boolean getbit(String key, long offset) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.getbit(key, offset);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return false;
////    }
////
////    public Boolean setbit(String key, long offset, boolean value) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.setbit(key, offset, value);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return false;
////    }
////
////    public long expire(String key, int seconds) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.expire(key, seconds);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    public long bitcount(String key) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.bitcount(key);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    // hash
////    //============================================================================
////    public String hget(String hash, String field) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.hget(hash, field);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return "";
////    }
////
////    public byte[] hget(byte[] hash, byte[] field) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.hget(hash, field);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return null;
////    }
////
////    public Map<String, String> hgetAll(String hash) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.hgetAll(hash);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return new HashMap<>(2);
////    }
////
////    public long hset(String hash, String field, String value) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.hset(hash, field, value);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    public long hset(byte[] hash, byte[] field, byte[] value) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.hset(hash, field, value);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////
////    public long hdel(String hash, String field) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.hdel(hash, field);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    public long hdel(byte[] hash, byte[] field) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.hdel(hash, field);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    public long hlen(String hash) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.hlen(hash);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    public long hlen(byte[] hash) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.hlen(hash);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////
////    public List<byte[]> hvals(byte[] hash) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.hvals(hash);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return new ArrayList<byte[]>();
////    }
////    // list
////    //============================================================================
////
////    // set
////    //============================================================================
////    public long sadd(String key, String member) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.sadd(key, member);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.sscan(key, cursor, params);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return null;
////    }
////
////    // zset
////    //============================================================================
////    public static long low4Byte = 0x00000000ffffffffL;
////    public static long deduct = 0x01000000ffffffffL;
////    // 分数(int值)+时间戳（秒） 组合排序
////
////    public long zComposeAdd(String key, int score, int timemillis, String member) {
////        Jedis jedis = null;
////        try {
////            long sc = ((long) score) << 32;
////            long time = timemillis;
////            long retime = deduct - time;
////            retime = retime & low4Byte;
////            long value = sc + retime;
////
////            jedis = jedisPool.getResource();
////            return jedis.zadd(key, value, member);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////
////    public long zadd(String key, double score, String member) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zadd(key, score, member);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return -1;
////    }
////
////    public Double zscore(String key, String member) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zscore(key, member);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return null;
////    }
////
////
////    public Long zcard(String key) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zcard(key);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return null;
////    }
////
////    public Double zincrby(String key, double score, String member) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zincrby(key, score, member);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return null;
////    }
////
////    public Set<String> zrevrange(String key, long max, long min) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zrevrange(key, max, min);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return new LinkedHashSet<String>();
////    }
////
////    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zrevrangeWithScores(key, start, end);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return new LinkedHashSet<Tuple>();
////    }
////
////    public Set<String> zrangeByScore(String key, double min, double max) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zrangeByScore(key, min, max);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return new LinkedHashSet<String>();
////    }
////
////    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return new LinkedHashSet<Tuple>();
////    }
////
////    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return new LinkedHashSet<Tuple>();
////    }
////
////
////    public Long zrevrank(String key, String member) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zrevrank(key, member);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return null;
////    }
////
////    public Long zremMember(String key, String... member) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zrem(key, member);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return null;
////    }
////
////    public Long zremrangeByRank(String key, long start, long end) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zremrangeByRank(key, start, end);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return null;
////    }
////
////
////    public Long zcount(String key, double min, double max) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            return jedis.zcount(key, min, max);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////        return null;
////    }
////
////    // publish / subscribe
////    //============================================================================
////    public void subscribe(BinaryJedisPubSub jedisPubSub, byte[] channel) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            jedis.subscribe(jedisPubSub, channel);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////    }
////
////    public void publish(byte[] channel, byte[] message) {
////        Jedis jedis = null;
////        try {
////            jedis = jedisPool.getResource();
////            jedis.publish(channel, message);
////        } catch (Exception e) {
////            logger.error("", e);
////        } finally {
////            if (null != jedis) {
////                jedis.close();
////            }
////        }
////    }
//    //============================================================================
//}