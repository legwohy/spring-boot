package com.cobra.util.cryto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;
import java.util.Base64;

/**
 *
 *  *  //括号数值为所需秘钥的长度
 *  <p>
 *       AES/CBC/NoPadding (128)
 AES/CBC/PKCS5Padding (128)
 AES/ECB/NoPadding (128)
 AES/ECB/PKCS5Padding (128)
 DES/CBC/NoPadding (56)
 DES/CBC/PKCS5Padding (56)
 DES/ECB/NoPadding (56)
 DES/ECB/PKCS5Padding (56)
 DESede/CBC/NoPadding (168)
 DESede/CBC/PKCS5Padding (168)
 DESede/ECB/NoPadding (168)
 DESede/ECB/PKCS5Padding (168)
 RSA/ECB/PKCS1Padding (1024, 2048)
 RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
 RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)

 CBC模式下需要初始向量数组
 *  </p>

 *
 * @author admin
 * @date 2020/12/30 17:34
 * @desc
 */
public class CipherUtils {

    /**
     * 1、生成密钥
     * 2、初始化加密类Cipher
     * @throws Exception
     */
    public void cipherAESForEncrypt(String content) throws Exception{
        //指定使用AES加密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //使用KeyGenerator生成key，参数与获取cipher对象的algorithm必须相同
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //指定生成的密钥长度为128
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(content.getBytes());
        System.out.println("AES加密： " + Base64.getEncoder().encodeToString(bytes));

    }

    public void testCipherAES() throws Exception{
        //指定使用AES加密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //使用KeyGenerator生成key，参数与获取cipher对象的algorithm必须相同
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //指定生成的密钥长度为128
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = cipher.doFinal("helloworld".getBytes());
        System.out.println("AES加密： " + Base64.getEncoder().encodeToString(bytes));

        //由于AES加密在CBC模式下是需要有一个初始向量数组byte[] initializeVector ,
        // 而解密的时候也需要同样的初始向量，因此需要使用加密时的参数初始化解密的cipher，否则会出错
        byte[] initializeVector = cipher.getIV();
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initializeVector);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        //上面三步操作可以用此操作代替   cipher.init(Cipher.DECRYPT_MODE, key, cipher.getParameters());
        bytes = cipher.doFinal(bytes);
        System.out.println("AES解密： " + new String(bytes));
    }

    public void testCipherDES() throws Exception{
        //指定使用DES加密
        Cipher cipher = Cipher.getInstance("DES");
        //使用KeyGenerator生成key，参数与获取cipher对象的algorithm必须相同
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        //DES的秘钥长度必须是56位
        keyGenerator.init(56);
        Key key = keyGenerator.generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = cipher.doFinal("helloworld".getBytes());
        System.out.println("DES加密： " + Base64.getEncoder().encodeToString(bytes));
        //与AES不同，由于DES并不需要初始向量，因此解密的时候不需要第三个参数
        cipher.init(Cipher.DECRYPT_MODE, key);
        bytes = cipher.doFinal(bytes);
        System.out.println("DES解密： " + new String(bytes));
    }

    public void testCipherRSA() throws Exception{
        //获取cipher对象
        Cipher cipher = Cipher.getInstance("RSA");
        //通过KeyPairGenerator来生成公钥和私钥
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();//公钥
        PrivateKey privateKey = keyPair.getPrivate();//私钥

    /*加密*/
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal("te".getBytes());
        final String encryptText = Base64.getEncoder().encodeToString(bytes);
        System.out.println("RSA公钥加密：" + encryptText);

    /*解密*/
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        bytes = cipher.doFinal(Base64.getDecoder().decode(encryptText));
        System.out.println("RSA解密：" + new String(bytes));
    }

}
