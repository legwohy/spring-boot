package com.cobra.myTomcat;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 客户端的响应数据 这里简单的客户端访问静态页面 只需将静态页面的内容返回 即可完成任务
 */
public class HttpResponse
{
    private OutputStream os;
    public HttpResponse (OutputStream os){
        this.os = os;
    }

    /**
     * 根据访问路径返回响应的页面 默认是在根路径下
     * param uri /htmlfile/login.html
     */
    public void writeFile(String uri) throws IOException
    {
        // 拼装头部
        StringBuffer sb = new StringBuffer();
        sb.append("HTTP/1.1 200 OK\n");
        sb.append("Content-Type:text/html;charset=UTF-8\n");
        sb.append("\r\n");
        os.write(sb.toString().getBytes());

        // 读取文件 根路径
        String path = this.getClass().getResource("/").getPath()+uri;
        FileInputStream fis = new FileInputStream(path);
        byte[] bytes = new byte[1024];// 每次读取1kb
        int len = 0;
        while ((len = fis.read(bytes))!= -1)
        {
            os.write(bytes,0,len);// 循环写入
        }

        os.flush();
        os.close();

    }


}
