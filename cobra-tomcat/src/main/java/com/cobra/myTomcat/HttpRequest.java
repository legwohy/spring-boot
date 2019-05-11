package com.cobra.myTomcat;

import java.io.IOException;
import java.io.InputStream;

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

    public HttpRequest (InputStream is)throws IOException
    {
        resolveRequest(is);
    }

    /**
     * 满足一致性 不要全部写在构造内 解析出uri
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
            System.out.println("读取到的信息---"+msg+"---");

            // 解析uri
            uri = msg.substring(msg.indexOf("/"),msg.indexOf("HTTP/1.1")-1);
            System.out.println("...uri=..."+uri);


        }else {
            System.out.println("......Bad Request");
        }

    }


}
