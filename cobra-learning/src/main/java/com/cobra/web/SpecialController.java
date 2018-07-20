package com.cobra.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping(value = "/rest")
public class SpecialController
{
    @Autowired private RestTemplate restTemplate;
    @RequestMapping("/{id}/test")
    public void testRest(@PathVariable String id){
       restTemplate.getForEntity("https://blog.csdn.net/itguangit/article/details/78825505", List.class);
    }
}
