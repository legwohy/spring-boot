package com.cobra.util.cryto;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author admin
 * @date 2020/12/31 10:27
 * @desc
 */
public class SignatureUtilsTest {
    PublicKey publicKey = null;
    PrivateKey privateKey = null;

    @Before
    public void setup() throws Exception{
        //KeyPairGenerator生成公钥和私钥
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        System.out.println("公钥");
        System.out.println(Base64.encodeBase64String(publicKey.getEncoded()));
        System.out.println("私钥");
        System.out.println(Base64.encodeBase64String(privateKey.getEncoded()));

    }

    /**
     * 私钥签名
     * @throws Exception
     */
    @Test
    public void test() throws Exception{
        String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJVP6ktzWeTP3PzTdI6YXjxswWfQZgXieDqfGPSiNR67gp40qSTD+d+6t5O0vvuP1GkyOq4nPMp05l8dG9DK4BtouhpIPW5Tjuek2IAvTlsthkShla8U5qv2A1sh6x5rwxfQN5idwDZOSveRSjwrpcGtn5NxXZDw0S+bVPnfos4bAgMBAAECgYAgiwZ5425YXKXBxSTGX5dKfDm7MsTJy46A2GRMDX6ecCgL61ZiWQSBKuUQIHjvw218cCIyCwNI7Sm42VNSsqUdfYHZc5OLfhyRKJMumXDsp+TmBGRq1rWTxjaIPz5ayjYlpdgSAhmR6z6GXbkuqjVp+RuN9NjbzB1Tssgbj7nwwQJBAOu0g363QykQXuRWjdp9xn7SyirZH2UuRrHRI33Y7MgRWjJ0aVyFy93NUWhvIlL9egNI0W98pnnDbyCw1BKYYYkCQQCiKxnO9F7lPfSxziqYf1Hs+HrxR8tY04Vi+x3NGh1pwMw9qz5FQBQu9aB94vrQ6d8rfo/RcDW4fDo0UXLegX2DAkEAt9uSKtjnEXQMoQ6HDmTH3gus2WmOHM3RJUle5v68DH/EnNDDkwFm+ff7RXxTCOtiLoexnsZb0WVKHPTyKCMnCQJAO0NhBR6M513C5I+hRWWR/PW5iB9ikq2KG2gFtgSLJzCL8yV3djs6pQaihh7C5kHxiqWPzUwida8AXko9nk7YHQJABdHSS9ryht2s+dCoPS/ePzFQr6hkjX0yE1bLvd9Gqjb6eNLUl8KlbaUItNi0rn64gJtY22+fk62r+DxEpwtTFg==";
        PrivateKey privateKey = (PrivateKey)SecretKeyUtils.transRSAKey(false, priKey);
        System.out.println(privateKey.getAlgorithm());
    }

    @Test
    public void signature() throws Exception{
        String content = "123456ABC";
        // 签名
        String sign = SignatureUtils.signature(content, privateKey);
        Assert.assertNotNull(sign);

        // 验签
        Assert.assertTrue(SignatureUtils.verify(content, sign, publicKey));
    }

    @Test
    public void testCert()throws Exception{
        String srcPlainText = "123456ABC";
        String sign = SignatureUtils.signatureByKeyStore(srcPlainText);
        Assert.assertNotNull(sign);
        Boolean certResult = SignatureUtils.verifySignByCertPath("/kft/key.cert",srcPlainText,sign);
        Boolean storeResult = SignatureUtils.verifySignByKeyStore(srcPlainText,sign);

        Assert.assertTrue(certResult);
        Assert.assertTrue(storeResult);
    }

}