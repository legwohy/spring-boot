package com.cobra.util.cryto;

import java.security.*;
import java.util.Base64;

/**
 * signature类用于提供数字签名，用于保证数据的完整性
 *
 * <p>
 *      NONEwithRSA、MD2withRSA、MD5withRSA、SHA1withRSA、SHA224withRSA、SHA256withRSA、SHA384withRSA
 *      SHA512withRSA、NONEwithDSA、SHA1withDSA、SHA224withDSA、SHA256withDSA、NONEwithECDSA、SHA1withECDSA、
 *      SHA224withECDSA、SHA256withECDSA、SHA384withECDSA、SHA512withECDSA
 * </p>

 * @author admin
 * @date 2020/12/30 17:37
 * @desc
 */
public class SignatureUtils {
    static String alg = "NONEwithRSA";

    public static String signature(String content, PrivateKey privateKey) throws Exception{
        //用私钥初始化signature
        Signature signature = Signature.getInstance(alg);
        signature.initSign(privateKey);
        //更新原始字符串
        signature.update(content.getBytes());
        byte[] bytes = signature.sign();
        String sign = Base64.getEncoder().encodeToString(bytes);
        return sign;

    }

    /**
     * 验签
     * @param content
     * @param sign
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static boolean verify(String content, String sign, PublicKey publicKey) throws Exception{
        Signature signature = Signature.getInstance(alg);
        //用公钥初始化signature
        signature.initVerify(publicKey);
        //校验签名是否正确
        signature.update(content.getBytes());

        return signature.verify(Base64.getDecoder().decode(sign));

    }

}
