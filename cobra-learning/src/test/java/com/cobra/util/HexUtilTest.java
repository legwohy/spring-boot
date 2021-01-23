package com.cobra.util;

import com.cobra.util.cryto.HexUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author admin
 * @date 2020/12/30 15:35
 * @desc
 */
public class HexUtilTest {
    /**
     * string 转化
     * @throws Exception
     */
    @Test
    public void encode() throws Exception{
        String src = "hexXb!@#十六进制";
        String cipher = HexUtil.encode(src);

        String ori = HexUtil.decode(cipher);
        Assert.assertEquals(src,ori);
    }

    @Test public void decode()throws Exception{
        Assert.assertEquals("",HexUtil.decode("6d15965727ea5e70f267e366c4b00f7a"));
    }

    /**
     * string与byte互转
     */
    @Test
    public void encode2(){
        String content = "aA12!@#$";
        byte[] bytes = HexUtil.toHexByte(content);

        String result = HexUtil.toHexString(bytes);

        Assert.assertEquals(content,result);
    }



}