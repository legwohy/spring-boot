package com.cobra.init;

import com.cobra.constant.BackConfig;
import com.cobra.pojo.SysConfig;
import com.cobra.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class InitBack implements CommandLineRunner {
    @Autowired private SysConfigService sysConfigService;
   /* @Override
    public void afterPropertiesSet() throws Exception {

    }*/

    @Override
    public void run(String... args) throws Exception {
        List<SysConfig> list = sysConfigService.select(new SysConfig());
        if(list.size() != 0){
            BackConfig.sysConfigList = list;
        }
    }
}
