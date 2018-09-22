package com.cobra.constants;

/**
 * 微信相关的常量类
 */
public final class WeChatConstant {
    private WeChatConstant() {
        throw new AssertionError("No com.cobra.constants.Constant instances for you!");
    }

    public static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
}