package com.cobra.util;

import org.apache.commons.codec.binary.Base64;

/**
 * base64
 * 1、三个字符为一组，不足三个字符的的用0补齐，组成24位二进制
 * 2、将24位二进制拆分为四组，每组6位二进制
 * 3、每组6位前面2位补0，构成八位，八位二进制查表
 * 4、体积会超三分之一(多了一个字符 八位二进制)
 */
public class Base64Util
{
    public static void main(String[] args)
    {
        String src = "base";
        String cipher = Base64Util.encode(src);
        System.out.println("编码:"+cipher);
        System.out.println("解码:"+Base64Util.decode(cipher));

    }

    /**
     * 编码
     * @param src
     * @return
     */
    public static String encode(String src){
        return new String(Base64.encodeBase64(src.getBytes()));
    }

    /**
     * 解码
     * @param cipher
     * @return
     */
    public static String decode(String cipher){
        return new String(Base64.decodeBase64(cipher));
    }
}
