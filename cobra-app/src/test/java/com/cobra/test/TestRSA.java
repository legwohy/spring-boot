package com.cobra.test;

import com.cobra.constants.TokenConstant;
import com.cobra.util.FileUtils;
import com.cobra.util.rsa.RSAEncrypt;
import com.cobra.util.rsa.RSASignature;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by legwo on 2018/7/4.
 */
public class TestRSA {
    private String plainText = "hello 你好";
    @Test public void test() throws Exception {
        // 私钥签名
        String privateKey = RSAEncrypt.loadPrivateKeyByFile(FileUtils.getRootPath()+"/keys");
        String sign = RSASignature.sign(plainText,privateKey);

        // 公钥验签
        String publicKey = RSAEncrypt.loadPublicKeyByFile(FileUtils.getRootPath()+"/keys");
        boolean flag = RSASignature.doCheck(plainText,sign,publicKey);

        Assert.assertTrue(flag);
    }
}
