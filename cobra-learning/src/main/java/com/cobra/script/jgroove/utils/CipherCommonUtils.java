package com.cobra.script.jgroove.utils;

import com.cobra.script.jgroove.pojo.ScriptDTO;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.StringWriter;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CipherCommonUtils {
    public static void main(String[] args){
        String content = "测试加密";
        System.out.println("md5签名:" + doMD5(content));
        System.out.println("sha1签名:" + doSHA1(content));
        System.out.println("sha256签名:" + doSHA256(content));

        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBImrOLkLteSSgbyr4kANAwvMnHNAbWefHVaA0uIRy9u+JnBFAyGAsFg40tLPmLYQJ9R/fkZvzcfrsfUMzY3s5kU/KE7siCBjPUA03dDdjSqktmI6+KWT8d+ATAY7KBCtuAGAzYkfEq2i8XQmszGvxopyb6RVpUtiqGzNJjzw/DQIDAQAB";
        String priKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIEias4uQu15JKBvKviQA0DC8ycc0BtZ58dVoDS4hHL274mcEUDIYCwWDjS0s+YthAn1H9+Rm/Nx+ux9QzNjezmRT8oTuyIIGM9QDTd0N2NKqS2Yjr4pZPx34BMBjsoEK24AYDNiR8SraLxdCazMa/GinJvpFWlS2KobM0mPPD8NAgMBAAECgYB/2YhvC4Q9wwPNR2L+5u4hjyHXpoIvoiqEwzSzHcAcZnkv//Fg90GoREnaStIV4RhqF2NwIKSLOVzaCQageSzEn7Qe5bt0cHOap0hm/UO+wdWmU9areXNyAn66Z1Laryzy4DlLx40cVMcDLiwdYVH9uJLe251E3F0YeSf0JtH2TQJBAMazptSlqRnyVwVw4hNqxUgf74/iyQOerOXBEwqT+8O2ahYmroWLgbM/UjJXGv0OmsIiPc7K4UhNYbqSQ2WITFMCQQCmXz3Jua5WDaYRIRq9A2E/cLZ+gp6221SP34u0R+9hrNrcp9dnyv2KuMTA3ymoEgtc4iKMXHs2SZBrlHHd0NsfAkBlL8t/crVAj1adYLTgKnk4l3rJjTXMcBugDMiDlISbT+OKUDyDqV1tSIu1MwJ9Z9iLkCBllI9Q4IcqGZ3UsKodAkAfU4ea2C+PXHqMdyNFu8Ev5NU39PhVje7aOElh5LmaNqXvLltM29fpqujBOEPrtZoWHy5KJ4+7pRZga08VaSIZAkA8y1rd9v/p6QX4a0DpV96SIFn9BJtGlj26+D3kE3uhaoXJ8A6vmVobFq8WjsEsFpOqUY5T580QtYm5WSAdLeQm";
        System.out.println("RSA-Sha1签名:" + doRSASha1(content, priKey));
        System.out.println("RSA-Sha256签名:" + doRSASha256(content, priKey));

        String cipherText = doRSAPublicEncrypt(content, pubKey);
        System.out.println("RSA公钥加密:" + cipherText);
        System.out.println("RSA私钥解密:" + doRSAPrivateDecrypt(cipherText, priKey));

        cipherText = doRSAPrivateEncrypt(content, priKey);
        System.out.println("RSA私钥加密:" + cipherText);
        System.out.println("RSA公钥解密:" + doRSAPublicDecrypt(cipherText, pubKey));



    }

    /**
     * Md5签名
     *
     * @param content
     * @return
     */
    public static String doMD5(String content){
        return messageDigest(content, "MD5");

    }

    /**
     * 脚本调试使用
     * Md5签名
     * @return
     */
    public static void doMD5(ScriptDTO ScriptDTO){
        ScriptDTO.setRequestBody(doMD5(ScriptDTO.getRequestBody()));
    }

    /**
     * SHA1签名
     *
     * @param content
     * @return
     */
    public static String doSHA1(String content){
        return messageDigest(content, "SHA-1");
    }

    /**
     * 脚本调试使用
     * SHA1签名
     *
     * @param ScriptDTO
     * @return
     */
    public static void doSHA1(ScriptDTO ScriptDTO){
        ScriptDTO.setRequestBody(doSHA1(ScriptDTO.getRequestBody()));
    }

    /**
     * SHA256签名
     *
     * @param content
     * @return
     */
    public static String doSHA256(String content){
        return messageDigest(content, "SHA-256");

    }

    /**
     * SHA256签名
     *
     * @param ScriptDTO
     * @return
     */
    public static void doSHA256(ScriptDTO ScriptDTO){
        ScriptDTO.setRequestBody(messageDigest(ScriptDTO.getRequestBody(), "SHA-256"));

    }

    /**
     * SHA1withRSA 签名
     *
     * @param content
     * @param privateKey
     * @return
     */
    public static String doRSASha1(String content, String privateKey){
        return signature(content, privateKey, "SHA1withRSA");

    }

    /**
     * SHA1withRSA 签名
     *
     * @param ScriptDTO
     * @param privateKey
     * @return
     */
    public static void doRSASha1(ScriptDTO ScriptDTO, String privateKey){
        ScriptDTO.setRequestBody(doRSASha1(ScriptDTO.getRequestBody(), privateKey));
    }

    /**
     * SHA256withRSA 签名
     *
     * @param content
     * @param privateKey
     * @return
     */
    public static String doRSASha256(String content, String privateKey){
        return signature(content, privateKey, "SHA256withRSA");
    }

    /**
     * SHA256withRSA 签名
     *
     * @param ScriptDTO
     * @param privateKey
     * @return
     */
    public static void doRSASha256(ScriptDTO ScriptDTO, String privateKey){
        ScriptDTO.setRequestBody(doRSASha256(ScriptDTO.getRequestBody(), privateKey));
    }

    /**
     * RSA公钥加密
     *
     * @param content
     * @param publicKey
     * @return
     */
    public static String doRSAPublicEncrypt(String content, String publicKey){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            //加密
            byte[] keyBytes = Base64.decodeBase64(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(keyBytes);

            cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(x509));
            byte[] bytes = cipher.doFinal(content.getBytes());
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * RSA公钥加密
     *
     * @param ScriptDTO
     * @param publicKey
     * @return
     */
    public static void doRSAPublicEncrypt(ScriptDTO ScriptDTO, String publicKey){
        ScriptDTO.setRequestBody(doRSAPublicEncrypt(ScriptDTO.getRequestBody(), publicKey));
    }

    /**
     * RSA公钥解密
     *
     * @param content
     * @param publicKey
     * @return
     */
    public static String doRSAPublicDecrypt(String content, String publicKey){
        try {

            Cipher cipher = Cipher.getInstance("RSA");
            //加密
            byte[] keyBytes = Base64.decodeBase64(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(keyBytes);

            cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePublic(x509));
            byte[] bytes = cipher.doFinal(Base64.decodeBase64(content));
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * RSA公钥解密
     *
     * @param ScriptDTO
     * @param publicKey
     * @return
     */
    public static void doRSAPublicDecrypt(ScriptDTO ScriptDTO, String publicKey){
        ScriptDTO.setRequestBody(doRSAPublicDecrypt(ScriptDTO.getRequestBody(), publicKey));
    }

    /**
     * RSA私钥加密
     *
     * @param content
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String doRSAPrivateEncrypt(String content, String privateKey){
        try {
            //获取cipher对象
            Cipher cipher = Cipher.getInstance("RSA");
            // 解密
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(keyBytes);
            cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePrivate(pkcs8));
            byte[] bytes = cipher.doFinal(content.getBytes());
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RSA私钥加密
     *
     * @param ScriptDTO
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static void doRSAPrivateEncrypt(ScriptDTO ScriptDTO, String privateKey){
        ScriptDTO.setRequestBody(doRSAPrivateEncrypt(ScriptDTO.getRequestBody(), privateKey));
    }

    /**
     * RSA私钥解密
     *
     * @param content
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String doRSAPrivateDecrypt(String content, String privateKey){
        try {
            //获取cipher对象
            Cipher cipher = Cipher.getInstance("RSA");
            // 解密
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(keyBytes);
            cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePrivate(pkcs8));
            byte[] bytes = cipher.doFinal(Base64.decodeBase64(content));
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RSA私钥解密
     *
     * @param ScriptDTO
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static void doRSAPrivateDecrypt(ScriptDTO ScriptDTO, String privateKey){
        ScriptDTO.setRequestBody(doRSAPrivateDecrypt(ScriptDTO.getRequestBody(), privateKey));
    }

    /**
     * AES 解密
     * 先base64解码再解密
     *
     * @return 解密明文 ( 为UTF_8编码集)
     */
    public static String doAesDecrypt(String sSrc, String sKey) throws Exception{
        try {
            // 判断Key是否正确
            if (sKey == null) {
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                throw new InvalidKeyException("The key's length must be 16");
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("Xadiapdfaxi0s91D".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            //先用base64解密
            byte[] encrypted1 = new Base64().decode(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return new String(original, "UTF-8");
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * AES 解密
     * 先base64解码再解密
     *
     * @return 解密明文 ( 为UTF_8编码集)
     */
    public static void doAesDecrypt(ScriptDTO ScriptDTO, String sKey) throws Exception{
        ScriptDTO.setRequestBody(doAesDecrypt(ScriptDTO.getRequestBody(), sKey));
    }

    /**
     * AES 加密并base64转码
     *
     * @param sSrc 加密内容 必填 ( 必须为UTF_8)
     * @param sKey 密钥 必填
     * @return 成功或失败或异常信息
     */
    public static String doAesEncrypt(String sSrc, String sKey) throws Exception{
        if (sKey == null) {
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            throw new InvalidKeyException("The key's length must be 16");
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        //"算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec("Xadiapdfaxi0s91D".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * AES 加密并base64转码
     *
     * @param sKey 密钥 必填
     * @return 成功或失败或异常信息
     */
    public static void doAesEncrypt(ScriptDTO ScriptDTO, String sKey) throws Exception{
        ScriptDTO.setRequestBody(doAesEncrypt(ScriptDTO.getRequestBody(), sKey));
    }



    private static final ObjectMapper MAPPER = new ObjectMapper();

    static{
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    private static final JsonFactory JSONFACTORY = new JsonFactory();

    /**
     * 转换vo 为 json
     */
    private static String bean2Json(Object o){
        StringWriter sw = new StringWriter();
        JsonGenerator jsonGenerator = null;

        try {
            jsonGenerator = JSONFACTORY.createJsonGenerator(sw);
            MAPPER.writeValue(jsonGenerator, o);
            return sw.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            if (jsonGenerator != null) {
                try {
                    jsonGenerator.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 转换Json String 为 HashMap
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> json2Map(String json,
                    boolean collToString){
        try {
            Map<String, Object> map = MAPPER.readValue(json, HashMap.class);
            if (collToString) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getValue() instanceof Collection
                                    || entry.getValue() instanceof Map) {
                        entry.setValue(bean2Json(entry.getValue()));
                    }
                }
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String signature(String content, String privateKey, String alg){
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //用私钥初始化signature
            Signature signature = Signature.getInstance(alg);
            signature.initSign(keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes)));
            //更新原始字符串
            signature.update(content.getBytes());
            byte[] bytes = signature.sign();
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("不支持算法," + alg);
        }

    }

    private static String messageDigest(String content, String alg){
        //参数可以是 MD5,MD2,MD5,SHA-1,SHA-224,SHA-256,SHA-384,SHA-512
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(alg);
            return Hex.encodeHexString(messageDigest.digest(content.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

}
