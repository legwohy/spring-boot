package com.cobra.util.cryto;

import java.security.*;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

/**
 * signature类用于提供数字签名，用于保证数据的完整性
 *
 * <p>
 *      NONEwithRSA、MD2withRSA、MD5withRSA、SHA1withRSA、SHA224withRSA、SHA256withRSA、SHA384withRSA
 *      SHA512withRSA、NONEwithDSA、SHA1withDSA、SHA224withDSA、SHA256withDSA、NONEwithECDSA、SHA1withECDSA、
 *      SHA224withECDSA、SHA256withECDSA、SHA384withECDSA、SHA512withECDSA
 * </p>

 * @author admin
 * @date 2020/12/30 17:37
 * @desc
 */
public class SignatureUtils {
    static String alg = "NONEwithRSA";

    public static String signature(String srcPlainText, String privateKey) throws Exception{
        PrivateKey priKey = (PrivateKey)SecretKeyUtils.transRSAKey(Boolean.FALSE, privateKey);

        return signature(srcPlainText, priKey);

    }

    /**
     * 私钥签名
     * @param srcPlainText
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String signature(String srcPlainText, PrivateKey privateKey) throws Exception{
        //用私钥初始化signature
        Signature signature = Signature.getInstance(alg);
        signature.initSign(privateKey);
        //更新原始字符串
        signature.update(srcPlainText.getBytes());
        byte[] bytes = signature.sign();
        String sign = org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
        return sign;

    }

    public static boolean verify(String srcPlainText, String cipherText, String publicKey) throws Exception{
        PublicKey pubKey = (PublicKey)SecretKeyUtils.transRSAKey(Boolean.TRUE, publicKey);
        return verify(srcPlainText, cipherText, pubKey);

    }

    /**
     * 公钥验签
     * @param srcPlainText
     * @param cipherText
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static boolean verify(String srcPlainText, String cipherText, PublicKey publicKey) throws Exception{
        Signature signature = Signature.getInstance(alg);
        //用公钥初始化signature
        signature.initVerify(publicKey);
        //校验签名是否正确
        signature.update(srcPlainText.getBytes());

        return signature.verify(Base64.decodeBase64(cipherText));

    }

    /**
     * <p>
     *     生成key.store(包含私钥和证书)  keytool -genkey -alias test -keyalg RSA -keystore c:/key.store
     *     生成证书(从keyStore中导出证书)  keytool -export -alias test -keystore c:/key.store -file c:/key.cert
     * </p>
     *
     */
    static String keyStorePath = "/kft/key.store";
    static String alias = "test";
    static String password = "123456";

    /**
     *
     *  keyStore获取私钥签名
     * @return
     */
    public static String signatureByKeyStore(String srcPlainText) throws Exception{

        X509Certificate certificate = (X509Certificate)SecretKeyUtils.getCertificateFromKeyStore(null, keyStorePath, password.toCharArray(), alias);
        Signature signature = Signature.getInstance(certificate.getSigAlgName());
        // 初始化签名,由私钥构建
        PrivateKey privateKey =SecretKeyUtils.getPrivateKeyFromKeyStore("JKS", keyStorePath, password.toCharArray(), alias, password.toCharArray());
        signature.initSign(privateKey);

        signature.update(srcPlainText.getBytes());
        final byte[] signatureInfo = signature.sign();
        return Base64.encodeBase64String(signatureInfo);
    }



    public static boolean verify(String srcPlainText, String signatureInfo, X509Certificate certificate) throws Exception{
        byte[] srcPlainTextBytes = srcPlainText.getBytes("utf-8");
        // 原签名串
        byte[] sign = Base64.decodeBase64(signatureInfo);

        Signature signature = Signature.getInstance(certificate.getSigAlgName());
        // 由证书初始化签名,使用证书中的公钥
        signature.initVerify(certificate);
        signature.update(srcPlainTextBytes);
        return signature.verify(sign);

    }

    /**
     *
     * 证书验签
     * @param cerPath /kft/20KFT.cer
     * @param srcPlainText 原文
     * @param cipherText 签名串
     * @return
     */
    public static boolean verifySignByCertPath(String cerPath, String srcPlainText, String cipherText){
        boolean verifySign = false;
        try {

            X509Certificate certificate = (X509Certificate)SecretKeyUtils.getCertificateFromPath(cerPath);
            return verify(srcPlainText, cipherText, certificate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return verifySign;
    }

    public static boolean verifySignByKeyStore(String srcPlainText, String cipherText) throws Exception{

        X509Certificate certificate = (X509Certificate)SecretKeyUtils.getCertificateFromKeyStore(
                        "JKS",
                        keyStorePath,
                        password.toCharArray(),
                        alias);
        return verify(srcPlainText, cipherText, certificate);

    }

}
