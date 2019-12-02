package com.cobra.jvm.clsloader;

/**
 * 类加载过程:加载 -> 连接(验证、准备、解析)->初始化->使用->卸载
 * <a link='https://www.cnblogs.com/aspirant/p/7200523.html'/>
 */
 class SSClass
{
    static
    {
        System.out.println("SSClass");
    }
}
 class SuperClass extends SSClass
{
    static
    {
        System.out.println("SuperClass init!");
    }

    public static int value = 123;// 类初始化后 将 123赋值给 value

    public SuperClass()
    {
        System.out.println("init SuperClass");
    }
}
 class SubClass extends SuperClass
{
    static
    {
        System.out.println("SubClass init");
    }

    static int a;

    public SubClass()
    {
        System.out.println("init SubClass");
    }
}
public class NotInitialization
{
    public static void main(String[] args)
    {
        //System.out.println(SubClass.value);// 静态文件 本身不需要实例化

        SuperClass[] sca = new SuperClass[10];// 通过数组定义不会触发 此类初始化
    }
}
