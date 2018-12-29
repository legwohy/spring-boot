package com.cobra.inout;


/**
 * @auther: leigang
 * @date: 2018/12/28 14:16
 * @description:
 */
public class MainClass
{
    public static void main(String[] args)
    {

        OuterClass.InnerClass innerClass =  new OuterClass().new InnerClass();

    }
}
