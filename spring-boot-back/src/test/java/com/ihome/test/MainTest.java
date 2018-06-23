package com.ihome.test;

import com.ihome.util.AESUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by legwo on 2018/6/24.
 */
public class MainTest {
    Logger logger = LoggerFactory.getLogger(MainTest.class);
    public static void main(String[] args) throws Exception {
        String sb = AESUtils.encrypt("sb");
        System.out.println("----------->\t"+sb);
    }
}
