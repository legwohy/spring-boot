package com.cobra.web;

import com.cobra.constant.BackConfig;
import com.cobra.pojo.BackConfigParams;
import com.cobra.service.BackConfigParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 此接口不对外暴露
 */
@RestController
@RequestMapping("sysCache/")
public class FlushCacheController {

    @Autowired private BackConfigParamsService backConfigParamsService;

    @GetMapping("/refresh")
    public String flush(){

        BackConfig.sysConfigList = backConfigParamsService.select(new BackConfigParams());

        return "加载成功";

    }
}
