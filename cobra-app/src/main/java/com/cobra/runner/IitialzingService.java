package com.cobra.runner;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;


@Service
public class IitialzingService implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化bean 可以使用依赖注入");
    }
}
