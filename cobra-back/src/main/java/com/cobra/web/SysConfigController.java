package com.cobra.web;

import com.cobra.constant.BackConfig;
import com.cobra.pojo.BackConfigParams;
import com.cobra.service.BackConfigParamsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Slf4j // lombok插件里面
@RestController
@RequestMapping(value = "sysConfig/")
public class SysConfigController {

    @Autowired private BackConfigParamsService backConfigParamsService;
    @GetMapping(value = "/get")
    public String get(){
        log.info("----------->info");
        BackConfig.sysConfigList = backConfigParamsService.select(new BackConfigParams());
        log.debug("----------->debug");
        log.trace("----------->trace");
        log.error("----------->error");
        return Arrays.toString(BackConfig.sysConfigList.toArray());
    }

    @RequestMapping("/visit")
    public String visit(){

        return "v2/refund/refundDetail";
    }
}
