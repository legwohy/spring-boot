package com.cobra.util.cryto;

import org.junit.Before;
import org.junit.Test;

/**
 * @author admin
 * @date 2020/12/31 13:38
 * @desc
 */
public class SecretKeyUtilsTest {
    SecretKeyUtils secretKeyUtils = null;
    @Before
    public void init(){
         secretKeyUtils = new SecretKeyUtils();
    }
    @Test
    public void testKeyGenerator() throws Exception{
        secretKeyUtils.testKeyGenerator();

    }

    @Test
    public void testSaveKeyPair2() throws Exception{
        secretKeyUtils.testSaveKeyPair2();
    }

    @Test
    public void testSaveKeyPair() throws Exception{
    }

}