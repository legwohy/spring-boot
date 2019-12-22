package com.cobra.init;

import com.cobra.service.UserInfoFromService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitialzingService implements InitializingBean {
    @Autowired private UserInfoFromService userInfoFromService;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化bean 可以使用依赖注入:"+userInfoFromService.selectByPrimaryKey(1).getUserName());
    }
}
