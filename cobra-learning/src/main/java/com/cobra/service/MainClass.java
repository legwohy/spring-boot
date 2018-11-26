package com.cobra.service;

import java.lang.reflect.Type;

/**
 * @className ${ClassName}
 * @auther: leigang
 * @date: 2018/11/1 18:13
 * @description:
 */
public class MainClass
{
    public static void main(String[] args)
    {
        C c = new C();
        Type[] types = c.getClass().getGenericInterfaces();
        System.out.println("==========>"+types[0].getTypeName());
    }
}
