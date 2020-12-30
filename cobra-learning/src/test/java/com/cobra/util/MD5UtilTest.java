package com.cobra.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author admin
 * @date 2020/12/30 14:49
 * @desc
 */
public class MD5UtilTest {
    String md5 = "";
    @Before
    public void init(){
        md5 = MD5Util.generate("123456");
        System.out.println(md5);

    }

    @Test
    public void MD5() throws Exception{
        Assert.assertEquals("e10adc3949ba59abbe56e057f20f883e",MD5Util.MD5("123456"));
    }


    @Test
    public void verify() throws Exception{
        Assert.assertTrue(MD5Util.verify("123456",md5));
    }

    @Test public void tmp(){
        Assert.assertTrue(MD5Util.verify("123456","973b88a0352836f25a934a53475a7a26e944959a8308934e"));
        Assert.assertTrue(MD5Util.verify("123456","453e22a46e70808b5ae60730c79f1eb47c41261f7c612688"));
    }

}