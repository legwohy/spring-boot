package com.cobra;

import com.cobra.bean.Animal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
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
    public void remarkUser(List<Animal> animals)
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
