package com.cobra.util.cryto;

import com.cobra.util.StringCommonUtils;

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
    static String ivs = "0000000000000000";// CBC 需要向量

    static String CBC = "CBC";

    public static String encryptFor3DEs(String content, String seed) throws Exception{
        String keyAlg = "DESede";
        String cipherAlg = "DESede/ECB/PKCS5Padding";
        return doEncrypt(keyAlg, 168, null, cipherAlg, Cipher.ENCRYPT_MODE, seed, null, content);
    }

    public static String decryptFor3DEs(String content, String seed) throws Exception{
        String keyAlg = "DESede";
        String cipherAlg = "DESede/ECB/PKCS5Padding";
        return doEncrypt(keyAlg, 168, null, cipherAlg, Cipher.DECRYPT_MODE, seed, null, content);
    }

    /**
     * 1、生成密钥
     * 2、加密
     * 3、BASE64编码
     * @throws Exception
     */
    public static String cipherAESForEncrypt(String content, String seed) throws Exception{
        String keyAlg = "AES";
        String cipherAlg = "AES/CBC/PKCS5Padding";

        return doEncrypt(keyAlg, 128, null, cipherAlg, Cipher.ENCRYPT_MODE, seed, ivs, content);
    }

    /**
     * 1、BASE64解码
     * 2、生成密钥
     * 3、解密
     * @throws Exception
     */
    public static String cipherAESForDecrypt(String content, String seed) throws Exception{
        String keyAlg = "AES";
        String cipherAlg = "AES/CBC/PKCS5Padding";

        return doEncrypt(keyAlg, 128, null, cipherAlg, Cipher.DECRYPT_MODE, seed, ivs, content);

    }

    public static String cipherAESForEncCredit(String content, String seed) throws Exception{

        String keyAlg = "AES";
        String cipherAlg = "AES/CBC/PKCS5Padding";

        return doEncrypt(keyAlg, 128, null, cipherAlg, Cipher.ENCRYPT_MODE, seed, "Xadiapdfaxi0s91D", true, content);

    }

    public static String cipherAESForDecCredit(String content, String seed) throws Exception{
        String keyAlg = "AES";
        String cipherAlg = "AES/CBC/PKCS5Padding";
        String iv = "Xadiapdfaxi0s91D";

        return doEncrypt(keyAlg, 128, null, cipherAlg, Cipher.ENCRYPT_MODE, seed, iv, true, content);

    }

    public static String cipherAESForEncECB(String content, String seed) throws Exception{
        String keyAlg = "AES";
        String cipherAlg = "AES/ECB/PKCS5Padding";
        return doEncrypt(keyAlg, 128, null, cipherAlg, Cipher.ENCRYPT_MODE, seed, null, true, content);

    }

    public static String cipherAESForDecECB(String content, String seed) throws Exception{
        String keyAlg = "AES";
        String cipherAlg = "AES/ECB/PKCS5Padding";
        return doEncrypt(keyAlg, 128, null, cipherAlg, Cipher.DECRYPT_MODE, seed, null, true,content);

    }

    public static String cipherDESForEnc(String content, String seed) throws Exception{
        String keyAlg = "DES";
        String cipherAlg = "DES";
        return doEncrypt(keyAlg, 56, null, cipherAlg, Cipher.ENCRYPT_MODE, seed, null, content);

    }

    public static String cipherDESForDec(String content, String seed) throws Exception{
        String keyAlg = "DES";
        String cipherAlg = "DES";
        return doEncrypt(keyAlg, 56, null, cipherAlg, Cipher.DECRYPT_MODE, seed, null, content);

    }


    static String RSA = "RSA";

    /**
     * X509EncodedKeySpec
     * PKCS8EncodedKeySpec
     * 生成密钥格式不一样
     *
     * @param content
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String cipherRSAPublic(String content, String publicKey) throws Exception{
        Cipher cipher = Cipher.getInstance(RSA);
        //加密
        cipher.init(Cipher.ENCRYPT_MODE, KeyUtils.transRSAKey(Boolean.TRUE, publicKey));
        byte[] bytes = cipher.doFinal(content.getBytes());
        final String encryptText = Base64.getEncoder().encodeToString(bytes);
        return encryptText;

    }

    public static String cipherRSAPrivate(String content, String privateKey) throws Exception{
        //获取cipher对象
        Cipher cipher = Cipher.getInstance(RSA);

        // 解密
        cipher.init(Cipher.DECRYPT_MODE, KeyUtils.transRSAKey(Boolean.FALSE, privateKey));
        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(content));
        return new String(bytes);
    }

    /**
     * 1、密钥生成
     * 2、随机算法
     * 3、加密
     * 4、base64编码
     *
     * @param keyAlg 密钥生成算法
     * @param keyLength 密钥长度
     * @param cipherAlg 加密算法  与密钥算法保持一致
     * @param content 待加密内容
     * @param seed 密钥生成种子
     * @param ivs 密钥生成种子
     * @param mode 模式 加密或解密 Cipher.ENCRYPT_MODE,
     * @return
     * @throws Exception
     */
    private static String doEncrypt(String keyAlg,
                    int keyLength,
                    String randomAlg,
                    String cipherAlg,
                    int mode,
                    String seed,
                    String ivs,
                    String content) throws Exception{
        // 算法名称校验
        if (StringCommonUtils.isEmpty(keyAlg) || !cipherAlg.startsWith(keyAlg)) {
            throw new IllegalArgumentException("加密算法不一致");
        }
        if (cipherAlg.startsWith(CBC)) {
            if (StringCommonUtils.isEmpty(ivs)) {
                throw new IllegalArgumentException("CBC算法 ivs不能为空");
            }
        }
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);

        SecureRandom random = null;
        // 随机算法
        if (StringCommonUtils.isNotEmpty(randomAlg)) {
            random = SecureRandom.getInstance(randomAlg);
            random.setSeed(seed.getBytes());
        } else {
            // SHA1PRNG
            random = new SecureRandom(seed.getBytes());
        }
        keyGenerator.init(keyLength, random);

        Key key = keyGenerator.generateKey();
        Cipher cipher = Cipher.getInstance(cipherAlg);

        // CBC模式需要向量
        if (cipherAlg.contains(CBC)) {
            cipher.init(mode, key, new IvParameterSpec(ivs.getBytes()));
        } else {
            cipher.init(mode, key);
        }

        // BASE64编码
        if (Cipher.ENCRYPT_MODE == mode) {
            // 加密
            byte[] bytes = cipher.doFinal(content.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } else {
            // 解密
            return new String(cipher.doFinal(Base64.getDecoder().decode(content)));
        }

    }
    private static String doEncrypt(String keyAlg,
                    int keyLength,
                    String randomAlgName,
                    String cipherAlg,
                    int mode,
                    String seed,
                    String ivs,
                    boolean overrideKey,// 是否重写key生成方法
                    String content) throws Exception{
        // 算法名称校验
        if (StringCommonUtils.isEmpty(keyAlg) || !cipherAlg.startsWith(keyAlg)) {
            throw new IllegalArgumentException("加密算法不一致");
        }
        if (cipherAlg.startsWith(CBC)) {
            if (StringCommonUtils.isEmpty(ivs)) {
                throw new IllegalArgumentException("CBC算法 ivs不能为空");
            }
        }
        KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);

        SecureRandom random = null;
        // 随机算法
        if (StringCommonUtils.isNotEmpty(randomAlgName)) {
            random = SecureRandom.getInstance(randomAlgName);
            random.setSeed(seed.getBytes());
        } else {
            // SHA1PRNG
            random = new SecureRandom(seed.getBytes());
        }
        keyGenerator.init(keyLength, random);

        Key key = keyGenerator.generateKey();
        if (overrideKey) {
            // 指定key
            key = new SecretKeySpec(seed.getBytes(), keyAlg);
        }
        Cipher cipher = Cipher.getInstance(cipherAlg);

        // CBC模式需要向量
        if (cipherAlg.contains(CBC)) {
            cipher.init(mode, key, new IvParameterSpec(ivs.getBytes()));
        } else {
            cipher.init(mode, key);
        }

        // BASE64编码
        if (Cipher.ENCRYPT_MODE == mode) {
            // 加密
            byte[] bytes = cipher.doFinal(content.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } else {
            // 解密
            return new String(cipher.doFinal(Base64.getDecoder().decode(content)));
        }

    }




}
