package com.cobra.util.aes;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author admin
 * @date 2021/1/4 16:37
 * @desc
 */
public class AesUtilTest {
    @Test
    public void encrypt() throws Exception{
        String content = "i lo 12";
        String seed = "不定长种子";
        String enc = AesUtil.encrypt(content,seed);
        Assert.assertEquals("036FABF88744EA69891C83ED9F60F9EE",enc);

        String origin = AesUtil.decrypt(enc,seed);
        Assert.assertEquals(content,origin);
    }



}