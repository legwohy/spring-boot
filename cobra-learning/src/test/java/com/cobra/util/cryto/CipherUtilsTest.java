package com.cobra.util.cryto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.*;

import static org.junit.Assert.*;

/**
 * @author admin
 * @date 2020/12/31 11:23
 * @desc
 */
public class CipherUtilsTest
{
    String seed = "i love yi tiao cai 不限制长度";
    String content = "123456";
    PublicKey publicKey = null;
    PrivateKey privateKey = null;

    @Before
    public void init() throws Exception
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();//公钥
        privateKey = keyPair.getPrivate();//私钥
    }

    @Test
    public void cipherAESForCBC() throws Exception
    {

        String enc = CipherUtils.cipherAESForEncrypt(content, seed);
        Assert.assertEquals("zc+k23lxO7VfwsdZPcJWFQ==", enc);

        String dec = CipherUtils.cipherAESForDecrypt(enc, seed);
        Assert.assertEquals(content, dec);

    }
    @Test
    public void cipherAESForEBC() throws Exception
    {
         seed = "i love yi tiao cai 不限制密钥长度";

        String enc = CipherUtils.cipherAESForEncEBC(content, seed);
        Assert.assertEquals("psO7nbLJUqaud+14IfyOIQ==", enc);

        String dec = CipherUtils.cipherAESForDecEBC(enc, seed);
        Assert.assertEquals(content, dec);

    }

    @Test
    public void cipherDES() throws Exception
    {

        String enc = CipherUtils.cipherDESForEnc(content, seed);
        Assert.assertEquals("xai3aqsAWIM=", enc);

        String dec = CipherUtils.cipherDESForDec(enc, seed);
        Assert.assertEquals(content, dec);

    }

    @Test
    public void testRSA() throws Exception
    {
        String enc = CipherUtils.cipherRSAEnc(content, publicKey);

        Assert.assertEquals(content, CipherUtils.cipherRSADec(enc, privateKey));
    }

}