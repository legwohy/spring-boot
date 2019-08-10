package com.cobra.prc.socketAndServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 监听到端口 进行读写操作
 * 1、获取ServerSocketChannel
 * 2、注册到Selector选择器
 * 3、轮询检查策略实时检查数据的就绪状态决定处理数据
 */
public class NIOServer
{
    /**
     * NIO 整个工程均用单线程 而且很快 只有数据过来才会处理
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            // 基于不同的平台来显示 provider方法
            // 客户端的连接通道
            ServerSocketChannel serverSocketChannel =  ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);// 默认是阻塞的 true 会抛异常
            serverSocketChannel.bind(new InetSocketAddress(9999));// 监听端口

            System.out.println("NIOServer has start. listenning port"+serverSocketChannel.getLocalAddress());

            // 选择器 新连接的Channel注册到选择器上
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);// 默认状态 acceptable


            // 数据缓冲区 读写数据军用buffer缓存
            // 分配大小 可以公用 byte[] 数组(position,limit 岂止)实现
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            // while轮询选择器中的ServerSocketChannel 看是由有数据传过来
            while (true){
                int select = selector.select();// 操作系统层面的 Epoll模型(C实现) io复用技术
                if(select == 0){
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                // 迭代
                while (iterator.hasNext())
                {
                    SelectionKey key = iterator.next();

                    // 已经连接上来了 这时可以读写
                    if(key.isAcceptable())
                    {
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        System.out.println("Connection "+clientChannel.getRemoteAddress());
                        clientChannel.configureBlocking(false);// 非阻塞

                        // 说明该channel可以度数据了 必须修改数据下面才能读取
                        clientChannel.register(selector,SelectionKey.OP_READ);
                    }

                    // 如果已经连接上来
                    if(key.isReadable())
                    {
                        SocketChannel socketChannel = (SocketChannel)key.channel();
                        socketChannel.read(byteBuffer);// 数据读到缓存中
                        String request = new String(byteBuffer.array()).trim();
                        byteBuffer.clear();// 将position还原初始位置0 limit还原为capacity
                        System.out.println(String.format("From %m:%s",socketChannel.getRemoteAddress(),request));

                        // 响应 也是buffer 缓存
                        socketChannel.write(ByteBuffer.wrap("response".getBytes()));
                    }
                    iterator.remove();// 删除

                }
            }


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
