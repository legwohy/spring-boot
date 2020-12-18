package com.cobra.cache;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;


/**
 * @author admin
 * @date 2020/12/18 10:30
 * @description
 */
public class LocalCacheTest {
    LocalCache localCache;

    @Before
    public void before(){
        localCache = new LocalCache();
    }

    @Test
    public void testCache() throws Exception{
        String cacheKey = "cacheKey";
        String cacheValue = "cacheTest";
        Assert.assertTrue(localCache.putValue(cacheKey, cacheValue, 3));

        // 值存储
        Assert.assertEquals(cacheValue, localCache.getValue(cacheKey));

        // 自动清除
        TimeUnit.SECONDS.sleep(10);
        Assert.assertNull(localCache.getValue(cacheKey));

    }


    @After
    public void clear() throws Exception{
        localCache.clear();
    }

}