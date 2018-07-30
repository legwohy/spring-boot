package com.cobra.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cobra.constants.Constant;
import com.cobra.util.FileUtils;
import com.cobra.util.rsa.RSAEncrypt;
import com.cobra.util.rsa.RSASignature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@Order(1)
@WebFilter(urlPatterns = "/api/*") // 没有拦截到kefu/distribute
public class IdentifiedFilter implements Filter
{

    @Value("${strWhite}") private String strWhite;
    List<String> whitelist = new ArrayList<>();
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        String[] whites = strWhite.split(";");
        for (String white:whites){
            if(!StringUtils.isEmpty(white)){
                whitelist.add(white);
            }

        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 白名单
        if(whitelist.contains(request.getRequestURI())){
            filterChain.doFilter(request,response);
            return;
        }


        String token = request.getHeader(Constant.token);
        // 解析
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(Constant.token);
        }

        if(StringUtils.isEmpty(token)){
            write(response,"token不能为空");
            return;
        }

        String[] arr = token.split("\\.");

        String payLoad = new String(Base64.decodeBase64(arr[1].getBytes()));
        JSONObject jsonPayLoad = JSONObject.parseObject(payLoad);

        long expiredTime = jsonPayLoad.getString("exp") == null?0:Long.parseLong(jsonPayLoad.getString("exp"));
        if(System.currentTimeMillis() > expiredTime){
            write(response,"token失效，请重新认证");
            return;
        }

        // 验签
        try
        {
           String signature = new String(Base64.decodeBase64(arr[2]));
           String publicKey = RSAEncrypt.loadPublicKeyByFile(FileUtils.getRootPath()+"/keys");
           boolean flag = RSASignature.doCheck(arr[0].concat(".").concat(arr[1]),signature,publicKey);
           log.debug("----->flag="+flag);
           if(flag){
               filterChain.doFilter(request,response);
               return;
           }

            write(response,"签名错误");

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void write(HttpServletResponse response,String message){
        response.setHeader("Content-type","application/json;charset=utf-8");
        try
        {
            Map<String,Object> map = new HashMap<>();
            map.put("code",400);
            map.put("msg",message);
            response.getOutputStream().write(JSON.toJSONString(map).getBytes("UTF-8"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy()
    {

    }
}
