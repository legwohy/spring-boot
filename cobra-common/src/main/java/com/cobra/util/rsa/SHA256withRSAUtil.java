package com.cobra.util.rsa;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 * @date 2020/12/28 14:49
 * @desc
 */
public class SHA256withRSAUtil {
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String ENCODE_ALGORITHM = "SHA-256";
    public static final String PLAIN_TEXT = "test string";

    /**
     * 生成没有加密的密钥对
     * @param algorithm
     * @param keysize
     * @return
     * @throws Exception
     */
    public static Map<String, Object> createKey(String algorithm, int keysize) throws Exception{
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
        keyPairGen.initialize(keysize);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();

        //公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put("publicKey", publicKey);
        keyMap.put("privateKey", privateKey);
        return keyMap;
    }

    /**
     * 生成加过密的密钥对 pkcs#1格式私钥 pkcs#8格式公钥
     *
     * @param algorithm
     * @param keysize
     * @param privateKeyPwd
     * @return
     * @throws Exception
     */
    public static Map<String, Object> createEncryKeyStr(String algorithm, int keysize, String privateKeyPwd) throws Exception{
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
        keyPairGen.initialize(keysize);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();

        String pubKeyStr = new String(java.util.Base64.getEncoder().encode(publicKey.getEncoded())); //pkcs8格式
        String priKeyStr = new String(java.util.Base64.getEncoder().encode(privateKey.getEncoded())); //pkcs8格式
        System.out.println(priKeyStr);//从输出结果可以知道是PKCS#8格式的

        //私钥加密
        String privateKeyStr = privateKeyPwdToPKCS1(privateKey, privateKeyPwd);//使用BC加密私钥格式会被转为PKSC#1格式
        System.out.println(privateKeyStr);
        System.out.println(privateKeyNoPwdToPKCS1(privateKey));

        //公私钥对象存入map中
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put("publicKeyStr", pubKeyStr);
        keyMap.put("privateKeyStr", privateKeyStr);
        return keyMap;
    }

    /**
     * 将私钥转为PKCS#1格式私钥（加密）
     *
     * @param privateKey
     * @param filePasswd
     * @return
     */
    private static String privateKeyPwdToPKCS1(PrivateKey privateKey, String filePasswd){
        Security.addProvider(new BouncyCastleProvider());
        StringWriter sw = new StringWriter();
        PEMWriter writer = new PEMWriter(sw);
        try {
            writer.writeObject(privateKey, "DESEDE", filePasswd.toCharArray(),
                            new SecureRandom());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sw.toString();
    }

    /**
     * 将私钥转为PKCS#1格式私钥（没有加密）
     * @param privateKey
     * @return
     */
    private static String privateKeyNoPwdToPKCS1(PrivateKey privateKey){
        Security.addProvider(new BouncyCastleProvider());
        StringWriter sw = new StringWriter();
        PEMWriter writer = new PEMWriter(sw);
        try {
            writer.writeObject(privateKey);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sw.toString();
    }

    /**
     * 签名
     * @param privateKey  私钥
     * @param plainText 明文
     * @return
     */
    public static byte[] sign(PrivateKey privateKey, String plainText){
        MessageDigest messageDigest;
        byte[] signed = null;
        try {
            messageDigest = MessageDigest.getInstance(ENCODE_ALGORITHM);
            messageDigest.update(plainText.getBytes());
            byte[] outputDigest_sign = messageDigest.digest();
            System.out.println("SHA-256编码后-----》"
                            + org.apache.commons.codec.binary.Base64.encodeBase64String(outputDigest_sign));
            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(privateKey);
            Sign.update(outputDigest_sign);
            signed = Sign.sign();
            System.out.println("SHA256withRSA签名后-----》"
                            + org.apache.commons.codec.binary.Base64.encodeBase64String(signed));
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return signed;
    }

    /**
     * 验签
     * @param publicKey 公钥
     * @param plain_text 明文
     * @param signed 签名
     */
    public static boolean verifySign(PublicKey publicKey, String plain_text, byte[] signed){
        MessageDigest messageDigest;
        boolean SignedSuccess = false;
        try {
            messageDigest = MessageDigest.getInstance(ENCODE_ALGORITHM);
            messageDigest.update(plain_text.getBytes());
            byte[] outputDigest_verify = messageDigest.digest();
            //System.out.println("SHA-256加密后-----》" +bytesToHexString(outputDigest_verify));
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(outputDigest_verify);
            SignedSuccess = verifySign.verify(signed);
            System.out.println("验证成功？---" + SignedSuccess);

        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return SignedSuccess;
    }

    /**
     * bytes[]换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

}
