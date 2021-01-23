package com.cobra.util.cryto;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

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

    public static String decode(String cipher) throws Exception{
        return new String(Hex.decodeHex(cipher.toCharArray()));
    }

    public static String toHexString(byte[] data){
        return Hex.encodeHexString(data);
    }

    /**
     *
     * @param data
     * @return
     */
    public static byte[] toHexByte(String data){
        return getBytes(Hex.encodeHex(data.getBytes()));
    }

    private static byte[] getBytes(char[] chars){
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }

    private static char[] getChars(byte[] bytes){
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);
        return cb.array();
    }

}
