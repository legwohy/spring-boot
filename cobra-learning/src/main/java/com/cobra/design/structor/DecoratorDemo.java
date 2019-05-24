package com.cobra.design.structor;

import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.InputStream;

/**
 * 装饰者模式
 *  1) 动态添加或者覆盖(重写父类的方法 继承+多态)被包装者的行为
 *  2） 装饰者与被装饰者之间有层级(继承 extend)关系
 *  3) java IO 、spring core 、Spring MVC
 * {@link org.springframework.core.DecoratingClassLoader}
 */
public class DecoratorDemo
{
    public static void main(String[] args)
    {
        // 被装饰者
        InputStream inputStream = null;

        // 装饰者
        FilterInputStream filterInputStream = new DataInputStream(inputStream);


    }
}
