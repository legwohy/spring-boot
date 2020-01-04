package com.cobra.jvm.btrace;

import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.*;

/**
 * <a link='https://www.jianshu.com/p/1b52561e3848'/>
 * <p>
 * <p>
 * <a link='https://blog.csdn.net/wilsonpeng3/article/details/52576253'/>
 * start：btrace -p 2021  -cp  D:\code_repository\cobra\cobra-learning\src\main\lib  pid BTraceTest.java
 *
 * tip:  -cp路径  BTraceTest引用jar包所在的路径 一般是 btrace-client.jar包路径
 *          中文会出现大量乱码 暂无解决方案
 */
@BTrace
public class BTraceTest {
    private static long count;

    static {
        println("---------------------------JVM properties:---------------------------");
        //printVmArguments();
        println("---------------------------System properties:------------------------");
        //printProperties();
        println("---------------------------OS properties:----------------------------");
        //printEnv();
        //exit();

    }

    /**
     * print request and response
     *
     * @param a
     * @param b
     */
    @OnMethod(
            clazz = "com.cobra.jvm.btrace.Calculator",
            method = "add",
            location = @Location(Kind.RETURN)
    )
    public static void trace1(int a, int b, @Return int sum) {
        println("trace1:a=" + a + ",b=" + b + ",sum=" + sum);
    }

    /**
     * print method loading time
     */
   /* @OnMethod(
            clazz = "com.cobra.jvm.btrace.Calculator",
            method = "add",
            location = @Location(Kind.RETURN)
    )
    public static void trace2(@Duration long duration) {
        println(strcat("duration(nanos): ", str(duration)));
        println(strcat("duration(s): ", str(duration / 1000000000)));
    }*/

    /**
     * monitor clazz method ect
     */
   /* @OnMethod(
            clazz = "com.cobra.jvm.btrace.Calculator",
            method = "add",
            location = @Location(value = Kind.CALL, clazz = "/.", method = "sleep")
    )
    public static void trace3(@ProbeClassName String pcm, @ProbeMethodName String pmn,
                              @TargetInstance Object instance, @TargetMethodOrField String method) {
        println(strcat("ProbeClassName: ", pcm));
        println(strcat("ProbeMethodName: ", pmn));
        println(strcat("TargetInstance: ", str(instance)));
        println(strcat("TargetMethodOrField : ", str(method)));
        println(strcat("count: ", str(++count)));
    }*/


    /**
     * time
     */
   /* @OnTimer(6000)
    public static void trace4() {
        println(strcat("trace4:count: ", str(count)));
    }*/

    /**
     * get feild
     */
    /*@OnMethod(
            clazz = "com.cobra.jvm.btrace.Calculator",
            method = "add",
            location = @Location(Kind.RETURN)
    )
    public static void trace5(@Self Object calculator) {
        println(get(field("Calculator", "c"), calculator));
    }*/


    /**
     * print stack and heap
     */
   /* @OnTimer(4000)
    public static void traceMemory() {
        println("heap:");
        println(heapUsage());
        println("no-heap:");
        println(nonHeapUsage());
    }*/

    /**
     * check deadlocks
     */
  /*  @OnTimer(4000)
    public static void trace6() {
        deadlocks();
    }*/


}
