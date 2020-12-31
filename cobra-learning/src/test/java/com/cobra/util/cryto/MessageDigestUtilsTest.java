package com.cobra.util.cryto;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author admin
 * @date 2020/12/31 10:40
 * @desc
 */
public class MessageDigestUtilsTest {
    @Test
    public void messageDigest() throws Exception{
        String value = MessageDigestUtils.messageDigest("value11");

        Assert.assertEquals("aa62d3c0d5a884bf8a627142641cfeff",value);
    }

}