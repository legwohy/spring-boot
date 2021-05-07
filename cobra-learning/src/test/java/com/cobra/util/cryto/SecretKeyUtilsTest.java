package com.cobra.util.cryto;

import com.cobra.util.cryto.enums.AlgEnums;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author admin
 * @date 2020/12/31 13:38
 * @desc
 */
public class SecretKeyUtilsTest {


    @Test
    public void generateKey() throws Exception{
        String seed = "1234";
        Assert.assertEquals("MTIzNDU2",SecretKeyUtils.generateKey(AlgEnums.AES.getCode(),seed,"0",AlgEnums.AES.getNation()));
    }

    /**
     * 生成key.store(包含私钥和证书)  keytool -genkey -alias test -keyalg RSA -keystore c:/key.store
     * 生成证书(从keyStore中导出证书)  keytool -export -alias test -keystore c:/key.store -file c:/key.cert
     */
    static String keyStorePath = "/kft/key.store";
    static String cerPath = "/kft/key.cert";
    static String alias = "test";
    static String password = "123456";
    @Test
    public void test()throws Exception{
        String priKey = Base64.encodeBase64String(SecretKeyUtils.getPrivateKeyFromKeyStore("JKS",keyStorePath,password.toCharArray(),alias,password.toCharArray()).getEncoded());

        System.out.println("priKey:"+priKey);

        String pubKey = Base64.encodeBase64String(SecretKeyUtils.getCertificateFromKeyStore("JKS",keyStorePath,password.toCharArray(),alias).getPublicKey().getEncoded());
        String pubKey2 = Base64.encodeBase64String(SecretKeyUtils.getCertificateFromPath(cerPath).getPublicKey().getEncoded());
        System.out.println("pubKey:"+pubKey);
        System.out.println("result:"+pubKey.equals(pubKey2));
    }


}