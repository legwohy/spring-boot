package com.cobra.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * 测试3
 */
public class OkHttpUtil {
    private static Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String post(String url, String json) {
        String result = "";
        Response response = null;
        Request request = null;
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = RequestBody.create(JSON, json);
            request = new Request.Builder().url(url).post(formBody).build();

            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            String msg = MessageFormat.format("请求异常,url:{0},params:{1},response:{2}", url, json, result);
            logger.error(msg);
            e.printStackTrace();
        }
        return result;
    }


}
