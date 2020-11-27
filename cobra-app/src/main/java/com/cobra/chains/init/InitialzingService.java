package com.cobra.chains.init;

import com.cobra.service.UserInfoFromService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 不加注解的化 初始化是不会调用的 在 CommandLineRunner 之前加载
 */
@Slf4j
@Service
public class InitialzingService implements InitializingBean {
    @Autowired(required = false)
    private UserInfoFromService userInfoFromService;

    @Override
    public void afterPropertiesSet(){
        try {
            log.info("初始化bean 可以使用依赖注入:");
            log.info(userInfoFromService.selectByPrimaryKey(1).getUserName());

        }catch (Exception e){
            log.error("errorMsg=[{}]",e);
        }
    }
}
