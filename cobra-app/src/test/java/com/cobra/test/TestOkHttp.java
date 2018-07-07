package com.cobra.test;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TestOkHttp
{
    public static void main(String[] args){
        String url = "https://blog.csdn.net/bo543937071/article/details/53380651";
        TestOkHttp test = new TestOkHttp();
        String rs = test.get(url);

        System.out.println("-------21   -->rs="+rs);
    }

    /**
     * get请求
     * @param url
     * @return
     */
    public  String get(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        Response response = null;
        try {
            request = new Request.Builder().url(url).build();// 请求
            response = client.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();// 输出响应
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
