package com.ihome.web;

import com.ihome.pojo.SysConfig;
import com.ihome.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j // lombok插件里面
@RestController
@RequestMapping(value = "sysConfig/")
public class SysConfigController {

    @Autowired private SysConfigService sysConfigService;
    @GetMapping(value = "get/{id}/{userName}")
    public String get(@PathVariable Integer id,@PathVariable String userName){
        if(id >= 3)
        {
            return "id不正确";
        }

        if(!"jack".equals(userName)){
            return "userName不正确";
        }
        SysConfig sysConfig = new SysConfig();
        sysConfig.setId(id);
        List<SysConfig> list = sysConfigService.select(sysConfig);
        if(list.size() == 0){
            return "id不存在";
        }
        return Arrays.toString(list.toArray());
    }
}
