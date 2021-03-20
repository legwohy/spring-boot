package com.cobra.util.web;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
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

    private HttpClientUtil()
    {
    }

    public final static int DEFAULT_REQUEST_TIMEOUT = 6 * 1000;//默认的请求超时时间，6秒
    public final static int DEFAULT_RESPONSE_TIMEOUT = 6 * 1000;//默认的响应超时时间，6秒

    private static final int DEFAULT_CONNECT_REQUEST_TIMEOUT = 1000; //默认从连接池中获取连接的超时时间,1秒

    private static final int DEFAULT_MAX_PER_ROUTE = 150;

    private static final int DEFAULT_MAX_TOTAL = 300;

    private static PoolingHttpClientConnectionManager connectionManager;

    private static RequestConfig requestConfig;

    static
    {//bean初始化
        if (connectionManager == null)
        {
            SSLContext sslContext = SSLContexts.createDefault();

            X509TrustManager tm = new X509TrustManager()
            {
                public void checkClientTrusted(X509Certificate[] xcs,
                                String string) throws CertificateException
                {
                }

                public void checkServerTrusted(X509Certificate[] xcs,
                                String string) throws CertificateException
                {
                }

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

    public static String sendByPostForZtx(String url, int connTimeout, int reqTimeout, Map<String, String> param, List<Header> headers)
    {
        List<NameValuePair> paraList = Lists.newArrayList();
        for (String key : param.keySet())
        {
            paraList.add(new BasicNameValuePair(key, param.get(key)));
        }
        HttpEntity resEntity =
                        new UrlEncodedFormEntity(paraList, Consts.UTF_8);
        return HttpClientUtil.sendByPost(url, connTimeout, reqTimeout, resEntity, headers);
    }

    public static String sendByPost(String url, Map<String, String> param)
    {
        return sendByPost(url, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT, param);
    }

    public static String sendByPost(String url, String content, String contentType)
    {
        return sendByPost(url, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT, content, contentType);
    }

    public static String sendByPost(String url, int connTimeout, int reqTimeout, String content, String contentType)
    {
        StringEntity resEntity = new StringEntity(content, Consts.UTF_8);
        List<Header> headers = new ArrayList<>();
        Header header = null;
        if (StringUtils.isNotEmpty(contentType))
        {
            header = new BasicHeader("Content-type", contentType);
            headers.add(header);
        }
        return HttpClientUtil.sendByPost(url, connTimeout, reqTimeout, resEntity, headers);
    }

    public static String sendByPost(String url, int connTimeout, int reqTimeout, String content,
                    String contentType, String headerK, String headerV)
    {
        StringEntity resEntity = new StringEntity(content, Consts.UTF_8);
        List<Header> headers = new ArrayList<>();
        Header header = null;
        if (StringUtils.isNotEmpty(contentType))
        {
            header = new BasicHeader("Content-type", contentType);
            headers.add(header);
        }
        if (StringUtils.isNotEmpty(headerK) && StringUtils.isNotEmpty(headerV))
        {
            header = new BasicHeader(headerK, headerV);
            headers.add(header);
        }
        return HttpClientUtil.sendByPost(url, connTimeout, reqTimeout, resEntity, headers);
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

    public static String sendByPostForZtx(String url, int connTimeout, int reqTimeout, Map<String, String> param, String contentType, String authorization)
    {

        List<Header> headers = new ArrayList<>();
        if (StringUtils.isNotEmpty(contentType))
        {

            Header header1 = new BasicHeader("Content-Type", contentType);
            Header header2 = new BasicHeader("Authorization", authorization);
            Header header3 = new BasicHeader("connection", "Keep-Alive");
            headers.add(header1);
            headers.add(header2);
            headers.add(header3);
        }

        return HttpClientUtil.sendByPostForZtx(url, connTimeout, reqTimeout, param, headers);

    }

    public static String sendByPost(String url, int connTimeout, int reqTimeout, String content)
    {
        StringEntity resEntity = new StringEntity(content, Consts.UTF_8);
        return HttpClientUtil.sendByPost(url, connTimeout, reqTimeout, resEntity, null);
    }

    public static String sendByPost(String url, int connTimeout, int reqTimeout, Map<String, String> param)
    {
        List<NameValuePair> paraList = Lists.newArrayList();
        for (String key : param.keySet())
        {
            paraList.add(new BasicNameValuePair(key, param.get(key)));
        }
        HttpEntity resEntity =
                        new UrlEncodedFormEntity(paraList, Consts.UTF_8);
        return HttpClientUtil.sendByPost(url, connTimeout, reqTimeout, resEntity, null);
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

    public static String sendByGetEncode(String url, int connTimeout, int reqTimeout, Map<String, String> params, boolean checkResponseStatus, String charset) throws Exception
    {
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
        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(DEFAULT_CONNECT_REQUEST_TIMEOUT).setConnectTimeout(connTimeout)
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
     * @param url
     * @param connTimeout  单位是毫秒
     * @param reqTimeout 单位是毫秒
     * @param param
     * @return
     */
    public static String sendByGet(String url, int connTimeout, int reqTimeout, Map<String, String> param, String interFace)
    {

        log.info("接口：{}请求数据源的超时时间为：{}，{}", interFace, connTimeout, reqTimeout);
        return HttpClientUtil.sendByGet(url, connTimeout, reqTimeout, param, true);
    }

    /**
     *
     * @param url
     * @param param
     * @return
     */
    public static String sendByPost(String url, Map<String, String> param, String interFace)
    {

        log.info("接口：{}请求数据源的超时时间为：{}，{}", interFace, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT);
        return sendByPost(url, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT, param);
    }

    /**
     * @param url
     * @param connTimeout
     * @param reqTimeout
     * @param content
     * @param contentType
     * @return
     */
    public static String sendByPost(String url, int connTimeout, int reqTimeout, String content, String contentType, String interFace)
    {
        StringEntity resEntity = new StringEntity(content, Consts.UTF_8);
        List<Header> headers = new ArrayList<>();
        Header header = null;
        if (StringUtils.isNotEmpty(contentType))
        {
            header = new BasicHeader("Content-type", contentType);
            headers.add(header);
        }

        log.info("接口：{}请求数据源的超时时间为：{}，{}", interFace, connTimeout, reqTimeout);
        return HttpClientUtil.sendByPost(url, connTimeout, reqTimeout, resEntity, headers);
    }

    /**
     *
     * @param url
     * @param content
     * @param contentType
     * @param interFace
     * @return
     */
    public static String sendByPost(String url, String content, String contentType, String interFace)
    {
        log.info("接口：{}请求数据源的超时时间为：{}，{}", interFace, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT);
        return sendByPost(url, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT, content, contentType);
    }

    /**
     * 带有一个header的post请求
     * @param url
     * @param content
     * @param contentType
     * @param interFace
     * @return
     */
    public static String sendByPost(String url, String content, String contentType, String headerK, String headerV, String interFace)
    {
        log.info("接口：{}请求数据源的超时时间为：{}，{}", interFace, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT);
        return sendByPost(url, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT, content, contentType, headerK, headerV);
    }

    public static String sendByPostWithoutCheck(String url, String content, String contentType, String interFace)
    {
        log.info("接口：{}请求数据源的超时时间为：{}，{}", interFace, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT);
        return sendByPostWithoutCheck(url, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT, content, contentType);
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
     * @param url  单位是毫秒
     * @param param 单位是毫秒
     * @return
     */
    public static String sendByQccGet(String url, Map<String, String> param, String interFace, List<Header> headers)
    {
        log.info("接口：{}请求数据源的超时时间为：{}，{}", interFace, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT);
        return sendByQccGet(url, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT, param, headers);
    }

    public static String sendByGet(String url, Map<String, String> param, String interFace, boolean checkResponseStatus)
    {
        log.info("接口：{}请求数据源的超时时间为：{}，{}", interFace, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT);
        return sendByGet(url, DEFAULT_REQUEST_TIMEOUT, DEFAULT_RESPONSE_TIMEOUT, param, checkResponseStatus);
    }
}
