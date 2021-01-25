package com.cobra.util.cryto;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Locale;

/**
 * 自定义AES加密
 */
@Slf4j
public class AesNoPaddingUtils
{
    public static void main(String[] args) throws Exception{

        String src = "12345678901234567890";
        String key = "1234567890123456";
        String ivs = "0000000000000000";
        String pwd = AesNoPaddingUtils.encrypt(src, key, ivs);
        System.out.println("pwd:" + pwd);

        String unpwd = AesNoPaddingUtils.decrypt(pwd, key, ivs);
        System.out.println("unpwd:" + unpwd);

        System.out.println(Arrays.toString(src.toCharArray()));
        System.out.println(Arrays.toString(unpwd.trim().toCharArray()));

    }

    /**
     *
     * @param sSrc 待加密的文件
     * @param sKey 加密的key 16位
     * @param ivs 初始向量 默认16个0
     * @return 加密后的值
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey, String ivs) throws Exception
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            byte[] plaintext = padding(sSrc, cipher);
            SecretKeySpec keyspec = new SecretKeySpec(sKey.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            return Base64.encodeBase64String(encrypted);
        }
        catch (Exception e)
        {
            log.error("AES加密失败:{}", e);
            return null;
        }
    }

    private static byte[] padding(String sSrc, Cipher cipher){
        int blockSize = cipher.getBlockSize();
        byte[] dataBytes = sSrc.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0)
        {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        return plaintext;
    }
    private static String padding(String sSrc, int blockSize){
        byte[] dataBytes = sSrc.getBytes();
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0)
        {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        return new String(plaintext);
    }

    /**
     * 解密 非填充会造成末尾空格
     * @param sSrc 加密的文件
     * @param sKey 解密的key 16位 与加密的参数一致
     * @param ivs 初始向量 与加密参数保持一致
     * @return 解密后的文件
     */
    public static String decrypt(String sSrc, String sKey, String ivs)
    {
        try
        {
            if (sKey == null)
            {
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes("utf-8"));//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted = Base64.decodeBase64(sSrc);//先用base64解密
            byte[] original = cipher.doFinal(encrypted);
            String originalString = new String(original);
            return originalString.trim();
        }
        catch (Exception ex)
        {
            log.error("AES解密失败:{}", ex);
            return null;
        }
    }



}



