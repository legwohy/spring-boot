package com.cobra.util.cryto;

import com.cobra.util.StringCommonUtils;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 <dependency>
 <groupId>org.bouncycastle</groupId>
 <artifactId>bcprov-jdk15on</artifactId>
 <version>1.65</version>
 </dependency>
 <dependency>
 <groupId>org.bouncycastle</groupId>
 <artifactId>bcpkix-jdk15on</artifactId>
 <version>1.65</version>
 </dependency>
 *
 * </p>
 * @author admin
 * @date 2021/1/7 10:56
 * @desc
 */
public class SmCipherUtils {
    static String CBC = "CBC";
    static String PUBLIC_KEY = "PUBLIC_KEY";
    static String PRIVATE_KEY = "PRIVATE_KEY";

    static String curveName = "sm2p256v1";// wapip192v1

    public static String sm4CbcEnc(String content, String seed) throws Exception{
        //String alg = "SM4/ECB/PKCS5Padding";
        String alg = "SM4/CBC/PKCS5Padding";
        String ivs = "1234567890123456";
        return doSm4(Cipher.ENCRYPT_MODE, alg, seed, ivs, content);

    }

    public static String sm4CbcDec(String content, String seed) throws Exception{
        //String alg = "SM4/ECB/PKCS5Padding";
        String alg = "SM4/CBC/PKCS5Padding";// 16位向量
        String ivs = "1234567890123456";
        return doSm4(Cipher.DECRYPT_MODE, alg, seed, ivs, content);
    }

    private static String doSm4(int mode, String alg, String seed, String srcPlainText){
        return doSm4(mode, alg, seed, null, srcPlainText);

    }

    private static String doSm4(int mode, String alg, String seed, String ivs, String srcPlainText){
        // BC
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(alg, BouncyCastleProvider.PROVIDER_NAME);
            String[] algArr = alg.split("/");
            String keyAlg = algArr[0];

            Key key = new SecretKeySpec(ByteUtils.fromHexString(seed), keyAlg);// hexString 转 byte[]
            String pad = algArr[1];
            if (CBC.equals(pad)) {
                if (StringCommonUtils.isEmpty(ivs)) {
                    throw new IllegalArgumentException("CBC算法 ivs不能为空");
                }
                cipher.init(mode, key, new IvParameterSpec(ivs.getBytes()));

            } else {
                cipher.init(mode, key);
            }

            if (Cipher.ENCRYPT_MODE == mode) {
                // 加密
                byte[] bytes = cipher.doFinal(srcPlainText.getBytes());
                return ByteUtils.toHexString(bytes);

            } else {
                // 解密
                byte[] bytes = cipher.doFinal(ByteUtils.fromHexString(srcPlainText));
                return new String(bytes);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 生成密钥对
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

    /**
     * 1、转换私钥
     * 2、解密
     * 3、结果转换
     * @param cipherData
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String dec(String cipherData, String privateKey) throws Exception{
        //JS加密产生的密文
        byte[] cipherDataByte = org.apache.commons.codec.binary.Base64.decodeBase64(cipherData);

        //刚才的私钥Hex，先还原私钥
        BigInteger privateKeyD = new BigInteger(privateKey, 16);

        X9ECParameters sm2ECParameters = GMNamedCurves.getByName(curveName);
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);


        // false 解密
        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(false, privateKeyParameters);

        // 解密
        byte[] bytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);

        return new String(bytes);
    }

    /**
     * 1、转换密钥
     * 2、加密
     * 3、转换为base64
     * @param cipherData
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String enc(String cipherData, String publicKey) throws Exception{
        //JS加密产生的密文
        byte[] cipherDataByte = cipherData.getBytes();

        //刚才的私钥Hex，先还原私钥
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");

        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        ECPublicKeyParameters publicKeyParameters =
                        new ECPublicKeyParameters(sm2ECParameters.getCurve().decodePoint(ByteUtils.fromHexString(publicKey)), domainParameters);


        //用私钥解密
        SM2Engine sm2Engine = new SM2Engine();
        // true 加密
        sm2Engine.init(true, new ParametersWithRandom(publicKeyParameters));

        byte[] bytes = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);

        return new String(Base64.getEncoder().encode(bytes));
    }

}
