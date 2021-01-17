package com.cobra.controller;

import com.alibaba.fastjson.JSON;
import com.cobra.domain.ContentDTO;
import com.cobra.domain.PwdReqDTO;
import com.cobra.param.BaseResponse;
import com.cobra.service.PwdService;
import com.cobra.util.cryto.SecretKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

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
    public BaseResponse output(@RequestBody PwdReqDTO reqDTO) throws Exception{

        return pwdService.doService(reqDTO);
    }

    @RequestMapping("/sign")
    public BaseResponse sign(@RequestBody PwdReqDTO reqDTO) throws Exception{

        return new BaseResponse(pwdService.sign(reqDTO));
    }

    @RequestMapping("/encrypt")
    public BaseResponse encrypt(@RequestBody PwdReqDTO reqDTO) throws Exception{
        Map<String, String> srcMap = new LinkedHashMap<>();
        srcMap.put("k1", reqDTO.getK1());
        srcMap.put("k2", reqDTO.getK2());
        return new BaseResponse(pwdService.encrypt(JSON.toJSONString(srcMap), reqDTO));
    }

    @RequestMapping("/decrypt")
    public BaseResponse decrypt(@RequestBody PwdReqDTO reqDTO) throws Exception{

        return new BaseResponse(JSON.parseObject(pwdService.decrypt(reqDTO), ContentDTO.class));
    }

    @RequestMapping("/genKeyPair")
    public BaseResponse genKeyPair(@RequestBody String type) throws Exception{
        if ("SM2".equalsIgnoreCase(type)) {
            return new BaseResponse(SecretKeyUtils.genSm2KeyPair());
        } else {
            return new BaseResponse(SecretKeyUtils.genRSAKeyPair("RSA", "123456"));
        }
    }

}


