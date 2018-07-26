package com.cobra.init;

import com.cobra.constant.BackConfig;
import com.cobra.pojo.BackConfigParams;
import com.cobra.service.BackConfigParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class InitBack implements CommandLineRunner {
    @Autowired private BackConfigParamsService backConfigParamsService;
   /* @Override
    public void afterPropertiesSet() throws Exception {

    }*/

    @Override
    public void run(String... args) throws Exception {
        List<BackConfigParams> list = backConfigParamsService.select(new BackConfigParams());
        if(list.size() != 0){
            BackConfig.sysConfigList = list;
        }
    }
}
