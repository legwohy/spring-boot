package com.cobra.test;

import okhttp3.*;
import java.io.File;
import java.io.IOException;


public class MainTest {
    public static void main(String[] args){
        System.out.println("---------->校验身份证正面开始");
        String apiKey = "LGq9uVn4OQnzFgNlSQ_S8-706xmcq5ij";
        String apiSecret = "NFzaxWUh4_o3st_Kr-kCOb53ZIbSbJ0V";
        String faceApiBaseUrl = "https://api.faceid.com";

        //--------------------okHttp------------
        OkHttpClient client = new OkHttpClient();

        File file = new File("E:/temp/test3.jpg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM) // 表单
                .addFormDataPart("file",file.getName(),RequestBody.create(MediaType.parse("application/json"),file))
                .addFormDataPart("api_key",apiKey)
                .addFormDataPart("api_secret",apiSecret)
                .addFormDataPart("legality","1")
                .addFormDataPart("userName","rose")
                .addFormDataPart("passwd","456")
                .build();// 请求体


        Request request = new Request.Builder()
                .addHeader("Connection","Keep-Alive")
                .addHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)")
                .post(requestBody)
                .url("http://192.168.1.4:8090/upload")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                System.out.println("返回结果:--------->"+response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("---------->校验身份证正面结束");


    }

}
