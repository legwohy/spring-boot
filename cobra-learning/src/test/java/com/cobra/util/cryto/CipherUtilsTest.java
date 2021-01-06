package com.cobra.util.cryto;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.*;

/**
 * @author admin
 * @date 2020/12/31 11:23
 * @desc
 */
public class CipherUtilsTest {
    String seed = "i love yi tiao cai 不限制长度";
    String content = "123456";

    String pubKey = null;
    String priKey = null;

    @Before
    public void init() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024,new SecureRandom(seed.getBytes()));
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();//公钥
        PrivateKey privateKey = keyPair.getPrivate();//私钥

        pubKey = Base64.encodeBase64String(publicKey.getEncoded());
        priKey = new String(Base64.encodeBase64(privateKey.getEncoded()));
    }

    @Test
    public void cipherAESForCBC() throws Exception{

        String enc = CipherUtils.cipherAESForEncrypt(content, seed);
        Assert.assertEquals("zc+k23lxO7VfwsdZPcJWFQ==", enc);

        String dec = CipherUtils.cipherAESForDecrypt(enc, seed);
        Assert.assertEquals(content, dec);

    }

    @Test
    public void cipherAESForECB() throws Exception{
        seed = "1234567890123456";

        String enc = CipherUtils.cipherAESForEncECB(content, seed);
        Assert.assertEquals("yXVUkR45PFz0UfpbDB8/ew==", enc);

        String dec = CipherUtils.cipherAESForDecECB(enc, seed);
        Assert.assertEquals(content, dec);

    }

    @Test
    public void cipherDES() throws Exception{

        String enc = CipherUtils.cipherDESForEnc(content, seed);
        Assert.assertEquals("xai3aqsAWIM=", enc);

        String dec = CipherUtils.cipherDESForDec(enc, seed);
        Assert.assertEquals(content, dec);

    }

    @Test
    public void cipher3DES() throws Exception{
        //TODO EOP 3DES
        seed = "asiainfo3Des";
        String enc = CipherUtils.encryptFor3DEs(content, seed);
        Assert.assertEquals("C2RbfjNDQgg=", enc);

        String dec = CipherUtils.decryptFor3DEs(enc, seed);
        Assert.assertEquals(content, dec);

    }


    @Test
    public void cipherAESForCredit() throws Exception{
        // TODO CREDIT
        content = "123456";
        String enc = CipherUtils.cipherAESForEncCredit(content, "1234567890123456");

        Assert.assertEquals("j/O5H1ZOR4O1gm3aTnYMWw==", enc);
    }

    @Test
    public void cipherRSA() throws Exception{
        content = "123456";

        String enc = CipherUtils.cipherRSAPublic(content, pubKey);
        Assert.assertNotNull(enc);

        String dec = CipherUtils.cipherRSAPrivate(enc, priKey);
        Assert.assertEquals(content, dec);
    }

}