package com.cobra.prc.socketAndServer;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by legwo on 2019/5/14.
 */
public class ServerThreadPool
{
    ExecutorService service = Executors.newFixedThreadPool(2);
    public static void main(String[] args)
    {
        ExecutorService service = Executors.newFixedThreadPool(2);// 两个线程
        try
        {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("NIOServer has start,listening port "+serverSocket.getLocalSocketAddress());
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("Connection from "+socket.getRemoteSocketAddress());



            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
