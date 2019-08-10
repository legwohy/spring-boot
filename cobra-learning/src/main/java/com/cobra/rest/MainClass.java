package com.cobra.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by legwo on 2018/12/19.
 */
public class MainClass
{
    public static void main(String[] args)
    {

        // 客户端
        RestTemplate client = new RestTemplate();

        // 构造请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);// 表单提交

        // 参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // 构造请求体
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // 执行http请求
        String url = "https://www.baidu.com";
        ResponseEntity<String> responseEntity = client.exchange(url, HttpMethod.POST, requestEntity, String.class);

        System.out.println(responseEntity.getBody());

    }

    @Test public void get() throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("https://www.cnblogs.com/liujisong2014221/articles/8269572.html");
        CloseableHttpResponse response = httpClient.execute(get);
        System.out.println("=========>"+response);
        System.out.println("headers>"+response.getAllHeaders().toString());
        System.out.println("entirty>"+response.getEntity().getContent());
        System.out.println("locate>"+response.getLocale());
        System.out.println("protocolVersion>"+response.getProtocolVersion());
        response.close();
        httpClient.close();
    }
}
