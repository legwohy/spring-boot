package com.cobra.util.digest;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;


/**
 * Created by leigang on 2019/12/3.
 */
public class SHA {
    public static final String STR = "<if test=\"outTradeNo != null and outTradeNo!=''\">\n" +
            "      and out_trade_no=#{outTradeNo,jdbcType=VARCHAR}\n" +
            "    </if>";

}

/**
 * use jre MessageDigest , BouncyCastle for SHA-224.
 *
 */
class SHACoder{

    public static byte[] encodeSHA1(byte[] data) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return md.digest(data);
    }

    public static byte[] encodeSHA256(byte[] data) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(data);
    }

    public static byte[] encodeSHA384(byte[] data) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        return md.digest(data);
    }

    public static byte[] encodeSHA512(byte[] data) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        return md.digest(data);
    }

    public static byte[] encodeSHA224(byte[] data) throws Exception{
        //Security.addProvider(new BouncyCastleProvider());
        MessageDigest md = MessageDigest.getInstance("SHA-224");
        return md.digest(data);
    }

    public static String encodeSHA1Hex(byte[] data) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return new String(Hex.encodeHexString(md.digest(data)));
    }

}

/**
 * use Apache commons codec, doesn't support SHA-224.
 *
 */
class ApacheSHACoder{

    public static byte[] encodeSHA1(String data)throws Exception{
        return DigestUtils.sha1(data);
    }

    public static byte[] encodeSHA256(String data)throws Exception{
        return DigestUtils.sha256(data);
    }

    public static byte[] encodeSHA384(String data)throws Exception{
        return DigestUtils.sha384(data);
    }

    public static byte[] encodeSHA512(String data)throws Exception{
        return DigestUtils.sha512(data);
    }

    public static String encodeSHA1Hex(String data)throws Exception{
        return DigestUtils.sha1Hex(data);
    }

    public static String encodeSHA256Hex(String data)throws Exception{
        return DigestUtils.sha256Hex(data);
    }

    public static String encodeSHA384Hex(String data)throws Exception{
        return DigestUtils.sha384Hex(data);
    }

    public static String encodeSHA512Hex(String data)throws Exception{
        return DigestUtils.sha512Hex(data);
    }
}
