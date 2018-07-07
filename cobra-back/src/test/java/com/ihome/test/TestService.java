package com.ihome.test;
import com.ihome.Application;
import com.ihome.pojo.SysConfig;
import com.ihome.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class TestService {
    @Autowired private SysConfigService sysConfigService;

    @Test public void test(){
        SysConfig sysConfig = new SysConfig();
        List<SysConfig> list = sysConfigService.select(sysConfig);
        for (SysConfig config:list){
            log.info("------>\t"+config.toString());
        }
    }

}
