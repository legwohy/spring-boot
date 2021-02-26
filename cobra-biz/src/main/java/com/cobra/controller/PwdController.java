package com.cobra.controller;

import com.alibaba.fastjson.JSON;
import com.cobra.domain.*;
import com.cobra.param.BaseResponse;
import com.cobra.service.PwdService;
import com.cobra.util.StringCommonUtils;
import com.cobra.util.cryto.SecretKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

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

    @RequestMapping("/callEOP")
    public Map callEOP(@RequestBody EopReq req) throws Exception{
        Map<String, Object> returnMap = new HashMap<>();
        try {
            log.info("eop参数:{}", req);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("mobileNo", req.getAccs_nbr());
            dataMap.put("seqid", req.getSeqid());
            dataMap.put("token", req.getToken());

            List<Map> list = new ArrayList<>();
            list.add(dataMap);

            returnMap.put("code", "10000");
            returnMap.put("message", "成功");
            returnMap.put("data", encrypt(JSON.toJSONString(list),req.getSkey()));

            return returnMap;
        } catch (Exception e) {
            log.error("EOP调用错误,errorMsg:{}", e);
            returnMap.put("code", "-1");
            returnMap.put("message", "失败");
            return returnMap;
        }
    }

    private static String encrypt(String data, String password){
        if (StringCommonUtils.isNotEmpty(password) && password.length() > 16) {
            password = password.substring(0, 16);
        }

        String result = null;
        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, key);
            byte[] encryptByte = cipher.doFinal(data.getBytes("UTF-8"));
            result = (new BASE64Encoder()).encode(encryptByte);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return result;
    }


    public static String decrypt(String base64Data, String password){
        if (StringCommonUtils.isEmpty(password)) {
            return "";
        }
        if (password.length() > 16) {
            password = password.substring(0, 16);
        }

        String result = null;

        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, key);
            byte[] decryptByte = (new BASE64Decoder()).decodeBuffer(base64Data);
            byte[] resultByte = cipher.doFinal(decryptByte);
            result = new String(resultByte, "UTF-8");
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return result;
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
    public BaseResponse genKeyPair(String type) throws Exception{
        log.info("生成密钥 type:{}", type);
        if ("SM2".equalsIgnoreCase(type)) {
            return new BaseResponse(SecretKeyUtils.genSm2KeyPair());
        } else {
            return new BaseResponse(SecretKeyUtils.genRSAKeyPair());
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
        keyMap.put(SecretKeyUtils.PUBLIC_KEY, reqDTO.getPubKey());
        keyMap.put(SecretKeyUtils.PRIVATE_KEY, reqDTO.getPrivateKey());
        return new BaseResponse(keyMap);

    }

    /**
     * 上传key
     * @param reqDTO
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public BaseResponse list(@RequestBody TestListReqDTO reqDTO) throws Exception{
        log.info("入参:" + reqDTO);
        List<TmpReqDTO> reqList = reqDTO.getList();

        List<TmpRspDTO> list = new ArrayList<>();
        TmpRspDTO tmpRspDTO = new TmpRspDTO();
        tmpRspDTO.setRsp1(reqList.get(0).getReq1());
        tmpRspDTO.setRsp2(reqList.get(0).getReq2());

        return new BaseResponse(list);

    }

}


