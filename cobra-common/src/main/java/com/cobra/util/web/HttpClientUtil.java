package com.cobra.util;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.XML;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * http工具学习
 *
 */
@Slf4j
public class HttpClientUtil
{
    public final static String CHARSET = "UTF-8";

    private HttpClientUtil(){}

    public final static int DEFAULT_REQUEST_TIMEOUT = 6 * 1000;//默认的请求超时时间，6秒
    public final static int DEFAULT_RESPONSE_TIMEOUT = 6 * 1000;//默认的响应超时时间，6秒

    private static final int DEFAULT_CONNECT_REQUEST_TIMEOUT = 1000; //默认从连接池中获取连接的超时时间,1秒

    private static final int DEFAULT_MAX_PER_ROUTE = 150;

    private static final int DEFAULT_MAX_TOTAL = 300;

    private static PoolingHttpClientConnectionManager connectionManager;

    private static RequestConfig requestConfig;

    static
    {//bean初始化
        if (connectionManager == null) {
            SSLContext sslContext = SSLContexts.createDefault();

            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException{}

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException{}

                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            };

            try
            {
                sslContext.init(null, new TrustManager[] {tm}, null);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            X509HostnameVerifier hostnameVerifier = new AllowAllHostnameVerifier();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

            Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslsf)
                            .build();
            connectionManager = new PoolingHttpClientConnectionManager(r);
            // 整个连接池最大连接数
            connectionManager.setMaxTotal(DEFAULT_MAX_PER_ROUTE);
            // 每路由最大连接数，默认值是2
            connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_TOTAL);
            requestConfig = RequestConfig.custom()
                            .setConnectionRequestTimeout(DEFAULT_REQUEST_TIMEOUT)
                            .setConnectTimeout(DEFAULT_CONNECT_REQUEST_TIMEOUT).setSocketTimeout(DEFAULT_RESPONSE_TIMEOUT)
                            .build();
        }
    }

    /**
     * @param url
     * @param connTimeout 单位是毫秒
     * @param reqTimeout 单位是毫秒
    //	 * @param content http协议的报文体
     * @return
     */
    public static String sendByPost(String url, int connTimeout, int reqTimeout, HttpEntity entity, List<Header> headers)
    {
        HttpPost httpPost = null;
        httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        return HttpClientUtil.sendRequest(httpPost, connTimeout, reqTimeout, entity, headers, true);
    }

    public static String sendByPostWithoutCheck(String url, int connTimeout, int reqTimeout, HttpEntity entity, List<Header> headers)
    {
        HttpPost httpPost = null;
        httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        return HttpClientUtil.sendRequestWithoutCheck(httpPost, connTimeout, reqTimeout, entity, headers, true);

    }

    /**
     * 公共方法
     * @param url
     * @param bodyParam
     * @param mediaType
     * @return
     */
    public static String sendByPost(String url,  Map<String, String> bodyParam, Map<String, String> headerParam,String mediaType){
        HttpEntity resEntity = null;
        List<Header> headers = new ArrayList<>();
        if(null != headerParam && !headerParam.isEmpty()){
            for (Map.Entry<String,String> en:headerParam.entrySet()){
                headers.add(new BasicHeader(en.getKey(),en.getValue()));
            }
        }
        if(ContentType.APPLICATION_FORM_URLENCODED.getMimeType().equals(mediaType)){
            List<NameValuePair> paraList = Lists.newArrayList();
            for (String key : bodyParam.keySet()) {
                paraList.add(new BasicNameValuePair(key, bodyParam.get(key)));
            }
            headers.add(new BasicHeader("Content-Type","application/x-www-form-urlencoded"));
            resEntity = new UrlEncodedFormEntity(paraList, Consts.UTF_8);
        }else if(ContentType.APPLICATION_XML.equals(mediaType)){
            headers.add(new BasicHeader("Content-Type","application/xml"));
            String xml = "<xml version=\"1.0\" encoding=\"UTF-8\"?>"+ org.json.XML.toString(bodyParam);
            resEntity = new StringEntity(xml,ContentType.create(ContentType.APPLICATION_XML.getMimeType()));
        }else {
            // 默认json
            headers.add(new BasicHeader("Content-Type","application/json"));
            resEntity = new StringEntity(JSON.toJSONString(bodyParam),ContentType.create(ContentType.APPLICATION_JSON.getMimeType()));
        }

        return HttpClientUtil.sendByPost(url, DEFAULT_CONNECT_REQUEST_TIMEOUT, DEFAULT_REQUEST_TIMEOUT, resEntity, headers);
    }



    public static String sendByPostWithoutCheck(String url, int connTimeout, int reqTimeout, String content, String contentType)
    {
        StringEntity resEntity = new StringEntity(content, Consts.UTF_8);
        List<Header> headers = new ArrayList<>();
        Header header = null;
        if (StringUtils.isNotEmpty(contentType))
        {
            header = new BasicHeader("Content-type", contentType);
            headers.add(header);
        }
        return HttpClientUtil.sendByPostWithoutCheck(url, connTimeout, reqTimeout, resEntity, headers);

    }





    /**
     * @param url
     * @param connTimeout  单位是毫秒
     * @param reqTimeout 单位是毫秒
     * @param param
     * @return
     */
    public static String sendByGet(String url, int connTimeout, int reqTimeout, Map<String, String> param)
    {
        return HttpClientUtil.sendByGet(url, connTimeout, reqTimeout, param, true);
    }

    /**
     * @param url
     * @param connTimeout  单位是毫秒
     * @param reqTimeout 单位是毫秒
     * @param param
     * @return
     */
    public static String sendByQccGet(String url, int connTimeout, int reqTimeout, Map<String, String> param, List<Header> headers)
    {
        return HttpClientUtil.sendByQccGet(url, connTimeout, reqTimeout, param, true, headers);
    }

    /**
     * @param url
     * @param connTimeout  单位是毫秒
     * @param reqTimeout 单位是毫秒
     * @param param
     * @param charset 编码格式
     * @return
     * @throws Exception
     */
    public static String sendByGetEncode(String url, int connTimeout, int reqTimeout, Map<String, String> param, String charset) throws Exception
    {
        return HttpClientUtil.sendByGetEncode(url, connTimeout, reqTimeout, param, true, charset);
    }

    public static String sendByGet(String url, int connTimeout, int reqTimeout, Map<String, String> param, boolean checkResponseStatus)
    {
        HttpGet httpGet = null;
        StringBuffer paramUrl = new StringBuffer();
        if (url.indexOf("?") == -1)
        {
            paramUrl.append("?");
        }
        for (String key : param.keySet())
        {
            paramUrl.append(key).append("=")
                            .append(param.get(key)).append("&");
        }

        String paramUrlStr = paramUrl.toString();
        String newParamUrl = paramUrlStr;
        if (newParamUrl.endsWith("&"))
        {
            newParamUrl = paramUrlStr.substring(0, paramUrlStr.length() - 1);
        }
        httpGet = new HttpGet(url + newParamUrl);
        return HttpClientUtil.sendRequest(httpGet, connTimeout, reqTimeout, null, null, checkResponseStatus);
    }

    public static String sendByQccGet(String url, int connTimeout, int reqTimeout, Map<String, String> param, boolean checkResponseStatus, List<Header> headers)
    {
        HttpGet httpGet = null;
        StringBuffer paramUrl = new StringBuffer();
        if (url.indexOf("?") == -1)
        {
            paramUrl.append("?");
        }
        for (String key : param.keySet())
        {
            paramUrl.append(key).append("=")
                            .append(param.get(key)).append("&");
        }

        String paramUrlStr = paramUrl.toString();
        String newParamUrl = paramUrlStr;
        if (newParamUrl.endsWith("&"))
        {
            newParamUrl = paramUrlStr.substring(0, paramUrlStr.length() - 1);
        }
        httpGet = new HttpGet(url + newParamUrl);
        return HttpClientUtil.sendRequest(httpGet, connTimeout, reqTimeout, null, headers, checkResponseStatus);
    }

    public static String sendByGetEncode(String url, int connTimeout, int reqTimeout, Map<String, String> params, boolean checkResponseStatus, String charset) throws Exception{
        HttpGet httpGet = null;
        if (params != null && !params.isEmpty())
        {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                if (entry.getValue() != null)
                {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            if (StringUtils.isEmpty(charset))
            {
                charset = CHARSET;
            }
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
        }
        httpGet = new HttpGet(url);
        return HttpClientUtil.sendRequest(httpGet, connTimeout, reqTimeout, null, null, checkResponseStatus);
    }

    /**
     * @param httpRequest
     * @param connTimeout 连接超时时间，单位是毫秒
     * @param reqTimeout  响应超时时间，单位是毫秒
     * @param entity
     * @param headers
     * @return
     */
    private static String sendRequest(HttpRequestBase httpRequest, int connTimeout, int reqTimeout, HttpEntity entity, List<Header> headers, boolean checkResponseStatus)
    {
        RequestConfig config = RequestConfig.custom()
                        .setConnectionRequestTimeout(DEFAULT_CONNECT_REQUEST_TIMEOUT)
                        .setConnectTimeout(connTimeout)
                        .setSocketTimeout(reqTimeout).build();
        httpRequest.setConfig(config);
        httpRequest.setHeader("User-Agent", "okHttp");
        if (httpRequest instanceof HttpEntityEnclosingRequestBase)
        {
            checkArgument(null != entity, "HttpEntity请求体不能为空");
            ((HttpEntityEnclosingRequestBase)httpRequest).setEntity(entity);
        }
        if (null != headers)
        {
            //添加请求头
            for (Header header : headers)
            {
                httpRequest.addHeader(header);
            }
        }
        CloseableHttpClient httpClient = getClient();

        CloseableHttpResponse response = null;
        String resString = null;
        try
        {
            response = httpClient.execute(httpRequest);
            HttpEntity resEntity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            resString = EntityUtils.toString(resEntity, "UTF-8");
            //很多人调用接口时不记录日志，因此在这里以error级别打印
            log.info("状态码:{},URL:{},请求内容:{},响应内容:{}", statusCode, httpRequest.getURI(), resString, resString);
            if (checkResponseStatus)
            {
                checkArgument(Objects.equal(statusCode, HttpStatus.SC_OK), "响应码状态不是200");
            }

            // httpPost.clone();
            return resString;
        }
        catch (Exception e)
        {
            log.error("调用HTTP post 服务异常，{}", e);
            //return "";
            throw new RuntimeException(resString, e);
        }
    }

    private static String sendRequestWithoutCheck(HttpRequestBase httpRequest, int connTimeout, int reqTimeout, HttpEntity entity, List<Header> headers, boolean checkResponseStatus)
    {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(connTimeout)
                        .setSocketTimeout(reqTimeout).build();
        httpRequest.setConfig(config);
        httpRequest.setHeader("User-Agent", "okHttp");
        if (httpRequest instanceof HttpEntityEnclosingRequestBase)
        {
            checkArgument(null != entity, "HttpEntity请求体不能为空");
            ((HttpEntityEnclosingRequestBase)httpRequest).setEntity(entity);
        }
        if (null != headers)
        {
            //添加请求头
            for (Header header : headers)
            {
                httpRequest.addHeader(header);
            }
        }
        CloseableHttpClient httpClient = getClient();

        CloseableHttpResponse response = null;
        String resString = null;
        try
        {
            response = httpClient.execute(httpRequest);
            HttpEntity resEntity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            resString = EntityUtils.toString(resEntity, "UTF-8");
            //很多人调用接口时不记录日志，因此在这里以error级别打印
            log.info("状态码:{},URL:{},请求内容:{},响应内容:{}", statusCode, httpRequest.getURI(), resString, resString);

            // httpPost.clone();
            return resString;
        }
        catch (Exception e)
        {
            log.error("调用HTTP post 服务异常，{}", e);
            //return "";
            throw new RuntimeException(resString, e);
        }
    }

    public static CloseableHttpClient getClient()
    {
        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                        .setConnectionManager(connectionManager)
                        .build();
        return closeableHttpClient;
    }



    /**
     * 请求参数进行编码处理
     * @param url  请求路径
     * @param param 单位是毫秒
     * @param charset 编码格式(默认UTF-8)
     * @return
     * @throws Exception
     */
    public static String sendByGetEncode(String url, Map<String, String> param, String charset) throws Exception
    {

        return sendByGetEncode(url, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT, param, charset);
    }


    /**
     * 上传图片
     * @param urlStr
     * @param textMap 表单参数<key,value>
     * @param fileMap <文件名,文件地址>
     * @param contentType 没有传入文件类型默认采用application/octet-stream
     * contentType非空采用filename匹配默认的图片类型
     * @return 返回response数据
     */
    @SuppressWarnings("rawtypes")
    public static String uploadFile(String urlStr, Map<String, String> textMap, Map<String, String> fileMap, String contentType) {
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
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
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
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes(CHARSET));
            }
            // file
            if (fileMap != null) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String fileName = (String) entry.getKey();
                    String filePath = (String) entry.getValue();
                    if (filePath == null) {
                        continue;
                    }

                    //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                    //contentType = new MimetypesFileTypeMap().getContentType(file);
                    contentType=null;
                    //contentType非空采用filename匹配默认的图片类型
                    if (StringCommonUtils.isEmpty(contentType)) {
                        contentType = "application/octet-stream";
                    }else {
                        if (filePath.endsWith(".png")) {
                            contentType = "image/png";
                        }else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".jpe")) {
                            contentType = "image/jpeg";
                        }else if (filePath.endsWith(".gif")) {
                            contentType = "image/gif";
                        }else if (filePath.endsWith(".ico")) {
                            contentType = "image/image/x-icon";
                        }
                    }

                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + fileName + "\"; filename=\"" + filePath + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes(CHARSET));

                    // 写入文件
                    URL ossurl = new URL(filePath);
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
            // 成功
            if (HttpStatus.SC_OK==responseCode) {
                // 读取返回数据
                StringBuffer strBuf = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),CHARSET));

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
            log.info("返回请求参数:{},msg:{}", responseCode , res);
        } catch (Exception e) {
            log.error("上传文件错误:{}" , urlStr);
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
