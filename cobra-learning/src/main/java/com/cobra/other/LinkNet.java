package com.cobra.other;

import org.junit.Test;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @auther: lg
 * @date: 2019/3/13 18:43
 * @description:
 */
public class LinkNet
{
    String charset = "UTF-8";

    public static void main1(String[] args) throws IOException
    {
        //如果需要进行https的请求只需要换成如下一句（https的默认端口为443，http默认端口为80）
        Socket socket = SSLSocketFactory.getDefault().createSocket("restapi.amap.com", 443);
        //Socket socket = new Socket("restapi.amap.com", 80);

        //获取输入流，即从服务器获取的数据
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //获取输出流，即我们写出给服务器的数据
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        //使用一个线程来进行读取服务器的响应
        new Thread()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    String line = null;
                    try
                    {
                        while ((line = bufferedReader.readLine()) != null)
                        {
                            System.out.println("recv : " + line);
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        bufferedWriter.write("GET /v3/weather/weatherInfo?city=%E9%95%BF%E6%B2%99&key=13cb58f5884f9749287abbead9c658f2 HTTP/1.1\r\n");
        bufferedWriter.write("Host: restapi.amap.com\r\n\r\n");
        bufferedWriter.flush();

    }

    @Test
    public void socketForGet() throws IOException
    {
        //如果需要进行https的请求只需要换成如下一句（https的默认端口为443，http默认端口为80）
        //Socket socket = SSLSocketFactory.getDefault().createSocket("restapi.amap.com", 443);
        Socket socket = new Socket("127.0.0.1", 8081);

        //获取输出流，即我们写出给服务器的数据
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("GET /borrowUser/login?name=jack&sex=female HTTP/1.0\r\n");
        writer.write("Host 127.0.0.1\r\n\r\n");

        writer.flush();

        //获取输入流，即从服务器获取的数据
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line = new String();
        try
        {
            while ((line = bufferedReader.readLine()) != null)
            {
                sb.append(line).append("\r\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(sb);

    }

    /**
     * 读取到换行符时认为header读取完毕
     * @throws IOException
     */
    @Test
    public void socketForPost() throws IOException
    {

        //如果需要进行https的请求只需要换成如下一句（https的默认端口为443，http默认端口为80）
        //Socket socket = SSLSocketFactory.getDefault().createSocket("restapi.amap.com", 443);
        Socket socket = new Socket("127.0.0.1", 8081);

        String params = URLEncoder.encode("name", charset) + "=" + URLEncoder.encode("jack", charset) + "&"
                        + URLEncoder.encode("sex", charset) + "=" + URLEncoder.encode("female", charset);
        //获取输出流，即我们写出给服务器的数据
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("POST /borrowUser/login HTTP/1.0\r\n");
        writer.write("Host: 127.0.0.1\r\n");
        writer.write("Content-Length: " + params.length() + "\r\n");
        writer.write("Content-Type: application/x-www-form-urlencoded\r\n");
        writer.write("\r\n");
        writer.write(params);
        writer.flush();

        //获取输入流，即从服务器获取的数据
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
        StringBuffer sb = new StringBuffer();
        String line = new String();
        try
        {
            while ((line = bufferedReader.readLine()) != null)
            {

                sb.append(line).append("\r\n");

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // 取body
        String[] arr = sb.toString().split("\r\n\r\n");
        System.out.println(arr[arr.length - 1]);

        socket.close();

    }

    @Test
    public void httpForGet() throws Exception
    {

        String u1 = "http://127.0.0.1:8081/borrowUser/login?name=jack&sex=female";
        HttpURLConnection conn = (HttpURLConnection)new URL(u1).openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);

        // 客户端输入流 服务器响应内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = new String();
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null)
        {
            buffer.append(line);
        }
        reader.close();
        System.out.println(buffer);

    }

    @Test
    public void httpForPost() throws Exception
    {
        //建立连接
        HttpURLConnection client = (HttpURLConnection)new URL("http://localhost:8081/borrowUser/login").openConnection();

        //设置参数
        client.setDoOutput(true);     //需要输出
        client.setDoInput(true);      //需要输入
        client.setUseCaches(false);   //不允许缓存
        client.setRequestMethod("POST");      //设置POST方式连接

        //设置请求属性
        client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        client.setRequestProperty("Charset", "UTF-8");

        //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
        client.connect();

        //建立输入流，向指向的URL传入参数 流传递参数 放在body里面
        String params = "name=jack&sex=female&file=sb";
        OutputStream writer = client.getOutputStream();
        writer.write(params.getBytes(charset));
        writer.flush();
        writer.close();

        //获得响应状态
        int resultCode = client.getResponseCode();
        if (HttpURLConnection.HTTP_OK == resultCode)
        {
            StringBuffer sb = new StringBuffer();
            String readLine = new String();
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
            while ((readLine = responseReader.readLine()) != null)
            {
                sb.append(readLine).append("\n");
            }
            responseReader.close();
            System.out.println(sb.toString());
        }
    }

    /**
     * 1、普通参数
     * --boundary
     * Content-Disposition:form-data;name=key
     *
     * value
     *
     * 2、文件参数
     * --boundary
     * Content-Disposition:form-data;name=fileName;filename=fileName.png
     * Content-Type:application/octet-stream
     *
     * fileInputStream
     *
     * 3、结尾
     * --boundary--
     *
     * @throws Exception
     */
    @Test
    public void httpForUpload() throws Exception
    {
        String prefix = "--";
        String boundary = "==========1234567890998653=============";
        String linend = "\r\n";
        //建立连接
        HttpURLConnection client = (HttpURLConnection)new URL("http://localhost:8081/borrowUser/login").openConnection();

        //设置参数
        client.setDoOutput(true);
        client.setDoInput(true);
        client.setUseCaches(false);
        client.setRequestMethod("POST");
        client.setReadTimeout(30000);

        //上传文件 必须是multipart/form-data 且必须带分隔符(分割多个post请求的信息)
        client.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        client.setRequestProperty("Charset", charset);
        client.setRequestProperty("Connection", "Keep-Alive");

        //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
        client.connect();

        StringBuilder txt = new StringBuilder();

        // 建立输入流，向指向的URL传入参数 流传递参数 放在body里面 boundary的--不能省
        OutputStream writer = new DataOutputStream(client.getOutputStream());
        txt.append(prefix + boundary + linend);
        txt.append("Content-Disposition:form-data; name=\"name\"" + linend);
        txt.append(linend);
        txt.append("jack");
        txt.append(linend);

        txt.append(prefix + boundary + linend);
        txt.append("Content-Disposition:form-data; name=\"sex\"" + linend);
        txt.append(linend);
        txt.append("female");
        txt.append(linend);

        writer.write(txt.toString().getBytes());

        StringBuilder file = new StringBuilder();
        // 文件数据
        file.append(prefix + boundary + linend);
        file.append("Content-Disposition: form-data; name=\"file\"; filename=\"pic_1.png\"" + linend);
        file.append("Content-Type: application/octet-stream" + linend);
        file.append(linend);

        writer.write(file.toString().getBytes());

        InputStream fis = (new FileInputStream("F:\\0.png"));
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = fis.read()) != -1)
        {
            writer.write(bytes, 0, len);
        }
        fis.close();
        writer.write(linend.getBytes());

        // 请求结束
        writer.write((prefix + boundary + prefix + linend).getBytes());
        writer.flush();
        writer.close();

        // 获得响应状态
        int resultCode = client.getResponseCode();
        System.out.println("response code=" + resultCode);
        if (HttpURLConnection.HTTP_OK == resultCode)
        {
            String line = "";
            BufferedReader responseReader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
            while ((line = responseReader.readLine()) != null)
            {
                System.out.println(line + "\n");
            }
            responseReader.close();
        }
    }


    public static void main(String[] args)
    {
        String sb = "hell";
        System.out.println(">>>\n");
        System.out.println(">>>\r\n");
        System.out.println(">>>");
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException
    {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1)
        {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}
