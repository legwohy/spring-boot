package com.cobra.util.cryto;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CipherUtilsTest {
    String seed = "i love yi tiao cai 不限制长度";
    String srcPlainText = "123456";

    String pubKey = null;
    String priKey = null;

    @Before
    public void init() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, new SecureRandom(seed.getBytes()));
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();//公钥
        PrivateKey privateKey = keyPair.getPrivate();//私钥

        pubKey = Base64.encodeBase64String(publicKey.getEncoded());
        priKey = new String(Base64.decodeBase64(privateKey.getEncoded()));
    }

    @Test
    public void cipherAESForCBC() throws Exception{

        String cipherText = CipherUtils.cipherAESForEncrypt(srcPlainText, seed);
        Assert.assertEquals("zc+k23lxO7VfwsdZPcJWFQ==", cipherText);

        String plainText = CipherUtils.cipherAESForDecrypt(cipherText, seed);
        Assert.assertEquals(srcPlainText, plainText);

    }

    @Test
    public void cipherAESForECB() throws Exception{
        seed = "1234567890123456";

        String cipherText = CipherUtils.cipherAESForEncECB(srcPlainText, seed);
        Assert.assertEquals("yXVUkR45PFz0UfpbDB8/ew==", cipherText);

        String plainText = CipherUtils.cipherAESForDecECB(cipherText, seed);
        Assert.assertEquals(srcPlainText, plainText);

    }

    @Test
    public void cipherDES() throws Exception{

        String cipherText = CipherUtils.cipherDESForEnc(srcPlainText, seed);
        Assert.assertEquals("xai3aqsAWIM=", cipherText);

        String plainText = CipherUtils.cipherDESForDec(cipherText, seed);
        Assert.assertEquals(srcPlainText, plainText);

    }

    @Test
    public void cipher3DES() throws Exception{
        //TODO EOP 3DES
        seed = "asiainfo3Des";
        String cipherText = CipherUtils.encryptFor3DEs(srcPlainText, seed);
        Assert.assertEquals("C2RbfjNDQgg=", cipherText);

        String plainText = CipherUtils.decryptFor3DEs(cipherText, seed);
        Assert.assertEquals(srcPlainText, plainText);

    }

    @Test
    public void cipherAESForCredit() throws Exception{
        // TODO CREDIT
        srcPlainText = "123456";
        String cipherText = CipherUtils.cipherAESForEncCredit(srcPlainText, "1234567890123456");

        Assert.assertEquals("j/O5H1ZOR4O1gm3aTnYMWw==", cipherText);
    }

    @Test
    public void cipherRSA() throws Exception{
        srcPlainText = "123456";

        String cipherText = CipherUtils.cipherRSAPublic(srcPlainText, pubKey);
        Assert.assertNotNull(cipherText);

        String plainText = CipherUtils.cipherRSAPrivate(cipherText, priKey);
        Assert.assertEquals(srcPlainText, plainText);
    }



}