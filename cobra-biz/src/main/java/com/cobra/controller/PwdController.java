package com.cobra.controller;

import com.cobra.domain.PwdReqDTO;
import com.cobra.param.BaseResponse;
import com.cobra.service.PwdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author admin
 * @date 2021/1/14 15:22
 * @desc
 */
@RestController
@RequestMapping("/pwd")
public class PwdController {
    @Autowired
    private PwdService pwdService;
    @RequestMapping("/output")
    public BaseResponse output(@RequestBody PwdReqDTO reqDTO)throws Exception{


        return pwdService.doService(reqDTO);
    }
}


