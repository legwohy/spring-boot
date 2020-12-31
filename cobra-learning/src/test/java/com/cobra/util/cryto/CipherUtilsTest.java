package com.cobra.util.cryto;

import org.junit.Assert;
import org.junit.Test;

import java.security.Key;

import static org.junit.Assert.*;

/**
 * @author admin
 * @date 2020/12/31 11:23
 * @desc
 */
public class CipherUtilsTest {
    @Test
    public void cipherAES() throws Exception{
        String key = "i love yi tiao cai";
        String content = "123456";

        String enc = CipherUtils.cipherAESForEncrypt(content, key);
        Assert.assertEquals("iKG36iJyF/h26k4cHc6HPw==", enc);

        String dec = CipherUtils.cipherAESForDecrypt(enc, key);
        Assert.assertEquals(content, dec);

    }

    @Test
    public void cipherDES()throws Exception{
        String key = "i love yi tiao cai";
        String content = "123456";
        String enc = CipherUtils.cipherDESForEnc(content, key);
        Assert.assertEquals("3u6YuwOH9cw=", enc);

        String dec = CipherUtils.cipherDESForDec(enc, key);
        Assert.assertEquals(content, dec);

    }

}