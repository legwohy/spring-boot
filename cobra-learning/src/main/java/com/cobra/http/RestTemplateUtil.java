package com.cobra.http;


import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by legwo on 2018/12/19.
 */
public class RestTemplateUtil {
    public static String post(String url){
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

        ResponseEntity<String> responseEntity = client.exchange(url, HttpMethod.POST, requestEntity, String.class);

        return responseEntity.getBody();

    }


}
