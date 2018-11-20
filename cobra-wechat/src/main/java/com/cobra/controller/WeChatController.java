package com.cobra.controller;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@Controller
public class WeChatController
{
    private static final String oauth_url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    public String intoIndex(HttpServletRequest request,HttpServletResponse response)
    {
        String appid = "";
        String redirect_uri = "";
        String response_type = "code";
        String scope = "snsapi_userinfo";// 用于拉取用户的信息
        String url = oauth_url.
                        concat("appid="+appid).
                        concat("&redirect_uri="+ URLEncoder.encode(redirect_uri)).
                        concat("&response_type="+response_type).
                        concat("&scope="+scope)
                        .concat("#wechat_redirect");
        return null;

    }
}
