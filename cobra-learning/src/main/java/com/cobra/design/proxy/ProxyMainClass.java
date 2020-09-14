package com.cobra.design.proxy;

import com.cobra.design.proxy.entity.Animal;
import com.cobra.design.proxy.entity.Dog;


/**
 * 代理
 * 1、描述：两种代理方式jdk和cglib，其代理了接口Animal的实现类Dog，
 * 实现类包含两个方法，eat和sleep，eat方法调用了sleep方法
 * 预期：调用代理方法，后应该是eat和sleep两个方法都会走切面而被代理
 * 实际：jdk只代理了eat方法，cglib两个方法都代理
 *
 * 分析：动态代理的坑 内部方法不走切面，只能代理对象的方法才会走切面
 * springboot默认改为 cglib代理 若要指定代理模式为jdk spring.aop.proxy-target-class: false
 *
 */

public class ProxyMainClass
{
    public static void main(String[] args){
        CommonJdkProxy jdkProxy = new CommonJdkProxy(new Dog());
        Animal dogAnimal_1 = (Animal)jdkProxy.getInstance();
        dogAnimal_1.eat("肉肉");

        System.out.println("\r\n");

        CommonCglibProxy cglibProxy = new CommonCglibProxy(new Dog());
        Animal dogAnimal_2 = (Animal)cglibProxy.getInstance();
        dogAnimal_2.eat("肉肉");


    }
}
