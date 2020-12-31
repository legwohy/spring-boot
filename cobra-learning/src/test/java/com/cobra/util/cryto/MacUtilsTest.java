package com.cobra.util.cryto;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author admin
 * @date 2020/12/31 10:34
 * @desc
 */
public class MacUtilsTest {
    @Test
    public void hmacMD5() throws Exception{
        String value = MacUtils.HmacMD5("flower","123456");
        Assert.assertEquals("b2935e938d3ea373ee8745b0fe0a1ec6",value);// 32
    }


}