package com.cobra.util.rsa;

import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 * @date 2020/12/28 15:14
 * @desc
 */
public class SHA256WithRSATestUtil {
    String PLAIN_TEXT = "test string RSA";
    Map<String, Object> keyMap = new HashMap<>();
    Map<String, Object> sKeyMap = new HashMap<>();// 加密后的密钥

    @Before
    public void createKey() throws Exception{
        keyMap = SHA256withRSAUtil.createKey("RSA", 2048);
    }


    @Test
    public void createEncryKeyStr() throws Exception{
        System.out.println("----------------------------------------------====");
        System.out.println(sKeyMap);
        sKeyMap = SHA256withRSAUtil.createEncryKeyStr("RSA", 2048,keyMap.get("privateKey").toString());
        System.out.println("----------------------------------------------");
        System.out.println(sKeyMap);
    }

    @Test
    public void sign() throws Exception{
        PublicKey publicKey = (PublicKey)keyMap.get("publicKey");
        PrivateKey privateKey = (PrivateKey)keyMap.get("privateKey");
        byte[] signBytes = SHA256withRSAUtil.sign(privateKey, PLAIN_TEXT);
        System.out.println(SHA256withRSAUtil.verifySign(publicKey, PLAIN_TEXT, signBytes));
    }

    @Test
    public void bytesToHexString() throws Exception{
    }



}