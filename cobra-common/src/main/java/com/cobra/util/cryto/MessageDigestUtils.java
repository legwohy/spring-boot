package com.cobra.util.cryto;


import com.cobra.util.HexUtil;

import java.security.MessageDigest;

/**
 *
 * MessageDigest主要是做hash变换(也称消息摘要或者散列值)
 *
 * @author admin
 * @date 2020/12/30 17:38
 * @desc
 */
public class MessageDigestUtils {

    public void testMessageDigest() throws Exception {
        //参数可以是 MD5,MD2,MD5,SHA-1,SHA-224,SHA-256,SHA-384,SHA-512
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] bytes = messageDigest.digest("helloworld".getBytes());
        //将二进制数组转成16进制字符串输出
        System.out.println("MD5哈希变换：" + HexUtil.toHexString(bytes));


        messageDigest = MessageDigest.getInstance("SHA-1");
        bytes = messageDigest.digest("helloworld".getBytes());
        System.out.println("SHA1哈希变换：" + HexUtil.toHexString(bytes));
    }






}
