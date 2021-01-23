package com.cobra.util.cryto;

import com.cobra.util.cryto.enums.AlgEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        Map<String,String> keyMap = SecretKeyUtils.genRSAKeyPair();

        pubKey = keyMap.get(SecretKeyUtils.PUBLIC_KEY);
        priKey = keyMap.get(SecretKeyUtils.PRIVATE_KEY);
    }

    @Test
    public void cipherAESForCBC() throws Exception
    {

        String cipherText = CipherUtils.cipherAESForEncrypt(srcPlainText, seed);
        Assert.assertEquals("2xih40GBNCYAGQzIRopIGw==", cipherText);

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
        Assert.assertEquals("M9jXmOZPy9g=", cipherText);

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