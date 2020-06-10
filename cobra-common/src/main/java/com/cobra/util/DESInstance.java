package com.cobra.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DESInstance {
    private static String ALGORITHM = "DESede";

    /**
     * 加密
     *
     * @param key
     * @param src
     * @return
     */
    public static byte[] enCode(byte[] key, byte[] src) {

        byte[] value = null;
        SecretKey deskey = new SecretKeySpec(key, ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            value = cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    /**
     * 解密
     *
     * @param key
     * @param src
     * @return
     */
    public static byte[] deCode(byte[] key, byte[] src) {
        byte[] value = null;
        SecretKey deskey = new SecretKeySpec(key, ALGORITHM);

        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, deskey);
            value = cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }
}
