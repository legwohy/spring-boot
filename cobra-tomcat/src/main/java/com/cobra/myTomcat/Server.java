package com.cobra.myTomcat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * tomcat的服务端 serverSocket 监听端口 并响应数据 响应头是必须的
 * uri做拦截 将会进入到自己写的业务代码
 */
public class Server
{
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(9090);// 监听端口 正常是可以配置的

        while (true)
        {
            System.out.println("服务端监听接口 9090.......");
            Socket socket = serverSocket.accept();// 建立连接
            // 客户端请求数据
            InputStream is = socket.getInputStream();
            HttpRequest request = new HttpRequest(is);

            System.out.println("uri="+request.getUri());
            System.out.println("userName="+request.getParamter("userName")+","+request.getParamter("pwd"));

            /*byte[] bytes = new byte[1024];// 每次读取1kb
            int len = is.read(bytes);
            if(len>0){
                System.out.println(new String(bytes,0,len));
            }*/


            // 客户端响应数据
            OutputStream os = socket.getOutputStream();
            HttpResponse response = new HttpResponse(os);
            String uri = request.getUri();
            if(isStatic(uri)){
                response.writeFile(uri.substring(1));// 去掉 /
            }else if(uri.endsWith(".action"))
            {
                new LoginServlet().server(request,response);

            }


            /*SimpleDateFormat formt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //构建一个响应头信息 200 ok
            StringBuffer sb=new StringBuffer();
            // 平凑响应头信息(必须)
            sb.append("HTTP/1.1 200 OK\n");
            sb.append("Content-Type: text/html;charset=UTF-8\n");
            sb.append("\r\n");
            //响应正文
            String html = "<html><head><title>欢迎各位同学！</title></head><body>当前时间："
                            +"<font size='14' color='blue'>"
                            + formt.format(new Date())
                            +"</font>"
                            + "<br/>服务器回复：<font size='14' color='blue'>大家今天有收获吗？</font></body></html>";
            //将响应头+正文 全部链接在一起
            sb.append(html);
            //os =  client.getOutputStream();
            os.write(sb.toString().getBytes());
            //===================================================
            os.flush();
            os.close();*/

            socket.close();// 请求结束


        }
    }


    /**
     * 静态资源重复获取问题解决 是否是静态资源
     * @param uri
     * @return
     */
    public static boolean isStatic(String uri){
        boolean isStatic = false;
        //定义一个静态资源的集合
        String[] suffixes={"html","css","jpg","js","jpeg","png"};
        for(String suffix:suffixes){
            if(uri.endsWith("."+suffix)){
                isStatic = true;
                break;
            }
        }
        return isStatic;
    }
}
