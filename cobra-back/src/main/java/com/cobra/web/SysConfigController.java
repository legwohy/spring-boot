package com.cobra.web;

import com.cobra.constant.BackConfig;
import com.cobra.pojo.SysConfig;
import com.cobra.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Slf4j // lombok插件里面
@RestController
@RequestMapping(value = "sysConfig/")
public class SysConfigController {

    @Autowired private SysConfigService sysConfigService;
    @GetMapping(value = "/get")
    public String get(){
        BackConfig.sysConfigList = sysConfigService.select(new SysConfig());
        return Arrays.toString(BackConfig.sysConfigList.toArray());
    }
}
