package com.cobra.web;

import com.cobra.constant.BackConfig;
import com.cobra.pojo.SysConfig;
import com.cobra.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 此接口不对外暴露
 */
@RestController
@RequestMapping("sysCache/")
public class FlushCacheController {

    @Autowired private SysConfigService sysConfigService;

    @GetMapping("/refresh")
    public String flush(){

        BackConfig.sysConfigList = sysConfigService.select(new SysConfig());

        return "加载成功";

    }
}
