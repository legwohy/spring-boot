package com.cobra.bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanMain
{
    public static void main(String[] args)
    {
        // 可以取到全局的bean
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:bean/applicationContext.xml");

        // 只能去取到beanA中的
        ApplicationContext context2 = new ClassPathXmlApplicationContext("classpath:bean/bean-emp.xml");

        System.out.println(context2.getBean("emp"));

    }
}
