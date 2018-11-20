package com.cobra.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cobra.constant.Wechat;
import com.cobra.pojo.UserInfo;
import com.cobra.util.OkHttpUtil;
import com.cobra.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;

@Slf4j
public class WechatInterceptor implements HandlerInterceptor
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception
    {

        String code = request.getParameter("code");
        if(StringUtils.isEmpty(code))
        {
            // 1、获取code
            String code_url = Wechat.code_url.concat("appid="+Wechat.appId).concat("&redirect_uri="+ URLEncoder.encode(Wechat.redirectUrl)).concat("&response_type=code&state=STATE#wechat_redirect");
            String codeResult = OkHttpUtil.get(code_url,new HashMap<>());
            log.info("获取code的返回值:"+codeResult);
        }

        // 2、换取网页的accessToken
        String access_token_url = Wechat.access_token_url
                        .concat("appid="+Wechat.appId)
                        .concat("&secret="+Wechat.appSecret)
                        .concat("&code="+code)
                        .concat("&grant_type=authorization_code");
        String accessTokenResult = OkHttpUtil.get(access_token_url,new HashMap<>());
        log.info("获取accessTokenResult:"+accessTokenResult);

        JSONObject tokenJson = JSON.parseObject(accessTokenResult);
        String accessToken = tokenJson.getString("access_token");
        String openId = tokenJson.getString("openid");
        // refreshToken 有效期一个月
        String refreshToken = tokenJson.getString("refresh_token");

        // 2、1 刷新token
        String refresh_token_url = Wechat.refresh_token_url.concat("appid="+Wechat.appId).concat("&grant_type=refresh_token").concat("&refresh_token="+refreshToken);
        String refreshsTokenResult = OkHttpUtil.get(refresh_token_url,new HashMap<>());
        log.info("刷新token返回值："+refreshsTokenResult);


        // 3、拉取用户信息
        String userInfoUrl = Wechat.user_info_url.concat("access_token="+accessToken).concat("&openid="+openId).concat("&lang=zh_CN");
        log.info("拉取用户信息的返回值:"+userInfoUrl);
        String strUserInfo = OkHttpUtil.get(userInfoUrl,new HashMap<>());
        JSONObject.parseObject(strUserInfo, UserInfo.class);








        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception
    {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception
    {

    }
}
