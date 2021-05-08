package com.cobra.util.cryto;

import com.cobra.util.StringCommonUtils;
import com.cobra.util.cryto.enums.AlgEnums;
import org.apache.commons.io.IOUtils;
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
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.Certificate;
import java.security.cert.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

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

    public final static String PUBLIC_KEY = "PUBLIC_KEY";
    public final static String PRIVATE_KEY = "PRIVATE_KEY";
    public final static String curveName = "sm2p256v1";// wapip192v1
    public final static String ALG_RANDOM = "SHA1PRNG";// 随机算法
    public final static String SEED_IS_KEY = "1";
    public final static String GENERATE_KEY = "0";
    public final static String PRODUCE_CN = "CN";
    public final static String PRODUCE_USA = "USA";


    /**
     * 生成密钥串
     * @param alg
     * @param seed
     * @return
     * @throws Exception
     */
    public static String generateKey(String alg, String seed,String seedIsKey) throws Exception{
        Key key = generateKey(alg,ALG_RANDOM,seed,seedIsKey);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 生成密钥对象
     * @see AlgEnums
     * @param keyAlg 随机算法
     * @param seed
     * @param seedIsKey
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Key generateKey(String keyAlg, String randomAlgName, String seed, String seedIsKey) throws NoSuchAlgorithmException
    {
        byte[] bytes;
        if(PRODUCE_CN.equalsIgnoreCase(AlgEnums.getNation(keyAlg))){
            // 国密
            bytes = ByteUtils.fromHexString(seed);
        }else {
            bytes = seed.getBytes();
        }
        Key key = null;
        if(SEED_IS_KEY.equals(seedIsKey)){
            key = new SecretKeySpec(bytes, keyAlg);
        }else {

            KeyGenerator keyGenerator = KeyGenerator.getInstance(keyAlg);

            SecureRandom random = null;
            // 随机算法
            if (StringCommonUtils.isNotEmpty(randomAlgName)) {
                random = SecureRandom.getInstance(randomAlgName);
                random.setSeed(bytes);
            } else {
                // TODO 坑 jdk高于8的默认不是 SHA1PRNG 算法 建议 指定算法
                random = new SecureRandom(seed.getBytes());
            }

            keyGenerator.init(AlgEnums.getLength(keyAlg), random);
            key = keyGenerator.generateKey();
        }
        return key;
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
     * @return
     * @throws Exception
     */
    public static Map<String, String> genRSAKeyPair() throws Exception{

        return genRSAKeyPair(UUID.randomUUID().toString());

    }
    public static Map<String, String> genRSAKeyPair(String seed) throws Exception{
        SecureRandom secureRandom = SecureRandom.getInstance(ALG_RANDOM);
        secureRandom.setSeed(seed.getBytes());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(AlgEnums.RSA.getCode());
        keyPairGenerator.initialize(AlgEnums.RSA.getKeyLength(), secureRandom);
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
        keyGen.init(new ECKeyGenerationParameters(domainParams, SecureRandom.getInstance(ALG_RANDOM)));
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



    /**
     * <p>
     *      从证书文件读取证书.'.crt'和'.cer'文件都可以读取 .cer是IE导出的公钥证书（der格式）
     * </p>
     * <>
     *     X.509证书,可能有不同的编码格式,目前有以下两种编码格式 PEM(文本 begin开头 end结尾，内容是base64编码)和DER(二进制)
     *     其它扩展名:
     *     CRT 证书 可能是 pem编码，也可能是der编码,大部分是pem编码，常见*nix
     *     CER 证书 可能是 pem编码，也可能是der编码,大部分是der编码 常见window
     *     CSR 证书签名请求，非证书，核心是公钥
     *     key 通常存放公钥或私钥，非X.509证书
     *     PFX/P12
     *
     *      查看KEY的办法:openssl rsa -in mykey.key -text -noout
     *      查看DER的办法:openssl rsa -in mykey.key -text -noout -inform der
     *
     * </>
     *
     * @param certificatePath
     *            证书文件路径:可以直接加载指定的文件,例如"file:C:/kft.cer"
     * @throws Exception
     */
    public static java.security.cert.Certificate getCertificateFromPath(String certificatePath) throws Exception{
        InputStream inputStream = null;
        try {
            inputStream = SecretKeyUtils.class.getResourceAsStream(certificatePath);
            // 实例化证书工厂
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            java.security.cert.Certificate cert = cf.generateCertificate(inputStream);
            return cert;
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * <p> KeyStore提取证书 </p>
     *
     * @param keystoreType  JKS(java keyStore java密钥库)
     *                      PKCS12(公钥加密标准)
     *                     如果为NULL, 先从Security.getProperty("keystore.type") 取值，不存在 jks
     *
     *
     * @param keyStorePath
     *            keystore文件路径:可以直接加载指定的文件,例如"file:C:/KFTCIPKeystore.keystore",也可以从classpath下加载,例如
     *            "classpath:/KFTCIPKeystore.keystore" 支持ANT语法 注意:从classpath下加载时,优先使用 thread context
     *            ClassLoader,没有找到的情况下,使用当前类加载器
     * @param keyStorePassword 访问密钥库的密码
     * @param alias 证书条目(包含公钥,私钥和数字证书)的别名.当keystore中只有一个条目时,此参数可以为null;如果有多个时,不能为null
     * @return
     * @throws Exception
     */
    public static java.security.cert.Certificate getCertificateFromKeyStore(String keystoreType,
                    String keyStorePath,
                    char[] keyStorePassword,
                    String alias) throws Exception{
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, keystoreType);
        if (alias == null)
        {
            List<String> aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1) {
                alias = aliases.get(0);
            }
            else
            {
                throw new IllegalArgumentException("[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
            }
        }
        return ks.getCertificate(alias);
    }

    /**
     * <p> KeyStore提取私钥 </p>
     *
     * @param keystoreType keystore的类型 ."JKS"或"PKCS12"等.默认是JKS ,如果为NULL,则默认使用
     *            java.security.KeyStore.getDefaultType()
     * @param keyStorePath keystore文件路径:可以直接加载指定的文件,例如"file:C:/KFTCIPKeystore.keystore"
     * @param keyStorePassword 访问密钥库的密码
     * @param alias 证书条目(包含公钥,私钥和数字证书)的别名.当keystore中只有一个条目时,此参数可以为null;如果有多个时,不能为null
     * @param keyPassword 私钥条目对应的密码
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromKeyStore(String keystoreType,
                    String keyStorePath,
                    char[] keyStorePassword,
                    String alias,
                    char[] keyPassword) throws Exception
    {
        KeyStore ks = loadKeyStore(keyStorePath, keyStorePassword, keystoreType);
        if (alias == null) {
            List<String> aliases = Collections.list(ks.aliases());
            if (aliases.size() == 1) {
                alias = aliases.get(0);
            }
            else
            {
                throw new IllegalArgumentException("[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
            }
        }
        PrivateKey key = (PrivateKey)ks.getKey(alias, keyPassword);
        return key;
    }

    /**
     * <p> 加载KeyStore 密钥库（包含私钥和证书）</p>
     *
     * @param keyStorePath resource下的文件路径
     * @param password 访问密钥库的密码
     * @param keystoreType keystore的类型,如果为NULL,则默认使用KeyStore.getDefaultType()
     * @return
     * @throws Exception
     */
    public static KeyStore loadKeyStore(String keyStorePath, char[] password, String keystoreType) throws Exception{
        KeyStore ks = KeyStore.getInstance(keystoreType == null ? KeyStore.getDefaultType() : keystoreType);

        InputStream inputStream = SecretKeyUtils.class.getResourceAsStream(keyStorePath);
        try {
            ks.load(inputStream, password);
            return ks;
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }


    /**
     * 可以使用公钥加密、私钥解密 也可以以 私钥加密公钥解密
     *  * KeyPairGenerator用于生成一对密钥对，用于做非对称加密操作。
     * KeyPairGenerator.getInstance(String alorithm)的可用参数为：
     * DSA、RSA、EC
     *
     * 代码生成的密钥对通常需要将公钥和私钥保存到文件中，这样才能够持久化进行操作，下面演示两种保存的实现
     *
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

}
