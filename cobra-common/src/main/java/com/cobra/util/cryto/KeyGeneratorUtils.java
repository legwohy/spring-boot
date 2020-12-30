package com.cobra.util.cryto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Base64;

/**
 * 用于生成秘钥， KeyGenerator.getInstance(String algorithm)支持的参数有
 *
 * AES (128)
 DES (56)
 DESede (168)
 HmacSHA1
 HmacSHA256
 *
 * @author admin
 * @date 2020/12/30 17:38
 * @desc
 */
public class KeyGeneratorUtils {

    public void testKeyGenerator() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //初始化方法有多种，根据需要选择
        keyGenerator.init(128);
        //      keyGenerator.init(new SecureRandom("1234567".getBytes()));

        SecretKey key = keyGenerator.generateKey();
        //key的二进制编码   将它保存到文件中

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] bytes = cipher.doFinal("helloworld".getBytes());

        System.out.println("加密数据: " + Base64.getEncoder().encodeToString(bytes));

    /*=========保存key的二进制编码=========*/
        byte[] keyBytes = key.getEncoded();
        FileOutputStream fos = new FileOutputStream("F://test/key.txt");
        fos.write(keyBytes);
        fos.flush();
        fos.close();


    /*============从文件中读取编码并恢复key==============*/
        FileInputStream fis = new FileInputStream("F://test/key.txt");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[1024];
        while ((len = fis.read(buffer)) > 0) {
            bos.write(buffer, 0, len);
        }
        fis.close();

    /*==============使用SecretKeySpec重新生成key============*/
        SecretKeySpec secretKeySpec = new SecretKeySpec(bos.toByteArray(), "AES");

        cipher.init(Cipher.DECRYPT_MODE,
                        secretKeySpec,
                        cipher.getParameters().getParameterSpec(IvParameterSpec.class));
        bytes = cipher.doFinal(bytes);
        System.out.println("解密数据: " + new String(bytes));
    }




}
