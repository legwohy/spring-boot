package com.cobra.util;

import org.apache.commons.codec.binary.Hex;

/**
 * 十六进制 0-9 A-F
 * 1、将八位拆分两组，一组四位
 * 2、每组前面补四个0(高位补0)
 * 3、将补齐后的八位查表
 */
public class HexUtil {

    /**
     * 编码
     * @param src
     * @return
     */
    public static String encode(String src){
        return Hex.encodeHexString(src.getBytes());
    }

    public static String decode(String cipher)throws Exception{
        return new String(Hex.decodeHex(cipher.toCharArray()));
    }

    public static String toHexString(byte[] data) {
        StringBuilder builder = new StringBuilder();
        int len = data.length;
        String hex;
        for (int i = 0; i < len; i++) {
            hex = Integer.toHexString(data[i] & 0xFF);
            if (hex.length() == 1) {
                builder.append("0");
            }
            builder.append(hex);
        }
        return builder.toString();
    }

}
