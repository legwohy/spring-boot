package com.cobra.util.cryto;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Base64;
import java.util.Map;

/**
 * @author admin
 * @date 2021/1/7 10:57
 * @desc
 */
@Slf4j
public class SmCipherUtilsTest {
    String srcPlainText = "ab内容A!@#$%";

    @Test
    public void tesSM4() throws Exception{

        String key = "698663d8d064266e2ead8d1d19cc5166";// 32
        String cipherText = SmCipherUtils.sm4CbcEnc(srcPlainText, key);

        String plainText = SmCipherUtils.sm4CbcDec(cipherText, key);
        Assert.assertEquals(srcPlainText, plainText);

    }

    @Test
    public void tesSM2() throws Exception{
        Map<String, String> keyPair = SecretKeyUtils.genSm2KeyPair();
        String pubKey = keyPair.get(SmCipherUtils.PUBLIC_KEY);
        String priKey = keyPair.get(SmCipherUtils.PRIVATE_KEY);
        System.out.println("公钥:\n" + pubKey);
        System.out.println("私钥:\n" + priKey);

        String cipherText = SmCipherUtils.enc(srcPlainText, pubKey);
        System.out.println("密文:\n" + cipherText);

        String plainText = SmCipherUtils.dec(cipherText, priKey);

        Assert.assertEquals(srcPlainText, plainText);

    }

    @Test
    public void testEnc() throws Exception{
        String pubKey = "0402C767563734AE05D8449197FB282020BB0149A8B4A40B2E13818901F87A22009CB8513C894392D78C4E40B3BFA4E1FB080DFC5396A883746030406EAC8BC0EC";
        String cipherText = SmCipherUtils.enc(srcPlainText, pubKey);

        // 每次加密出来的结果都不一样
        Assert.assertNotNull(cipherText);
    }

    @Test
    public void testDec() throws Exception{
        String cipherText = "BDO6oEjo7+GcxKE5BV12IUxIs0m6k/dWUpLAu1WRULRzlkBbCep8VbIQDZpOMzhatDnVufC3PIKnwLf0zIz4VukqukP5DZKIUJg4b6kjqDMpyFnYICLnyxJkt1sXrCfTEv5DamFoC+DS9cdPvg6b";
        String priKey = "00FD8C255C88B086516FDBC01B5BB16918B883D12755C10EDC70B8D57986280CBF";
        String plainText = SmCipherUtils.dec(cipherText, priKey);

        Assert.assertEquals(srcPlainText, plainText);
    }

    public static void main(String[] args){
        System.out.println(Base64.getEncoder().encodeToString("698663d8d064266e2ead8d1d19cc5166".getBytes()));
    }

}