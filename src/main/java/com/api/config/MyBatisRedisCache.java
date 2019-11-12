package com.api.config;

import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class MyBatisRedisCache implements Cache {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private String id;

    private RedisTemplate redisTemplate;

    //判断cache客户端是否已经加载过
    private boolean isLoaded = false;

    //如: mb:keys:com.jsonMapping.dao.JsonMappingDicMapper
    private String keys;

    //如: mb:keys:com.jsonMapping.dao.JsonMappingDicMapper
    private String simpleId;

    //启动的通过该构造器构造, id为mapper的全名, 如:com.jsonMapping.dao.JsonMappingDicMapper
    public MyBatisRedisCache(String id) {
        this.id = id;
        if (id.lastIndexOf(".") > 0) {
            simpleId = id.substring(id.lastIndexOf(".") + 1);
        } else {
            simpleId = id;
        }
        keys = "mb:keys:" + id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        load();
        Lock lock = readWriteLock.writeLock();
        try {
            lock.lock();
            String cacheKey = getKey(key);
            if (null != redisTemplate) {
                redisTemplate.opsForValue().set(cacheKey, value);
                redisTemplate.opsForList().rightPush(keys, cacheKey);
                redisTemplate.expire(cacheKey, 1, TimeUnit.HOURS);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Object getObject(Object key) {
        load();
        Object obj = null;
        if (null != redisTemplate) {
            obj = redisTemplate.opsForValue().get(getKey(key));
        }
        return obj;
    }

    /**
     * 该方法未被框架调用
     */
    @Override
    public Object removeObject(Object key) {
        load();
        if (null != redisTemplate) {
            redisTemplate.delete(getKey(key));
        }
        return key;
    }

    @Override
    public void clear() {
        load();
        Lock lock = readWriteLock.writeLock();
        try {
            lock.lock();
            if (null != redisTemplate) {
                ListOperations listOperations = redisTemplate.opsForList();
                Long size = listOperations.size(keys);
                List keyList = listOperations.range(keys, 0, size);
                if (keyList != null && !keyList.isEmpty()) {
                    redisTemplate.delete(keyList);
                    listOperations.trim(keys, size, -1);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 该方法未被框架调用
     */
    @Override
    public int getSize() {
        load();
        if (null != redisTemplate) {
            Long dbSize = (Long) redisTemplate.execute((RedisCallback) connection -> connection.dbSize());
            return dbSize.intValue();
        }
        return 0;
    }

    /**
     * 该方法未被框架调用
     */
    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    /**
     * 自定义key，防止key冲突
     *
     * @param key key
     * @return 自定义key
     */
    private String getKey(Object key) {
        return "mb:" + simpleId + ":" + key.hashCode();
    }

    private boolean load() {
        if (isLoaded) {
            return true;
        } else {
            synchronized (this) {
                if (isLoaded) {
                    return true;
                } else {
                    redisTemplate = SpringUtil.getBeanReturnNull("redisTemplate", RedisTemplate.class);
                    isLoaded = true;
                }
            }
            return true;
        }
    }
}
