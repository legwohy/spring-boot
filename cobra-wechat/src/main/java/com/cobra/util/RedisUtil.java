
package com.cobra.util;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * redicache 工具类
 */
@SuppressWarnings("unchecked")
@Component
public class RedisUtil
{

    public static final int ORDER_PAY_LOCK_TIMEOUT = 2 * 60 * 60 * 1000; //两小时  2 * 60 * 60 * 1000


    Logger logger = LoggerFactory.getLogger(getClass());

    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys)
    {
        for (String key : keys)
        {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern)
    {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0)
            redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key)
    {
        if (exists(key))
        {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key)
    {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public Object get(final String key)
    {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value)
    {
        boolean result = false;
        try
        {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime)
    {
        boolean result = false;
        try
        {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value, expireTime, TimeUnit.SECONDS);
            //            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取延期时间
     * http://blog.csdn.net/u011911084/article/details/53435172
     */
    public Long getExpireTime(final String key)
    {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     * @author user
     * @date 2018年7月17日
     * @param key
     * @param timeout 单位秒
     * @return
     */
    public Boolean expire(final String key, final long timeout)
    {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public void increment(final String key)
    {
        redisTemplate.boundValueOps(key).increment(1);//val +1
    }

    public long getIncrementValue(final String key)
    {
        return redisTemplate.opsForValue().increment(key, 1);
    }

    public void increment(final String key, int i)
    {
        redisTemplate.boundValueOps(key).increment(i);//val +1
    }

    /***
     * 加锁
     * 防止当俩进程同时读到发现锁超时，都去释放锁，相互覆盖
     * @param key
     * @param value 当前时间+超时时间
     * @return 锁住返回true
     */
    public boolean lock(String key, String value)
    {
        if (redisTemplate.opsForValue().setIfAbsent(key, value))//setNX 返回boolean
        {
            redisTemplate.expire(key, ORDER_PAY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
            return true;
        }
        //如果锁超时 ***
        String currentValue = (String)redisTemplate.opsForValue().get(key);
        if (!StringCommonUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis())
        {
            //获取上一个锁的时间
            String oldvalue = (String)redisTemplate.opsForValue().getAndSet(key, value);
            if (!StringCommonUtils.isEmpty(oldvalue) && oldvalue.equals(currentValue))
            {
                redisTemplate.expire(key, ORDER_PAY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
                return true;
            }
        }
        return false;
    }

    /***
     * 解锁
     * @param key
     * @param value
     * @return
     */
    public void unlock(String key)//, String value)
    {
        try
        {
            String currentValue = (String)redisTemplate.opsForValue().get(key);
            if (!StringCommonUtils.isEmpty(currentValue))
            {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }
        catch (Exception e)
        {
            logger.error("1解锁异常");
        }
    }

}