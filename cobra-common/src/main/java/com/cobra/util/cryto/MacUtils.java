package com.cobra.util.cryto;


import com.cobra.util.HexUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * HmacMD5
 HmacSHA1
 HmacSHA224
 HmacSHA256
 HmacSHA384
 HmacSHA512
 *
 * @author admin
 * @date 2020/12/30 17:36
 * @desc
 */
public class MacUtils {

    public void HmacMD5() throws Exception {
        Mac mac = Mac.getInstance("HmacMD5");
        //第一个参数可以是任意字符串,第二个参数与获取Mac对象的algorithm相同
        SecretKeySpec secretKeySpec = new SecretKeySpec("123456".getBytes(), "HmacMD5");
        mac.init(secretKeySpec);
        byte[] bytes = mac.doFinal("helloworld".getBytes());
        System.out.println("HmacMD5结果：" + HexUtil.toHexString(bytes));

    }

    public void HmacSHA1() throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec("123456".getBytes(), "HmacSHA1"));
        byte[] bytes = mac.doFinal("helloworld".getBytes());
        System.out.println("HmacSHA1结果：" + HexUtil.toHexString(bytes));
    }


}
