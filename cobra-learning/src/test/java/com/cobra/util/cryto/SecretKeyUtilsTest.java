package com.cobra.util.cryto;

import com.cobra.util.cryto.enums.AlgEnums;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author admin
 * @date 2020/12/31 13:38
 * @desc
 */
public class SecretKeyUtilsTest {


    @Test
    public void generateKey() throws Exception{
        String seed = "1234";
        Assert.assertEquals("MTIzNDU2",SecretKeyUtils.generateKey(AlgEnums.AES.getCode(),seed,"0",AlgEnums.AES.getNation()));
    }

}