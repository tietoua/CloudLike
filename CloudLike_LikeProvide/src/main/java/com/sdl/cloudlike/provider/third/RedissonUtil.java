package com.sdl.cloudlike.provider.third;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;
import org.redisson.config.Config;

import java.security.PublicKey;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @title: RedissonUtil
 * @description:
 * @Author 宋岱霖
 * @Date: 2020/11/2 22:44
 */

public class RedissonUtil {
    private static RedissonClient client;
    static {
        //配置
        Config config=new Config();
        config .setThreads(1000);
        config.useSingleServer().setAddress("redis://39.105.189.141:6380").setPassword("qfjava");//
        client=Redisson.create(config);
    }
    //新增
    public static void setStr(String key,String val){
        client.getBucket(key).set(val);
    }
    public static void setStr(String key,String val,long second){
        client.getBucket(key).set(val,second, TimeUnit.SECONDS);
    }
    public static void setSet(String key,String val,long second){
        client.getSet(key).add(val);
    }
    public static void setSet(String key, Set<Object> vals){
        client.getSet(key).addAll(vals);
    }
    public static void setZset(String key,double score,String vals){
        client.getScoredSortedSet(key).add(score,vals);
    }
    public static void setZset(String key, Map<Object,Double> vals){
        client.getScoredSortedSet(key).addAll(vals);
    }
    //查询
    public static String getStr(String key){
        return client.getBucket(key).get().toString();
    }
    public static Set<Object> getSet(String key){
        return client.getSet(key).readAll();
    }
    public static Collection<Object> getZset(String key){
        return client.getSet(key).readAll();
    }
    public static Collection<Object> getZSet(String key){
        return client.getScoredSortedSet(key).readAll();
    }
    public static Collection<ScoredEntry<Object>> getZSet(String key, double score){
        return client.getScoredSortedSet(key).entryRange(score,true,System.currentTimeMillis(),true);
        //return client.getScoredSortedSet(key).readSort(SortOrder.ASC,score,);
    }
    //校验
    public static boolean checkSet(String key,Object val){
        return client.getSet(key).contains(val);
    }
    public static boolean checkZset(String key,Object val){
        return client.getScoredSortedSet(key).contains(val);
    }
    public static boolean checkKey(String key){
        return client.getKeys().countExists(key)>0;
    }
    //设置有效期
    public static void setTime(String key,long time,TimeUnit timeUnit){
        client.getKeys().expire(key,time,timeUnit);
    }
    //删除key
    public static void delKeys(String... keys){
        client.getKeys().delete(keys);
    }
    //移除元素
    public static boolean delZSet(String key,String val){
        return client.getScoredSortedSet(key).remove(val);
    }
    //分布式锁 =redis(setnx)+Java(Lock)
    //加锁
    public static void lock(String key){
        client.getLock(key).lock();
    }
    //加锁 设置默认的超市时间
    public static void lock(String key,long sceonds){
        client.getLock(key).lock(sceonds,TimeUnit.SECONDS);
    }
    //释放锁
    public static  void unLock(String key){
        client.getLock(key).unlock();
    }
   //模糊查询
    public static Iterable<String> getKeys(String pat){
        return client.getKeys().getKeysByPattern(pat);
    }

}















