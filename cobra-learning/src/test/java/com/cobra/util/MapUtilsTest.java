package com.cobra.util;

import org.junit.Test;


/**
 * @author admin
 * @date 2021/1/18 14:26
 * @desc
 */
public class MapUtilsTest {
    @Test
    public void readValue() throws Exception{
        String json = "{\"credit\":{\"header\":{\"mobile\":\"credit.header.mobile\",\"version_fix\":\"credit.header.version_fix\",\"flag_system\":\"credit.header.flag_system\"},\"body\":\"credit.body\",\"mac\":\"credit.mac\"}}";
        String paramIndex = "credit.header.mobile";
        System.out.println(MapUtils.readValue(json,paramIndex));
    }

}