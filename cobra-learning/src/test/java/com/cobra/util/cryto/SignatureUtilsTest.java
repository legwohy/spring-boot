package com.cobra.util.cryto;

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

}