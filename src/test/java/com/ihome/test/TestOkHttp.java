package com.ihome.test;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import okio.BufferedSink;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestOkHttp {
    Logger logger = LoggerFactory.getLogger(TestOkHttp.class);

    @Test public void http() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)  // 连接超时
                .writeTimeout(20,TimeUnit.SECONDS)  // 写入时间
                .readTimeout(20,TimeUnit.SECONDS)  // 读取时间
                .build();// 构建一个对象

        String url = "https://blog.csdn.net/u013651026/article/details/79738059";
        final Request request = new Request.Builder()
                .url(url) // url
                .get() // 默认是get请求
                .addHeader("content-type", "application/json;charset:utf-8")
                .addHeader("User-Agent","android") // 请求头 多个
                .build();// 构建一个对象

        String china = URLEncoder.encode("中文","UTF-8");// 若输入中文需要编码

        // 创建call
        Call call = client.newCall(request);

        // 加入异步队列
        call.enqueue(new Callback() {

            // 请求错误回调函数
            @Override
            public void onFailure(Call call, IOException e) {
                logger.info("--------->连接失败");
            }

            // 异步请求 非主线程
            @Override
            public void onResponse(Call call, Response response) throws IOException {
               if(response.isSuccessful()){
                   logger.info("-------->"+response.body().string());
               }
            }
        });

        // form表单提交
        FormBody formBody = new FormBody.Builder()
                .add("name","jack")
                .add("gender","female")
                .build();

        // json提交
        JSONObject json = JSONObject.parseObject("");
        RequestBody requestBody = FormBody
                .create(MediaType.parse("application/json;charset=utf-8"),json.toJSONString());

    }


}
