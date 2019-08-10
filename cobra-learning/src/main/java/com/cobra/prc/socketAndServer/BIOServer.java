package com.cobra.prc.socketAndServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 可以在telnet中访问 Scanner访问
 * BIO 同步阻塞
 * 阻塞概念：应用程序在获取网络数据的时候，如果网络传输数据很慢，
 *          那么程序就一直等着，知道传输完毕为止。
 *非阻塞概念：应用程序直接可以获取到已经转备好的数据，无需等待。
 *
 */
public class BIOServer
{

    public static void main(String[] args)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8888);
            // 多个客户端 必须循环
            while (true)
            {
                Socket socket = serverSocket.accept();
                // 获取客户端响应数据
                Scanner input = new Scanner(socket.getInputStream());// 文件打印到控制台上
                // 会等待客户端数据的输入 会一直等待 一直占用当前线程
                String request = input.nextLine();
                if("quit".equals(request))
                {
                    break;
                }
                System.out.println(String.format("From %s: %n",socket.getRemoteSocketAddress(),request));
                String response = "From BIOServer Hello "+request+".\n";
                socket.getOutputStream().write(response.getBytes());

            }
        }catch (Exception e)
        {
            e.printStackTrace();

        }



    }
}
