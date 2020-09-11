package com.cobra.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  applicationContext从各xml配置文件中获取bean
 */
@RestController
public class BeanController
{
    /**
     * 这里只读取 bean-emp.xml 文件 是取不到 ${emp.name} 中配置的属性的
     * @return
     */
    @RequestMapping("/context-emp")
    public Object contextEmp(){
        ApplicationContext context2 = new ClassPathXmlApplicationContext("classpath:bean/bean-emp.xml");
        return context2.getBean("emp");
    }

    /**
     * 由于从 applicationContext.xml 这里取 是可以取到的 emp的属性吧name
     * @return
     */
    @RequestMapping("/context-all")
    public Object contextAll(){
        ApplicationContext context2 = new ClassPathXmlApplicationContext("classpath:bean/applicationContext.xml");
        return context2.getBean("emp");
    }

    /**
     * 这里是通过xmlConfig注入的 可以取到值
     */
    @Autowired
    private Employee employee;
    @RequestMapping("/emp")
    public Object emp(){
        return employee;
    }

}
