package com.cobra.util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public static final String CHARSET = "UTF-8";

    public static String doGet(String apiURL , List<Header> headers){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(apiURL);
        String body = null;
        try {

            // 建立一个NameValuePair数组，用于存储欲传送的参数
            httpGet.addHeader("Content-type","application/json; charset=utf-8");
            httpGet.setHeader("Accept", "application/json");
            for(Header header : headers) {
                httpGet.addHeader(header);
            }

            HttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Method failed:" + response.getStatusLine());
            }

            // Read the response body
            body = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            // 网络错误
            logger.error("链接错误:" + e.getMessage() , e);
        }
        return body;
    }

    public static String postJson(String apiURL, String params){
        logger.info("Post url:" + apiURL);
        logger.info("Post params:" + params);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(apiURL);
        String body = null;
//      logger.info("parameters:" + parameters);
        try {

            // 建立一个NameValuePair数组，用于存储欲传送的参数
            httpPost.addHeader("Content-type","application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            if (params != null) {
                // 设置字符集
                StringEntity stringEntity = new StringEntity(params, "utf-8");
                // 设置参数实体
                httpPost.setEntity(stringEntity);
            }
//          httpPost.setEntity(new SerializableEntity(parameters, Charset.forName("UTF-8")));

            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                logger.error("Method failed:" + response.getStatusLine());
            }

            // Read the response body
            body = EntityUtils.toString(response.getEntity());

            logger.info("Post response:" + body);

        } catch (Exception e) {
            // 网络错误
            e.printStackTrace();
        }
        return body;
    }

    /**
     * 扫描图片信息   上传图片到oss后 face验证修改
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @param contentType 没有传入文件类型默认采用application/octet-stream
     * contentType非空采用filename匹配默认的图片类型
     * @return 返回response数据
     */
    @SuppressWarnings("rawtypes")
    public static String formUploadImageNew(String urlStr, Map<String, String> textMap, Map<String, String> fileMap, String contentType) {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------123821742118716";
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes(CHARSET));
            }
            // file
            if (fileMap != null) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    String filename = inputValue;

                    //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                    //contentType = new MimetypesFileTypeMap().getContentType(file);
                    contentType=null;
                    //contentType非空采用filename匹配默认的图片类型
                    if(!"".equals(contentType)){
                        if (filename.endsWith(".png")) {
                            contentType = "image/png";
                        }else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
                            contentType = "image/jpeg";
                        }else if (filename.endsWith(".gif")) {
                            contentType = "image/gif";
                        }else if (filename.endsWith(".ico")) {
                            contentType = "image/image/x-icon";
                        }
                    }
                    if (contentType == null || "".equals(contentType)) {
                        contentType = "application/octet-stream";
                    }
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"; filename=\"" + filename
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes(CHARSET));
                    logger.info("inputValue-----------------"+inputValue);
                    URL ossurl = new URL(inputValue);
                    DataInputStream in = new DataInputStream(ossurl.openStream());
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            int responseCode = conn.getResponseCode();
            if (responseCode==200) {
                // 读取返回数据
                StringBuffer strBuf = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(),CHARSET));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line).append("\n");
                }
                res = strBuf.toString();
                reader.close();
                reader = null;
            }else{
                StringBuffer error = new StringBuffer();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        conn.getErrorStream(),CHARSET));
                String line1 = null;
                while ((line1=bufferedReader.readLine())!=null) {
                    error.append(line1).append("\n");
                }
                res=error.toString();
                bufferedReader.close();
                bufferedReader=null;
            }
            logger.info("返回请求参数:" + responseCode + " msg=" + res);
        } catch (Exception e) {
            logger.info("发送POST请求出错。" + urlStr);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }
}
