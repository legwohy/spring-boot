package com.cobra.spring.sprboot.aware;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * <a href="https://www.bbsmax.com/A/MyJxvrbadn/">两种容器的区别</a>
 * spring提供两种容器
 * beanFactory
 *  最基本的容器 提供基本的依赖注入 修改行为使用beanFactory
 * * 和applicationContext 封装beanFactory
 */
public class AwareMainTest
{
    public static void main(String[] args)
    {
        AwareMainTest test = new AwareMainTest();
        //test.testBeanFactory();
        test.testApplicationContext();



    }

    public void testApplicationContext(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        Apple apple = (Apple)ctx.getBean("apple");
        Market market = (Market)ctx.getBean("market");
        System.out.println("apple:"+apple.getName());
        System.out.println("market:"+market.getName());
    }

    public void testBeanFactory(){
        // 获取bean工厂
        ConfigurableBeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));

        Apple apple = (Apple)beanFactory.getBean("apple");
        BeanPostProcessor bpp = new AppleAwarePostProcessor(apple);

        // bpp必须加入到当前容器中 才能注册 修改bean的行为 才能调用BeanPostProcessor修改bean的行为
        beanFactory.addBeanPostProcessor(bpp);

        // 从工厂中获取实例
        Market market = (Market)beanFactory.getBean("market");


        System.out.println("========>"+market.getName());
    }
}
