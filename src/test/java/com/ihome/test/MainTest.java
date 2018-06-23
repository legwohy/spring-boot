package com.ihome.test;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by legwo on 2018/6/21.
 */
public class MainTest {
    static Logger logger = LoggerFactory.getLogger(MainTest.class);

    public static void main(String[] args){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)  // 连接超时
                .writeTimeout(20,TimeUnit.SECONDS)  // 写入时间
                .readTimeout(20,TimeUnit.SECONDS)  // 读取时间
                .build();// 构建一个对象
        String url = "https://blog.csdn.net/u013651026/article/details/79738059";
        final Request request = new Request.Builder()
                .url(url) // url
                .get() // 默认是get请求
                .build();// 构建一个对象

        // 创建call
        Call call = client.newCall(request);

        // 加入异步队列
        call.enqueue(new Callback() {

            // 请求错误回调函数
            @Override
            public void onFailure(Call call, IOException e) {
                logger.info("--------->连接失败");
            }

            // 异步请求 非线程
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers headers = response.headers();
                logger.info("------------->"+headers.toString());
                if(response.isSuccessful()){
                    logger.info("-------->"+response.body().string());
                }
            }
        });

    }
}
