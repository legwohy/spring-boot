package com.cobra.util.cryto;

import java.security.*;
import java.util.Base64;

/**
 * signature类用于提供数字签名，用于保证数据的完整性
 * NONEwithRSA
 MD2withRSA
 MD5withRSA
 SHA1withRSA
 SHA224withRSA
 SHA256withRSA
 SHA384withRSA
 SHA512withRSA
 NONEwithDSA
 SHA1withDSA
 SHA224withDSA
 SHA256withDSA
 NONEwithECDSA
 SHA1withECDSA
 SHA224withECDSA
 SHA256withECDSA
 SHA384withECDSA
 SHA512withECDSA

 * @author admin
 * @date 2020/12/30 17:37
 * @desc
 */
public class SignatureUtils {
    static String TEXT = "aa";
    Signature signature = null;
    PublicKey publicKey = null;
    PrivateKey privateKey = null;

    public SignatureUtils() throws Exception{
        signature = Signature.getInstance("NONEwithRSA");

        //KeyPairGenerator生成公钥和私钥
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

    }

    public String signature(String content) throws Exception{

        //用私钥初始化signature
        signature.initSign(privateKey);
        //更新原始字符串
        signature.update(content.getBytes());
        byte[] bytes = signature.sign();
        String sign = Base64.getEncoder().encodeToString(bytes);
        return sign;

    }

    public boolean verify(String sign) throws Exception{

        //用公钥初始化signature
        signature.initVerify(publicKey);
        //更新原始字符串
        signature.update(TEXT.getBytes());
        //校验签名是否正确
        return signature.verify(Base64.getDecoder().decode(sign));

    }

}
