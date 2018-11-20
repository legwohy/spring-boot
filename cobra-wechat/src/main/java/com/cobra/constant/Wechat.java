package com.cobra.constant;


public interface Wechat
{
    String appId = "";
    String appSecret = "";
    String hostName = "www.legwohy.com";
    String redirectUrl = hostName.concat("/index");

    // 有过期时间
    String accessToken = "";

    String code_url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    String access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    String refresh_token_url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?";
    String user_info_url = "https://api.weixin.qq.com/sns/userinfo?";
}
