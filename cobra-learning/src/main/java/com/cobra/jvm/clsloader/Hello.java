package com.cobra.jvm.clsloader;

/**
 * @auther: leigang
 * @date: 2019/1/4 18:16
 * @description:
 */
public class Hello
{
    public static void main(String[] args){
        System.out.println("tes22t2");
        for (String arg : args)
        {
            System.out.println("运行Hello的参数：" + arg);
        }

    }

    public void test(){
        System.out.println("执行test方法===================" );
    }

}
