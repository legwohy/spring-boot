package com.cobra.service;


import com.alibaba.fastjson.JSONObject;
import com.cobra.util.HttpUtil;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class FaceService {
    private String apiKey = "LGq9uVn4OQnzFgNlSQ_S8-706xmcq5ij";
    private String apiSecret = "NFzaxWUh4_o3st_Kr-kCOb53ZIbSbJ0V";
    private String cardUrl = "https://api.faceid.com/faceid/v1/ocridcard";
    private String faceUrl = "https://api.megvii.com/faceid/v2/verify";// 人脸
    private Logger logger = LoggerFactory.getLogger(FaceService.class);
    static OkHttpClient client = new OkHttpClient();

    static {
        client.newBuilder().connectTimeout(3, TimeUnit.SECONDS);
        client.newBuilder().writeTimeout(5, TimeUnit.SECONDS);
        client.newBuilder().readTimeout(20, TimeUnit.SECONDS);

    }

    /**
     * 证件识别
     * @throws Exception
     */
   public void testScanningCard() throws Exception {
        System.out.println("----------->校验身份证开始");

        //--------------------okHttp------------
       OkHttpClient client = new OkHttpClient();
        File file = new File("E:/temp/test4.jpg");

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM) // 表单
                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .addFormDataPart("api_key", apiKey)
                .addFormDataPart("api_secret", apiSecret)
                .addFormDataPart("legality", "1")
                .build();// 请求体

       // 请求参数设置
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)")
                .header("Connection", "Keep-Alive")
                .addHeader("Content-Type","Multipart/form-data")
                .post(requestBody)
                .url(cardUrl)
                .build();


        // 回调或响应  本回调为异步，因此需要在主线程(不要在单元测试中启动)中开启
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("---------->回调失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println(response);
                System.out.println("--------回调成功:"+response.body().string());

            }
        });
    }




    public static OkHttpClient getConnection() throws Exception {
        SSLContext ctx = SSLContext.getInstance("SSL");
        ctx.init(new KeyManager[0], new TrustManager[]{new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        }}, new SecureRandom());
        client.sslSocketFactory();
        client.newBuilder().sslSocketFactory(ctx.getSocketFactory());

        client.newBuilder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        return client;
    }


    /**
     * 有源比对
     */
    public void checkFace() throws IOException {
        System.out.println("-------------->识别开始");

        File file = new File("E:\\temp\\test2.jpg");
        // okHttp3--------------------------
        OkHttpClient client = new OkHttpClient();
        // 请求体
        RequestBody fileBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("api_key", apiKey)
                .addFormDataPart("api_secret", apiSecret)
                .addFormDataPart("comparison_type", "1") // 有源比对
                .addFormDataPart("face_image_type", "raw_image")// 对比图片的类型
                .addFormDataPart("idcard_name", "朱民主")// 有源比对必选参数
                .addFormDataPart("idcard_number","421126199408066951") // 有源比对必选参数
                .addFormDataPart("fail_when_multiple_faces","0") // 多张图片取最大脸继续比对
                .addFormDataPart("face_quality_threshold","0") // 人脸图像质量低于30 返回LOW_QUALITY错误
                .addFormDataPart("multi_oriented_detection","1") // 允许旋转检测
                .addFormDataPart("image",file.getName(),RequestBody.create(MediaType.parse("application/octet-stream"),file))
                .build();

        // 请求
        Request request = new Request.Builder()
                .url(faceUrl)
                .post(fileBody)
                .build();

        Response rs = client.newCall(request).execute();
        System.out.println("----------->rs="+rs.body().string());


    }

    public void test1() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 200);
        jsonObject.put("msg", "测试");

        if (!"200".equals(jsonObject.get("code").toString())) {
            System.out.println("------->" + jsonObject.get("msg"));
        }
        if ("200".equals(jsonObject.get("code"))) {
            System.out.println("策划你哥哥");
        }
    }


    public void checkIdCardZ() {
        System.out.println("---------->校验身份证正面开始");
        String apiKey = "LGq9uVn4OQnzFgNlSQ_S8-706xmcq5ij";
        String apiSecret = "NFzaxWUh4_o3st_Kr-kCOb53ZIbSbJ0V";
        String faceApiBaseUrl = "https://api.faceid.com";
        Map<String, String> textMap = new HashMap<>(), fileMap = new HashMap<>();
        // 可以设置多个input的name，value
        textMap.put("api_key", apiKey);
        textMap.put("api_secret", apiSecret);
        // 返回身份证照片合法性检查结果，值只取“0”或“1”。“1”：返回； “0”：不返回。默认“0”。
        textMap.put("legality", "1");
        fileMap.put("image", "file:///E:\\temp\\test4.jpg");// 本地图片  file:///F:/img_copy/sb.png

        System.out.println("请求参数：" + textMap.toString() + fileMap.toString());

        String idcardResultStr = HttpUtil.formUploadImageNew(faceApiBaseUrl + "/faceid/v1/ocridcard", textMap, fileMap, "");

        System.out.println("返回结果:" + idcardResultStr);


    }
}
