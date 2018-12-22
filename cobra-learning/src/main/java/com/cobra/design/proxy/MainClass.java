package com.cobra.design.proxy;

import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * Created by legwo on 2018/12/22.
 */
public class MainClass
{
    public static void main(String[] args)
    {
       /* AnimalProxy proxy = new AnimalProxy(new Dog());
        Animal animal = (Animal)Proxy.newProxyInstance(
                        Dog.class.getClassLoader(),
                        Dog.class.getInterfaces(),
                        //new Class[]{Animal.class},// 接口
                        proxy);*/


        AnimalProxy proxy1 = new AnimalProxy(new Dog());
        Animal animal = (Animal)proxy1.getInstance();
        animal.eat("rou");

        Object obj = new Dog();
        System.out.println("1\t"+Arrays.toString(Dog.class.getInterfaces()));
        System.out.println("2\t"+Arrays.toString(new Dog().getClass().getInterfaces()));
        System.out.println("3\t"+Arrays.toString(obj.getClass().getInterfaces()));        Object object = new Dog();


    }
}
