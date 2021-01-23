package com.cobra.util.cryto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * <p>
 *     HmacSHA1、HmacSHA224、 HmacSHA256、 HmacSHA384、 HmacSHA512、HmacMD5
 * </p>
 *
 * @author admin
 * @date 2020/12/30 17:36
 * @desc
 */
public class MacUtils {

    static String alg = "HmacMD5";
    public static String HmacMD5(String content, String key) throws Exception{
        Mac mac = Mac.getInstance(alg);
        //第一个参数可以是任意字符串,第二个参数与获取Mac对象的algorithm相同
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), alg);
        mac.init(secretKeySpec);
        byte[] bytes = mac.doFinal(content.getBytes());

        return HexUtil.toHexString(bytes);

    }


}
