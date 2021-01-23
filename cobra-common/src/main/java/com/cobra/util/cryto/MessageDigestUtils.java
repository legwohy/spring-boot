package com.cobra.util.cryto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 *     MessageDigest主要是做hash变换(也称消息摘要或者散列值)
 *      MD5,MD2,MD5,SHA-1,SHA-224,SHA-256,SHA-384,SHA-512
 * </p>
 *
 * @author admin
 * @date 2020/12/30 17:38
 * @desc
 */
public class MessageDigestUtils {
    public final static String MD5 = "MD5";
    public final static String SHA256 = "SHA-256";
    public final static String SHA512 = "SHA-512";


    public static String md5(String content) {
        return messageDigest(content, MD5);

    }

    public static String SHA256(String content) {
        return messageDigest(content, SHA256);

    }

    public static String SHA512(String content) {
        return messageDigest(content, SHA512);

    }

    public static String messageDigest(String content, String alg) {
        //参数可以是 MD5,MD2,MD5,SHA-1,SHA-224,SHA-256,SHA-384,SHA-512
        MessageDigest messageDigest = null;
        try
        {
            messageDigest = MessageDigest.getInstance(alg);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            throw new RuntimeException("不支持算法,"+alg);
        }
        byte[] bytes = messageDigest.digest(content.getBytes());

        //将二进制数组转成16进制字符串输出
        return HexUtil.toHexString(bytes);

    }

}
