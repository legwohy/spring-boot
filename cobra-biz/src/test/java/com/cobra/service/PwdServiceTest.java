package com.cobra.service;

import com.BaseTest;
import com.alibaba.fastjson.JSON;
import com.cobra.domain.ContentDTO;
import com.cobra.domain.PwdReqDTO;
import com.cobra.util.cryto.MessageDigestUtils;
import com.cobra.util.cryto.SmCipherUtils;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author admin
 * @date 2021/1/14 15:42
 * @desc
 */
@Slf4j
public class PwdServiceTest extends BaseTest {
    final static String seed = "698663d8d064266e2ead8d1d19cc5166";
    @Autowired
    private PwdService pwdService;
    @Test
    public void doService() throws Exception{
        PwdReqDTO reqDTO = new PwdReqDTO();
        reqDTO.setId("1001");
        reqDTO.setName("小王");
        Map<String,Object> signMap = new LinkedHashMap<>();
        signMap.put("id",reqDTO.getId());
        signMap.put("name",reqDTO.getName());
        reqDTO.setSign(MessageDigestUtils.md5(JSON.toJSONString(signMap)));

        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setK1("zhi");

        reqDTO.setContent(SmCipherUtils.sm4CbcEnc(JSON.toJSONString(contentDTO),seed));

        log.info("params:{}",JSON.toJSONString(reqDTO));

        log.info(pwdService.doService(reqDTO).toString());

    }

}