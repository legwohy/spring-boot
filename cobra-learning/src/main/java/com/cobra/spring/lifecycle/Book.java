package com.cobra.spring.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
 *
 * 初始化过程
 * 1、构造方法
 * 2、Aware(BeanName、BeanFactory、ApplicationContext)
 * 3、BeanPostProcessor.postProcessBeforeInitialization
 * PostConstruct(初始化之前 构造方法之后 这是一个方法)
 * InitializingBean.afterPropertiesSet(初始化Bean)
 * init-method（自定义初始化方法）
 * 3、BeanPostProcessor.postProcessAfterInitialization
 *
 *
 */
public class Book implements BeanNameAware,BeanFactoryAware,
        ApplicationContextAware,InitializingBean,DisposableBean {
    private String bookName;
    public Book(){
        System.out.println("Book Initializing ");
    }
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("Book.setBeanFactory invoke");
    }
    public void setBeanName(String name) {
        System.out.println("Book.setBeanName invoke");
    }
    public void destroy() throws Exception {
        System.out.println("Book.destory invoke");
    }
    public void afterPropertiesSet() throws Exception {
        System.out.println("Book.afterPropertiesSet invoke");
    }
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("Book.setApplicationContext invoke");
    }
    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
        System.out.println("setBookName: Book name has set.");
    }
    public void myPostConstruct(){
        System.out.println("Book.myPostConstruct invoke");
    }
    // 自定义初始化方法
    @PostConstruct
    public void springPostConstruct(){
        System.out.println("@PostConstruct");
    }
    public void myPreDestory(){
        System.out.println("Book.myPreDestory invoke");
        System.out.println("---------------destroy-----------------");
    }
    // 自定义销毁方法
    @PreDestroy
    public void springPreDestory(){
        System.out.println("@PreDestory");
    }
    @Override
    protected void finalize() throws Throwable {
        System.out.println("------inside finalize-----");
    }
}
