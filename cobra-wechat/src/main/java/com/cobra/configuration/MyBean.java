package com.cobra.configuration;

import com.cobra.pojo.User1;
import com.cobra.pojo.User2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBean
{
    @Bean
    public User1 user1()
    {
        User1 u1 = new User1();
        u1.setName("lg");
        u1.setAddress("mars");
        return u1;
    }

    @Bean
    public User2 user2(){
        User2 u2 = new User2();
        u2.setName("lg");
        u2.setAddress("mars");
        return u2;
    }

}
