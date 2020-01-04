package com.cobra.prc.rpc;

import com.cobra.prc.rpc.api.IProductService;
import com.cobra.prc.rpc.provider.ProductServiceImpl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <a link='https://www.jianshu.com/p/29d75a25eeaf'/>
 * 请求和响应 通过socket建立通信
 */
public class RpcFrame {
    /**
     * 通过socket 传递请求的参数 接收响应的参数
     * 动态代理RPC服务接口
     */
    public static Object rpc(final Class clazz) throws Throwable {
        return Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // socket通信 发送调用的方法以及参数
                Socket socket = new Socket("127.0.0.1", 8888);
                // socket接收响应的数据
                String apiClazzName = clazz.getName();
                String methodName = method.getName();
                Class[] parameterType = method.getParameterTypes();


                // 请求的数据
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeUTF(apiClazzName);
                oos.writeUTF(methodName);
                oos.writeObject(parameterType);
                oos.writeObject(args);
                oos.flush();

                // 响应的数据 返回的数据
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object response = ois.readObject();
                ois.close();
                oos.close();

                socket.close();

                return response;
            }
        });
    }

    /** 启动服务
     * 1、接收请求的参数
     * 2、反射调用服务的提供者
     * 3、返回响应的数据
     *
     * */
    public static void main(String[] args) throws Exception {
        // 服务方 提供者
        ServerSocket serverSocket = new ServerSocket(8888);
        while (true) {
            Socket socket = serverSocket.accept();// 阻塞

            // 请求的接口服务内容 按照顺序读取 包括调用的接口、方法以及参数
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String apiClazzName = ois.readUTF();
            String methodName = ois.readUTF();
            Class[] parameterTypes = (Class[]) ois.readObject();
            Object[] methodArgs = (Object[]) ois.readObject();

            // 反射获取响应的数据
            Class clazz = null;
            if (apiClazzName.equals(IProductService.class.getName())) {
                clazz = ProductServiceImpl.class;// 实现类
            }
            Method method = clazz.getMethod(methodName, parameterTypes);
            Object invoke = method.invoke(clazz.newInstance(), methodArgs);

            // 结果返回
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(invoke);
            ois.close();
            oos.close();
            socket.close();
        }

    }
}
