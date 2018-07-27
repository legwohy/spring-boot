package com.cobra.constants;


import java.util.Map;

/**
 * 常量类建议final(继承) private(实例化)
 */
public final class BackConfigParam {
    private BackConfigParam(){
        throw new AssertionError("No com.cobra.constants.BackConfigParam instances for you!");
    }
    public static Map<String,Object> backMap;
}
