package com.cobra.sprboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication(
        exclude={
                DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class LearnApplication
{
    public static void main(String[] args) throws InterruptedException
    {
        SpringApplication.run(LearnApplication.class,args);
    }

    /**
     * 标注在方法上 程序启动时会执行一边
     */
    @Autowired
    public void remarkUser(List<XAnimal> animals)
    {
        animals.get(0).eat();
        System.out.println("=============>@Autowired标注在方法上 程序启动时自动执行");

    }


    @PostConstruct
    public void remarkUser1()
    {
        System.out.println("=============>@PostConstruct标注在方法上 有点类似构造");
    }

}

interface XAnimal {
    void eat();
}
@Service
class Dog implements XAnimal {
    @Override
    public void eat()
    {
        System.out.println("狗吃屎");
    }
}

@Service
class Pig implements XAnimal {
    @Override
    public void eat()
    {
        System.out.println("🐖吃草");
    }
}

@Service
 class Cat implements XAnimal {
    @Override
    public void eat() {
        System.out.println("猫吃老鼠");
    }
}



