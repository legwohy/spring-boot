package com.cobra.util.cryto;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

/**
 *
 *  <p>
 *      Cipher提供如下参数:
 *
 *      AES/CBC/NoPadding (128)、AES/CBC/PKCS5Padding (128)、AES/ECB/NoPadding (128)、
 *      AES/ECB/PKCS5Padding (128)、DES/CBC/NoPadding (56)、DES/CBC/PKCS5Padding (56)、
 *      DES/ECB/NoPadding (56)、DES/ECB/PKCS5Padding (56)、DESede/CBC/NoPadding (168)、
 *      DESede/CBC/PKCS5Padding (168)、DESede/ECB/NoPadding (168)、DESede/ECB/PKCS5Padding (168)、
 *      RSA/ECB/PKCS1Padding (1024, 2048)、RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)、
 *      RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)
 *  </p>
 *
 * <a link=https://www.cnblogs.com/caizhaokai/p/10944667.html/>
 *  <p>
 *
 *      算法：AES、DES、DESede(DES3)、RSA
 *      模式：CBC(有向量) ECB(无向量)
 *      填充模式：
 *          NoPadding 加密内容不足8位用0补足8位，需要自己实现代码给加密内容添加0 如{65,65,65,0,0,0,0,0}
 *          PKCS5Padding 加密内容不足8位用余数补足8位 如{65,65,65,5,5,5,5,5}或{97,97,97,97,97,97,2,2}; 刚好8位补8位8
 *  </p>
 *
 * <p>
 *
 *     @see Cipher#init(int, Key, AlgorithmParameterSpec, SecureRandom)
 *     @see Cipher#init(int, Key, AlgorithmParameterSpec)
 *
 *    opmode ：Cipher.ENCRYPT_MODE(加密模式)和 Cipher.DECRYPT_MODE(解密模式)
 *    key ：密匙，使用传入的盐构造出一个密匙，可以使用SecretKeySpec、KeyGenerator和KeyPairGenerator创建密匙，其中
 *      SecretKeySpec和KeyGenerator支持AES，DES，DESede三种加密算法创建密匙
 *      KeyPairGenerator支持RSA加密算法创建密匙
 *    AlgorithmParameters ：使用CBC模式时必须传入该参数，该项目使用IvParameterSpec创建iv 对象
 *
 * </p>
 *
 * 创建密钥
 * <p>
 *      SecretKeySpec 支持 AES(16)、DES(8)、DES3(24)  密钥长度和向量长度 byte[] 的长度
 *      KeyGenerator 支持 AES、DES、DES3 初始化需要传入随机源 一般使用 SecureRandom构造随机数
 *      KeyPairGenerator 支持RSA
 * </p>
 *
 * <p>
 *     byte[] b = cipher.doFinal(content);返回结果为byte数组，
 *     如果直接使用 new String(b) 封装成字符串，则会出现乱码
 *     BASE64编码或HEX编码
 * </p>
 * tip:创建密钥，若使用盐作为种子生成随机数，需要保存盐，不使用盐，需要自行保存密钥文件
 *
 * @author admin
 * @date 2020/12/30 17:34
 * @desc
 */
public class CipherUtils {
    static String AES_CBC = "AES/CBC/PKCS5Padding";
    static String AES_EBC = "AES/ECB/PKCS5Padding";
    static String ivs = "0000000000000000";// CBC需要

    /**
     * 1、生成密钥
     * 2、加密
     * 3、BASE64编码
     * @throws Exception
     */
    public static String cipherAESForEncrypt(String content, String seed) throws Exception{
        //指定使用AES加密
        //使用KeyGenerator生成key，参数与获取cipher对象的algorithm必须相同
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //指定生成的密钥长度为128
        keyGenerator.init(128, new SecureRandom(seed.getBytes()));
        Key key = keyGenerator.generateKey();
        System.out.println("密钥长度是16位:"+(key.getEncoded().length==16));
        // TODO SecretKeySpec 限制字节数组的长度
        //key = new SecretKeySpec(seed.getBytes(),"AES");
        Cipher cipher = Cipher.getInstance(AES_CBC);
        cipher.init(Cipher.ENCRYPT_MODE,
                        key,
                        new IvParameterSpec(ivs.getBytes()));
        byte[] bytes = cipher.doFinal(content.getBytes());

        return Base64.getEncoder().encodeToString(bytes);

    }



    /**
     * 1、BASE64解码
     * 2、生成密钥
     * 3、解密
     * @throws Exception
     */
    public static String cipherAESForDecrypt(String content, String seed) throws Exception{
        //指定使用AES加密
        Cipher cipher = Cipher.getInstance(AES_CBC);
        //使用KeyGenerator生成key，参数与获取cipher对象的algorithm必须相同
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, new SecureRandom(seed.getBytes()));
        Key key = keyGenerator.generateKey();
        // TODO 这种密钥会限制种子长度
        //key = new SecretKeySpec(seed.getBytes(),"AES");

        cipher.init(Cipher.DECRYPT_MODE,
                        key,
                        new IvParameterSpec(ivs.getBytes()));

        // 解密
        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(content));

        return new String(bytes);
    }

    public static String cipherAESForEncEBC(String content, String seed) throws Exception{
        //指定使用AES加密
        //使用KeyGenerator生成key，参数与获取cipher对象的algorithm必须相同
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //指定生成的密钥长度为128
        keyGenerator.init(128, new SecureRandom(seed.getBytes()));
        Key key = keyGenerator.generateKey();

        Cipher cipher = Cipher.getInstance(AES_EBC);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(content.getBytes());

        return Base64.getEncoder().encodeToString(bytes);

    }
    public static String cipherAESForDecEBC(String content, String seed) throws Exception{
        //指定使用AES加密
        Cipher cipher = Cipher.getInstance(AES_EBC);
        //使用KeyGenerator生成key，参数与获取cipher对象的algorithm必须相同
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, new SecureRandom(seed.getBytes()));
        Key key = keyGenerator.generateKey();

        cipher.init(Cipher.DECRYPT_MODE, key);

        // 解密
        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(content));

        return new String(bytes);
    }

    public static String cipherDESForEnc(String content, String seed) throws Exception{
        //指定使用DES加密
        Cipher cipher = Cipher.getInstance("DES");
        //使用KeyGenerator生成key，参数与获取cipher对象的algorithm必须相同
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        //DES的秘钥长度必须是56位
        keyGenerator.init(56, new SecureRandom(seed.getBytes()));
        Key key = keyGenerator.generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(content.getBytes());

        return Base64.getEncoder().encodeToString(bytes);

    }

    public static String cipherDESForDec(String content, String seed) throws Exception{
        //指定使用DES加密
        Cipher cipher = Cipher.getInstance("DES");
        //使用KeyGenerator生成key，参数与获取cipher对象的algorithm必须相同
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        //DES的秘钥长度必须是56位
        keyGenerator.init(56, new SecureRandom(seed.getBytes()));
        Key key = keyGenerator.generateKey();
        //与AES不同，由于DES并不需要初始向量，因此解密的时候不需要第三个参数
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(content));
        return new String(bytes);
    }

    public static String cipherRSAEnc(String content,PublicKey publicKey) throws Exception{

        Cipher cipher = Cipher.getInstance("RSA");
        //加密
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content.getBytes());
        final String encryptText = Base64.getEncoder().encodeToString(bytes);
        return encryptText;

    }
    public static String cipherRSADec(String content,PrivateKey privateKey) throws Exception{
        //获取cipher对象
        Cipher cipher = Cipher.getInstance("RSA");

        // 解密
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(content));
        return new String(bytes);
    }

}
