package com.cobra.util.web;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

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

    /**
     * 上传文件
     * @param url
     * @param textMap
     * @param fileMap
     * @return
     * @throws Exception
     */
    public String postForForm(String url,Map<String,String> textMap,Map<String,String> fileMap) throws Exception {

        //--------------------okHttp------------
        OkHttpClient client = new OkHttpClient();
        File file = new File(fileMap.get("filePath"));

        RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM) // 表单
                        .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                        .addFormDataPart("key1", textMap.get("key1"))
                        .addFormDataPart("key2", textMap.get("key2"))
                        .build();// 请求体

        // 请求参数设置
        Request request = new Request.Builder()
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)")
                        .header("Connection", "Keep-Alive")
                        .addHeader("Content-Type","Multipart/form-data")
                        .post(requestBody)
                        .url(url)
                        .build();


        // 回调或响应  本回调为异步，因此需要在主线程(不要在单元测试中启动)中开启
        Call call = client.newCall(request);
        Response response = call.execute();
        if(response.isSuccessful()){
            return response.body().string();
        }

        return null;

    }


}
