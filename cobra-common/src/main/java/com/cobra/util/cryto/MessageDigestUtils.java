package com.cobra.util.cryto;

import com.cobra.util.HexUtil;
import com.cobra.util.digest.MD5;

import java.security.MessageDigest;

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

    public static void main(String[] args)throws Exception{
        String src = "{\"privateKey\":\"私钥\",\"name\":\"小王\",\"signType\":\"SHA-256\"}";
        System.out.println(SHA256(src));
    }

    public static String md5(String content) throws Exception{
        return messageDigest(content, MD5);

    }

    public static String SHA256(String content) throws Exception{
        return messageDigest(content, SHA256);

    }

    public static String SHA512(String content) throws Exception{
        return messageDigest(content, SHA512);

    }

    public static String messageDigest(String content, String alg) throws Exception{
        //参数可以是 MD5,MD2,MD5,SHA-1,SHA-224,SHA-256,SHA-384,SHA-512
        MessageDigest messageDigest = MessageDigest.getInstance(alg);
        byte[] bytes = messageDigest.digest(content.getBytes());

        //将二进制数组转成16进制字符串输出
        return HexUtil.toHexString(bytes);

    }

}
