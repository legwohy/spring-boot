package com.cobra.util.cryto;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

/**
 *
 * KeyPairGenerator用于生成一对密钥对，用于做非对称加密操作。
 * KeyPairGenerator.getInstance(String alorithm)的可用参数为：
 * DSA、RSA、EC
 *
 * @author admin
 * @date 2020/12/30 17:39
 * @desc
 */
public class KeyPairGeneratorUtils {
    /**
     * 代码生成的密钥对通常需要将公钥和私钥保存到文件中，这样才能够持久化进行操作，下面演示两种保存的实现
     *
     * @throws Exception
     */
    public void testSaveKeyPair2() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey oldPbk = keyPair.getPublic();
        PrivateKey oldPrk = keyPair.getPrivate();

        Cipher cipher = Cipher.getInstance("RSA");
    /*============使用原始私钥加密，重新生成的公钥解密===============*/
        cipher.init(Cipher.ENCRYPT_MODE, oldPrk);
        byte[] bytes = cipher.doFinal("helloworld".getBytes());
        System.out.println("原始私钥加密： " + Base64.getEncoder().encodeToString(bytes));

    /*提取公钥的比特编码经过Base64转换后保存到文件，注意公钥的比特编码是X.509格式*/
        byte[] pbks = Base64.getEncoder().encode(oldPbk.getEncoded());
        File file = new File("F://test/public.key");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(pbks);
        fos.flush();
        fos.close();

    /*从文件中提取公钥比特编码并恢复成公钥*/
        file = new File("F://test/public.key");
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) > 0) {
            bos.write(buffer, 0, len);
        }
        pbks = Base64.getDecoder().decode(bos.toByteArray());
        X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(pbks);
        //重新得到公钥
        PublicKey newPbk = KeyFactory.getInstance("RSA").generatePublic(encodedKeySpec);


        cipher.init(Cipher.DECRYPT_MODE, newPbk);
        bytes = cipher.doFinal(bytes);
        System.out.println("新的公钥解密： " + new String(bytes));

      /*============使用原始公钥加密，重新生成的私钥解密===============*/
        cipher.init(Cipher.ENCRYPT_MODE, oldPbk);
        bytes = cipher.doFinal("helloworld".getBytes());
        System.out.println("原始私钥加密： " + Base64.getEncoder().encodeToString(bytes));


    /*省略了文件存取操作，与公钥相同*/

        byte[] prks = oldPrk.getEncoded();
    /*私钥的比特编码是pkcs8格式*/
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(prks);
        PrivateKey newPrk = KeyFactory.getInstance("RSA").generatePrivate(pkcs8EncodedKeySpec);
        cipher.init(Cipher.DECRYPT_MODE, newPrk);
        bytes = cipher.doFinal(bytes);
        System.out.println("新的私钥解密： " + new String(bytes));
    }

    /**
     * 保存密钥对的特征值 公钥（N，e）私钥（N，d）
     * @throws Exception
     */
    public void testSaveKeyPair() throws Exception {
        final String algorithm = "RSA";
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);

        keyPairGenerator.initialize(1024);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

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
