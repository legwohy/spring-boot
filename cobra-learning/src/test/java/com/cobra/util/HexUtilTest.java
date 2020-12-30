package com.cobra.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author admin
 * @date 2020/12/30 15:35
 * @desc
 */
public class HexUtilTest {
    @Test
    public void encode() throws Exception{
        String src = "hex";
        String cipher = HexUtil.encode(src);
        Assert.assertEquals("686578",cipher);

        String ori = HexUtil.decode(cipher);
        Assert.assertEquals(src,ori);
    }



}