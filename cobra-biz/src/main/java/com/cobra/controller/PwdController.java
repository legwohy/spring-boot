package com.cobra.controller;

import com.alibaba.fastjson.JSON;
import com.cobra.domain.ContentDTO;
import com.cobra.domain.PwdReqDTO;
import com.cobra.param.BaseResponse;
import com.cobra.service.PwdService;
import com.cobra.util.cryto.SecretKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author admin
 * @date 2021/1/14 15:22
 * @desc
 */
@Slf4j
@RestController
@RequestMapping("/pwd")
public class PwdController {

    final Map<String,String> keyMap = new HashMap<>();

    @Autowired
    private PwdService pwdService;

    @RequestMapping("/call")
    public BaseResponse call(@RequestBody PwdReqDTO reqDTO) throws Exception{
        try {
            if("RSA".equals(reqDTO.getEncType()) || "SM2".equals(reqDTO.getEncType())){
                reqDTO.setPrivateKey(keyMap.get(SecretKeyUtils.PRIVATE_KEY));
                reqDTO.setPubKey(keyMap.get(SecretKeyUtils.PUBLIC_KEY));
            }
            return pwdService.doService(reqDTO);
        } catch (Exception e) {
            log.error("调用错误,errorMsg:{}", e);
            return new BaseResponse("301", "调用异常");
        }
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

    /**
     * 上传key
     * @param reqDTO
     * @return
     * @throws Exception
     */
    @RequestMapping("/upload")
    public BaseResponse upload(@RequestBody PwdReqDTO reqDTO) throws Exception{
        keyMap.put(SecretKeyUtils.PUBLIC_KEY,reqDTO.getPubKey());
        keyMap.put(SecretKeyUtils.PRIVATE_KEY,reqDTO.getPrivateKey());
        return new BaseResponse(keyMap);

    }


}


