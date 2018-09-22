package com.cobra.service;

import com.cobra.constants.WeChatConstant;
import com.cobra.util.OkHttpUtil;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public  class WeChat {
    private String appIdTest = "wx1cfea202c46b9760";
    private String appSecretTest = "90b95f773a4eebb3600880db3bc4adb4";
    @Value("appId") private String appId;
    @Value("appSecret") private String appSecret;

    public String getAccessToken(){
        Map<String,String> params = new HashMap<>();
        params.put("appid",appIdTest);
        params.put("secret",appSecretTest);
        String accessToken = OkHttpUtil.get(WeChatConstant.URL_ACCESS_TOKEN,params);

        return accessToken;

    }

    public static void main(String[] args){


        WeChat weChat = new WeChat();
        System.out.println("==========:"+weChat.getAccessToken());

    }

}
