package com.cobra.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 测试3
 */
public class OkHttpUtil {
    private static Logger logger = LoggerFactory.getLogger(OkHttpUtil.class);
    private static OkHttpClient client = null;
    static {
        if(client == null){
            client = new OkHttpClient();
        }

    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String post(String url, String json) {
        String result = "";
        Response response = null;
        Request request = null;
        try {

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

    /**
     * get请求 参数拼接在url中
     * @param url
     * @return
     */
    public static String get(String url, Map<String,String> params){
        String result = "";
        Response response = null;
        Request request = null;
        try {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String,String> en :params.entrySet()){
                builder.append(en.getKey()).append("=").append(en.getValue()).append("&");
            }
            url = url.concat("&").concat(builder.toString());

            logger.info("------->请求参数："+url);
            request = new Request.Builder().url(url).get().build();

            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            }

            logger.info("----------->响应值："+result);
        } catch (Exception e) {
            String msg = MessageFormat.format("请求异常,url:{0},params:{1},response:{2}", url, result);
            logger.error(msg);
            e.printStackTrace();
        }
        return result;

    }


}
