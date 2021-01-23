package com.cobra.util.cryto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author admin
 * @date 2020/12/31 13:38
 * @desc
 */
public class SecretKeyUtilsTest {


    @Test
    public void generateKey() throws Exception{
        Assert.assertEquals("MTIzNDU2",SecretKeyUtils.generateKey("AES","123456"));
    }

}