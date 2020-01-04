package com.cobra.sprboot.controller;

import com.cobra.domain.entity.UserInfo;
import com.cobra.param.BaseResponse;
import io.swagger.annotations.Api;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(description = "测试过滤器 拦截响应 并对部分字段脱敏")
@RestController
@RequestMapping("/userInfo")
public class ResponseFilterController
{
    @PostMapping("/list")
    public BaseResponse<List<UserInfo>> list(@RequestBody UserInfo userInfo){
        List<UserInfo> list = new ArrayList<>();
        UserInfo u1 = new UserInfo();
        u1.setUserName("jack");
        u1.setPassword("123");
        u1.setId(1);
        list.add(u1);

        UserInfo u2 = new UserInfo();
        u2.setUserName("jack");
        u2.setPassword("123");
        u2.setId(1);
        list.add(u2);

        return new BaseResponse<List<UserInfo>>(list);

    }

    @PostMapping("/obj")
    public BaseResponse<UserInfoTemp> obj(@RequestBody UserInfo userInfo){
        UserInfoTemp temp = new UserInfoTemp();
        List<UserInfo> list = new ArrayList<>();
        UserInfo u1 = new UserInfo();
        u1.setUserName("jack");
        u1.setPassword("123");
        u1.setId(1);
        list.add(u1);

        UserInfo u2 = new UserInfo();
        u2.setUserName("jack");
        u2.setPassword("123");
        u2.setId(1);
        list.add(u2);

        temp.setList(list);
        temp.setUserName("rose");
        temp.setPwd("234");
        temp.setUserInfo(new UserInfo(1,"joker","chai"));

        return new BaseResponse<>(temp);

    }

    @Data
    private class UserInfoTemp{

        private String userName;
        private String pwd;
        private UserInfo userInfo;
        private List<UserInfo> list;
    }
}
