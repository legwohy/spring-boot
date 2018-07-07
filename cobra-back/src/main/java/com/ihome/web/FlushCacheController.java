package com.ihome.web;

import com.ihome.constant.BackConfig;
import com.ihome.pojo.SysConfig;
import com.ihome.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("flush/")
public class FlushCacheController {

    @Autowired private SysConfigService sysConfigService;

    @GetMapping("/{name}/{code}")
    public String flush(@PathVariable String name,@PathVariable String code){
        if(!"jack".equals(name)){
            return "用户名不正确";
        }
        if(!"xxoo".equals(code)){
            return "无权限";
        }

        List<SysConfig> list = sysConfigService.select(new SysConfig());
        if(list.size() == 0){
            return "加载错误";
        }
        BackConfig.sysConfigList = list;

        return "加载成功";

    }
}
