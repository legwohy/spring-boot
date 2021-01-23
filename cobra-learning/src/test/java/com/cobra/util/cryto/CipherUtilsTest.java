package com.cobra.util.cryto;

import com.cobra.util.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author admin
 * @date 2020/12/31 11:23
 * @desc
 */
@Slf4j
public class CipherUtilsTest
{
    String seed = "i love yi tiao cai 不限制长度";
    String srcPlainText = "123456";

    String pubKey = null;
    String priKey = null;

    @Before
    public void init() throws Exception
    {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, new SecureRandom(seed.getBytes()));
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();//公钥
        PrivateKey privateKey = keyPair.getPrivate();//私钥

        pubKey = Base64.encodeBase64String(publicKey.getEncoded());
        priKey = new String(Base64.decodeBase64(privateKey.getEncoded()));
    }

    @Test
    public void cipherAESForCBC() throws Exception
    {

        String cipherText = CipherUtils.cipherAESForEncrypt(srcPlainText, seed);
        Assert.assertEquals("zc+k23lxO7VfwsdZPcJWFQ==", cipherText);

        String plainText = CipherUtils.cipherAESForDecrypt(cipherText, seed);
        Assert.assertEquals(srcPlainText, plainText);

    }

    @Test
    public void cipherAESForECB() throws Exception
    {
        seed = "1234567890123456";

        String cipherText = CipherUtils.cipherAESForEncECB(srcPlainText, seed);
        //Assert.assertEquals("yXVUkR45PFz0UfpbDB8/ew==", cipherText);

        String plainText = CipherUtils.cipherAESForDecECB(cipherText, seed);
        Assert.assertEquals(srcPlainText, plainText);

    }

    @Test
    public void cipherDES() throws Exception
    {

        String cipherText = CipherUtils.cipherDESForEnc(srcPlainText, seed);
        Assert.assertEquals("xai3aqsAWIM=", cipherText);

        String plainText = CipherUtils.cipherDESForDec(cipherText, seed);
        Assert.assertEquals(srcPlainText, plainText);

    }

    @Test
    public void cipher3DES() throws Exception
    {
        //TODO EOP 3DES
        seed = "asiainfo3Des";
        srcPlainText = "17717552884";
        String cipherText = CipherUtils.encryptFor3DEs(srcPlainText, seed);
        Assert.assertEquals("lqCXpCxTOAShMjm2AdKzdQ==", cipherText);

        String plainText = CipherUtils.decryptFor3DEs(cipherText, seed);
        Assert.assertEquals(srcPlainText, plainText);

    }

    public static void main(String[] args)
    {
        System.out.println(Base64Util.encode("698663d8d064266e2ead8d1d19cc5166698663d8d064266e2ead8d1d"));
    }

    @Test
    public void cipherAESForCredit() throws Exception
    {
        // TODO CREDIT
        srcPlainText = "123456";
        String cipherText = CipherUtils.cipherAESForEncCredit(srcPlainText, "1234567890123456");

        Assert.assertEquals("j/O5H1ZOR4O1gm3aTnYMWw==", cipherText);
    }

    @Test
    public void cipherRSA() throws Exception
    {
        srcPlainText = "123456";

        String cipherText = CipherUtils.cipherRSAPublic(srcPlainText, pubKey);
        Assert.assertNotNull(cipherText);

        String plainText = CipherUtils.cipherRSAPrivate(cipherText, priKey);
        Assert.assertEquals(srcPlainText, plainText);
    }


    @Test
    public void testDES(){
        String content = "12";
        String seed = "123";
        String ivs = "1233";
        List<String> desAlgList = Arrays.asList("des", "DES/CBC/NoPadding", "DES/CBC/PKCS5Padding",
                        "DES/ECB/NoPadding", "DES/ECB/PKCS5Padding",
                        "DESede", "DESede/CBC/NoPadding","DESede/ECB/NoPadding",
                        "DESede/CBC/PKCS5Padding",
                         "DESede/ECB/PKCS5Padding");
        for (String alg : desAlgList) {
            try
            {
                Assert.assertNotNull(CipherUtils.doEncryptDES(alg, Cipher.ENCRYPT_MODE, seed, ivs,"1", content));
            }
            catch (Exception e)
            {
                System.out.println("异常:"+alg);
                e.printStackTrace();
                return;
            }
        }
    }

    @Test
    public void testAES(){
        String content = "12";
        String seed = "123";
        String ivs = "1233";
        List<String> desAlgList = Arrays.asList("AES/CBC/NoPadding","AES/CBC/PKCS5Padding",
                        "AES/ECB/NoPadding","AES/ECB/PKCS5Padding");
        for (String alg : desAlgList) {
            try
            {
                Assert.assertNotNull(CipherUtils.doEncryptForAES(alg, Cipher.ENCRYPT_MODE, seed, ivs,"0", content));
            }
            catch (Exception e)
            {
                System.out.println("异常:"+alg);
                e.printStackTrace();
                return;
            }
        }
    }

}