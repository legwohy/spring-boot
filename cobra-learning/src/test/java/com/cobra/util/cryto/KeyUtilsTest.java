package com.cobra.util.cryto;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author admin
 * @date 2020/12/31 13:38
 * @desc
 */
public class KeyUtilsTest {
    KeyUtils keyUtils = null;
    @Before
    public void init(){
         keyUtils = new KeyUtils();
    }
    @Test
    public void testKeyGenerator() throws Exception{
        keyUtils.testKeyGenerator();

    }

    @Test
    public void testSaveKeyPair2() throws Exception{
        keyUtils.testSaveKeyPair2();
    }

    @Test
    public void testSaveKeyPair() throws Exception{
    }

}