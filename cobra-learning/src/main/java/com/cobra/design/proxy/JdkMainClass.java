package com.cobra.design.proxy;

import com.cobra.design.proxy.entity.Animal;
import com.cobra.design.proxy.entity.Dog;


/**
 * jdk动态代理
 */
public class JdkMainClass
{
    public static void main(String[] args)
    {
       /* CommonJdkProxy proxy = new CommonJdkProxy(new Dog());
        Animal animal = (Animal)Proxy.newProxyInstance(
                        Dog.class.getClassLoader(),
                        Dog.class.getInterfaces(),
                        //new Class[]{Animal.class},// 接口
                        proxy);*/


        CommonJdkProxy proxy1 = new CommonJdkProxy(new Dog());
        Animal animal = (Animal)proxy1.getInstance();
        animal.eat("肉肉");

//        Object obj = new Dog();
//        System.out.println("1\t"+Arrays.toString(Dog.class.getInterfaces()));
//        System.out.println("2\t"+Arrays.toString(new Dog().getClass().getInterfaces()));
//        System.out.println("3\t"+Arrays.toString(obj.getClass().getInterfaces()));


    }
}
