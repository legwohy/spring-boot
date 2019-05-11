package com.cobra.myTomcat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装客户端的请求 主要是InputStream
 *  1、解析uri(https://www.w3cschool.cn/java/ http/1.1中的 /java)
 *  2、打印所有请求信息(放在构造方法中)
 */
public class HttpRequest
{
    private String uri;// /file/login.html
    public String getUri(){
        return uri;
    }

    private Map<String,String> parameterMap = new HashMap<>();
    public String getParamter(String key)
    {
        return parameterMap.get(key);
    }

    public HttpRequest (InputStream is)throws IOException
    {
        resolveRequest(is);
    }

    /**
     * 满足一致性 不要全部写在构造内 解析出uri
     *      1、GET uri HTTP/1.1
     *      2、host: hostName:port
     *      3、。。。
     *
     *      4、k1=v1&k2=v2
     * @param is
     */
    private void resolveRequest(InputStream is)throws IOException
    {
        // 读取流
        byte[] bytes = new byte[1024];// 每次读取1kb
        int len = is.read(bytes);
        if(len > 0)
        {
            String msg = new String(bytes,0,len);
            System.out.println("客户端请求信息 \t"+msg+"\t===================");

            // 解析uri
            //uri = msg.substring(msg.indexOf("/"),msg.indexOf("HTTP/1.1")-1);

            parseRequestToUri(msg);

            if(isPost(msg)){
                parseRequestToMap(msg);
            }




        }else {
            System.out.println("......Bad Request");
        }

    }

    private boolean isPost(String msg)
    {
        if(msg.startsWith("POST"))
        {
            return true;
        }
        return false;

    }

    /**
     * 解析uri
     * @param msg
     * @return
     */
    private void parseRequestToUri(String msg)
    {
        int startIndex = 0;
        int endIndex = msg.indexOf("HTTP/1.1")-1;
        if(msg.startsWith("GET")){
            startIndex = msg.indexOf("GET")+4;
        }else if(msg.startsWith("POST")){
            startIndex = msg.indexOf("GET")+5;
        }
        uri = msg.substring(startIndex,endIndex);

    }

    private void parseRequestToMap(String msg)
    {
        String parameter = msg.substring(msg.lastIndexOf("\n")+1);
        String[] keyPairs = parameter.split("&");
        System.out.println("--->>>"+ Arrays.toString(keyPairs));
        for (String keyPair:keyPairs)
        {
            String[] kv = keyPair.split("=");
            System.out.println("--->>>"+ Arrays.toString(kv));

            parameterMap.put(kv[0],kv[1]);
        }


    }


}
