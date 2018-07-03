package com.ihome.filter;


import com.alibaba.fastjson.JSONObject;
import com.ihome.constants.TokenConstant;
import com.ihome.util.rsa.RSAEncrypt;
import com.ihome.util.rsa.RSASignature;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@WebFilter(urlPatterns = "/api/*")
public class IdentifiedFilter implements Filter
{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 白名单
        String uri = request.getRequestURI();
        System.out.println("--------->白名单的地址:"+uri);
        if("/api/img/upload".equals(uri)){

            filterChain.doFilter(request,response);
            return;
        }

        String token = request.getHeader(TokenConstant.token);
        // 解析
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(TokenConstant.token);
        }


        String[] arr = token.split("\\.");

        String payLoad = new String(Base64.decodeBase64(arr[1].getBytes()));
        JSONObject jsonPayLoad = JSONObject.parseObject(payLoad);

        long expiredTime = jsonPayLoad.getString("exp") == null?0:Long.parseLong(jsonPayLoad.getString("exp"));
        if(System.currentTimeMillis() > expiredTime){
            write(response);
            return;
        }

        // 验签
        try
        {
           String header = new String(Base64.decodeBase64(arr[0]));
           String signature = new String(Base64.decodeBase64(arr[2]));
           String publicKey = RSAEncrypt.loadPublicKeyByFile(TokenConstant.keyPath);
           boolean flag = RSASignature.doCheck(header.concat(payLoad),signature,publicKey);
           if(!flag){
               write((HttpServletResponse) servletResponse);
               return;
           }

           filterChain.doFilter(request,response);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void write(HttpServletResponse response){
        response.setHeader("Content-type","application/json;charset=utf-8");
        try
        {
            response.getWriter().print("傻逼");
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
