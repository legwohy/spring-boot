package com.cobra.service;

import com.alibaba.fastjson.JSON;
import com.cobra.domain.ContentDTO;
import com.cobra.domain.PwdReqDTO;
import com.cobra.param.BaseResponse;
import com.cobra.util.StringCommonUtils;
import com.cobra.util.cryto.MessageDigestUtils;
import com.cobra.util.cryto.SmCipherUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 * @date 2021/1/14 15:41
 * @desc
 */
@Component
public class PwdService {
    final static String seed = "698663d8d064266e2ead8d1d19cc5166";

    public BaseResponse doService(PwdReqDTO reqDTO)throws Exception{
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("id",reqDTO.getId());
        jsonMap.put("name",reqDTO.getName());
        String srcSign = MessageDigestUtils.md5(JSON.toJSONString(jsonMap));
        if(reqDTO.getSign().equals(srcSign)){
            return new BaseResponse("201","签名错误");
        }

        String result = SmCipherUtils.sm4CbcDec(reqDTO.getContent(),seed);
        if(StringCommonUtils.isEmpty(result)){
            return new BaseResponse("202","解密错误");
        }
        ContentDTO contentDTO = JSON.parseObject(result, ContentDTO.class);
        if(null == contentDTO){
            return new BaseResponse("203","内容错误");
        }
        contentDTO.setK1("内容一\t"+contentDTO.getK1());
        contentDTO.setK2("内容二\t"+contentDTO.getK2());
        String cipher = SmCipherUtils.sm4CbcEnc(JSON.toJSONString(contentDTO),seed);

        return new BaseResponse(cipher);
    }
}
