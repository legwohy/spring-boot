package com.cobra.util.cryto;

import com.cobra.util.StringCommonUtils;
import com.cobra.util.cryto.enums.AlgEnums;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * <p>
 *      用于生成秘钥， KeyGenerator.getInstance(String algorithm)支持的参数有
 *      AES (128)、 DES (56)、 DESede (168)、HmacSHA1、 HmacSHA256
 * </p>
 *
 * <p>
 *      单向加密(MD5、SHA、HMAC)、对称加密(AES、DES)、非对称加密(RSA、DSA)
 *
 *      keyGenerator：秘钥生成器，也就是更具算法类型随机生成一个秘钥，例如HMAC，所以这个大部分用在非可逆的算法中
 *      SecretKeyFactory：秘密秘钥工厂，言外之意就是需要根据一个秘密（password）去生成一个秘钥,例如DES，PBE，所以大部分使用在对称加密中
 *      KeyPairGenerator:秘钥对生成器，也就是可以生成一对秘钥，也就是公钥和私钥，所以大部分使用在非对称加密中
 * </p>
 *
 * @author admin
 * @date 2020/12/30 17:38
 * @desc
 */
public class SecretKeyUtils {

    public static String PUBLIC_KEY = "PUBLIC_KEY";
    public static String PRIVATE_KEY = "PRIVATE_KEY";
    public static String curveName = "sm2p256v1";// wapip192v1

    public void writeKeyToFile(String filePath, String content) throws Exception{
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(content.getBytes());
        fos.flush();
        fos.close();
    }

    public static String readKeyFromFile(String filePath) throws Exception{
        FileInputStream fis = new FileInputStream(filePath);
        byte[] bytes = new byte[1024];

        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        int index = -1;
        while ((index = fis.read(bytes)) != -1) {
            bas.write(bytes, 0, index);
        }
        bas.flush();
        bas.close();

        return bas.toString();
    }

    /**
     * 生成单个key
     * @param alg
     * @param seed
     * @return
     * @throws Exception
     */
    public static String generateSingleKey(String alg, String seed) throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance(alg);
        //初始化方法有多种，根据需要选择
        keyGenerator.init(128);
        keyGenerator.init(new SecureRandom(seed.getBytes()));

        Key key = keyGenerator.generateKey();
        return org.apache.commons.codec.binary.Base64.encodeBase64String(key.getEncoded());
    }
    final static String SEED_IS_KEY = "1";

    /**
     * 组装key
     * @see AlgEnums
     * @param keyAlg 随机算法
     * @param seed
     * @param seedIsKey
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Key generateKey(String keyAlg, String randomAlgName, String seed, String seedIsKey) throws NoSuchAlgorithmException
    {
        Key key = null;
        if(SEED_IS_KEY.equals(seedIsKey)){
            key = new SecretKeySpec(seed.getBytes(), keyAlg);
        }else {

            KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);

            SecureRandom random = null;
            // 随机算法
            if (StringCommonUtils.isNotEmpty(randomAlgName)) {
                random = SecureRandom.getInstance(randomAlgName);
                random.setSeed(seed.getBytes());
            } else {
                // TODO 坑 jdk高于8的默认不是 SHA1PRNG 算法 建议 指定算法
                random = new SecureRandom(seed.getBytes());
            }

            keyGenerator.init(AlgEnums.getLength(keyAlg), random);
            key = keyGenerator.generateKey();
        }
        return key;
    }


    public void testKeyGenerator() throws Exception{
        // 保存密钥
        String key = generateSingleKey("AES", "123456");
        writeKeyToFile("F://test/key.txt", key);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,
                        new SecretKeySpec(org.apache.commons.codec.binary.Base64.decodeBase64(key), "AES"));

        byte[] bytes = cipher.doFinal("helloworld".getBytes());

    /*==============使用SecretKeySpec重新生成key============*/
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                        org.apache.commons.codec.binary.Base64.decodeBase64(readKeyFromFile("F://test/key.txt")), "AES");

        cipher.init(Cipher.DECRYPT_MODE,
                        secretKeySpec,
                        cipher.getParameters().getParameterSpec(IvParameterSpec.class));
        bytes = cipher.doFinal(bytes);
        System.out.println("解密数据: " + new String(bytes));
    }

    /**
     * 可以使用公钥加密、私钥解密 也可以以 私钥加密公钥解密
     *  * KeyPairGenerator用于生成一对密钥对，用于做非对称加密操作。
     * KeyPairGenerator.getInstance(String alorithm)的可用参数为：
     * DSA、RSA、EC
     *
     * 代码生成的密钥对通常需要将公钥和私钥保存到文件中，这样才能够持久化进行操作，下面演示两种保存的实现
     *
     * @throws Exception
     */
    public void testSaveKeyPair2() throws Exception{
        Map<String, String> keyMap = genRSAKeyPair("RSA", "123456");
        String pubKey = keyMap.get(PUBLIC_KEY);
        String priKey = keyMap.get(PRIVATE_KEY);
        // 保存密钥
        writeKeyToFile("F://test/public.key", pubKey);
        writeKeyToFile("F://test/private.key", priKey);

        String filePubKey = readKeyFromFile("F://test/public.key");
        String filePriKey = readKeyFromFile("F://test/private.key");

        // 公钥加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, transRSAKey(true, filePubKey));
        byte[] bytes = cipher.doFinal("helloworld".getBytes());
        String enc = Base64.getEncoder().encodeToString(bytes);
        System.out.println("加密后的内容： " + enc);

        cipher.init(Cipher.DECRYPT_MODE, transRSAKey(false, filePriKey));
        byte[] dec = cipher.doFinal(org.apache.commons.codec.binary.Base64.decodeBase64(enc));

        System.out.println("新的私钥解密： " + new String(dec));
    }

    /**
     * 保存密钥对的特征值 公钥（N，e）私钥（N，d）
     * @throws Exception
     */
    public void testSaveKeyPairFeature() throws Exception{
        final String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);

        keyPairGenerator.initialize(1024);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();

    /*特征值N  e   d*/

        BigInteger N = publicKey.getModulus();
        BigInteger e = publicKey.getPublicExponent();
        BigInteger d = privateKey.getPrivateExponent();

    /**/
        String nStr = Base64.getEncoder().encodeToString(N.toByteArray());
        String eStr = Base64.getEncoder().encodeToString(e.toByteArray());
        String dStr = Base64.getEncoder().encodeToString(d.toByteArray());

    /*将这三个字符串保存到文件或者数据库，通常n，e可以保存在客户端，而n，d的数据必须保存在服务端*/

        N = new BigInteger(Base64.getDecoder().decode(nStr));
        e = new BigInteger(Base64.getDecoder().decode(eStr));
        d = new BigInteger(Base64.getDecoder().decode(dStr));

     /*根据N，e生成公钥*/
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(N, e);
        PublicKey pbk = KeyFactory.getInstance(algorithm).generatePublic(publicKeySpec);

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, pbk);
        //bytes 是加密后的数据
        byte[] bytes = cipher.doFinal("helloworld".getBytes());
        //用base64转换输出
        System.out.println("加密数据：" + Base64.getUrlEncoder().encodeToString(bytes));

     /*根据N，d生成私钥*/
        RSAPrivateKeySpec ps = new RSAPrivateKeySpec(N, d);
        PrivateKey prk = KeyFactory.getInstance(algorithm).generatePrivate(ps);

        cipher.init(Cipher.DECRYPT_MODE, prk);
        bytes = cipher.doFinal(bytes);
        System.out.println("解密数据：" + new String(bytes));
    }

    /**
     * 密钥转换
     * 公钥 byte编码 X.509
     * 私钥 byte编码 PKCS8
     * @param isPub 是否公钥 true 公钥
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public static Key transRSAKey(boolean isPub, String key) throws Exception{
        byte[] keyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(key);
        KeyFactory keyFactory = KeyFactory.getInstance(AlgEnums.RSA.getCode());
        if (isPub) {
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(keyBytes);
            return keyFactory.generatePublic(x509);
        } else {
            PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(keyBytes);
            return keyFactory.generatePrivate(pkcs8);
        }

    }
    /**
     * 生成密钥RSA对
     * @param alg
     * @param seed
     * @return
     * @throws Exception
     */
    public static Map<String, String> genRSAKeyPair(String alg, String seed) throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(alg);
        keyPairGenerator.initialize(1024, new SecureRandom(seed.getBytes()));
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();//公钥
        PrivateKey privateKey = keyPair.getPrivate();//私钥

        Map<String, String> keyMap = new HashMap<>();
        // 公钥 byte X.509编码
        keyMap.put(PUBLIC_KEY, org.apache.commons.codec.binary.Base64.encodeBase64String(publicKey.getEncoded()));
        // 私钥 byte PCKS8
        keyMap.put(PRIVATE_KEY, org.apache.commons.codec.binary.Base64.encodeBase64String(privateKey.getEncoded()));

        return keyMap;

    }


    /**
     * 生成密钥SM2对
     * @return
     * @throws Exception
     */
    public static Map<String, String> genSm2KeyPair() throws Exception{
        // 构造曲线
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName(curveName);
        ECDomainParameters domainParams = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        ECKeyPairGenerator keyGen = new ECKeyPairGenerator();
        keyGen.init(new ECKeyGenerationParameters(domainParams, SecureRandom.getInstance("SHA1PRNG")));
        AsymmetricCipherKeyPair keyPair = keyGen.generateKeyPair();

        ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();
        ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();

        // 私钥d值
        String privateKeyHex = ByteUtils.toHexString(priKey.getD().toByteArray()).toUpperCase();

        //公钥 q值 pubK 还有 x、y值
        String publicKeyHex = ByteUtils.toHexString(pubKey.getQ().getEncoded(false)).toUpperCase();

        Map<String, String> keyMap = new HashMap<>();
        keyMap.put(PUBLIC_KEY, publicKeyHex);
        keyMap.put(PRIVATE_KEY, privateKeyHex);
        return keyMap;
    }

}
