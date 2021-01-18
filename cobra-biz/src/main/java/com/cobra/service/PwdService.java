package com.cobra.service;

import com.alibaba.fastjson.JSON;
import com.cobra.domain.ContentDTO;
import com.cobra.domain.PwdReqDTO;
import com.cobra.param.BaseResponse;
import com.cobra.util.StringCommonUtils;
import com.cobra.util.cryto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author admin
 * @date 2021/1/14 15:41
 * @desc
 */
@Component
@Slf4j
public class PwdService {
    final static String sm4_seed = "698663d8d064266e2ead8d1d19cc5166";
    final static String credit_seed = "698663d8d06426ab";
    final static String des_seed = "698663d8d06426ab不限长度";
    final static String three_des_seed = "asiainfo3Des";




    /**
     * 1、验签
     * 2、解密
     * 3、结果加密
     *
     * @param reqDTO
     * @return
     * @throws Exception
     */
    public BaseResponse doService(PwdReqDTO reqDTO) throws Exception{
        // 1、验签
        log.info("签名开始...... req:{}",reqDTO);
        String srcSign = sign(reqDTO);
        if (!reqDTO.getSign().equals(srcSign)) {
            log.info("签名失败 req:{},签名值:{}",reqDTO,srcSign);
            return new BaseResponse("201", "签名错误");
        }
        log.info("签名成功 sign:{}",srcSign);
        // 2、解密
        log.info("解密开始......");
        String result = decrypt(reqDTO);
        log.info("解密结果 result:{}",result);
        if (StringCommonUtils.isEmpty(result)) {
            return new BaseResponse("202", "解密错误");
        }
        ContentDTO contentDTO = JSON.parseObject(result, ContentDTO.class);
        if (null == contentDTO) {
            return new BaseResponse("203", "内容错误");
        }

        contentDTO.setK1("res_v-1:" + contentDTO.getK1());
        contentDTO.setK2("res_v-2:" + contentDTO.getK2());
        log.info("加密开始......");
        String cipher = encrypt(JSON.toJSONString(contentDTO),reqDTO);
        log.info("加密结果:{}",cipher);

        return new BaseResponse(cipher);
    }

    /**
     * 解密
     * @param reqDTO
     * @return
     * @throws Exception
     */
    public String decrypt(PwdReqDTO reqDTO) throws Exception{
        String cipherText = reqDTO.getContent();
        String encType = reqDTO.getEncType();
        String plainText = "";
        switch (encType) {
            case "SM4":
                plainText = SmCipherUtils.sm4CbcDec(cipherText, sm4_seed);
                break;
            case "SM2":
                plainText = SmCipherUtils.dec(cipherText, reqDTO.getPrivateKey());
                break;
            case "AES":
                plainText = CipherUtils.cipherAESForDecCredit(cipherText, credit_seed);
                break;
            case "DES":
                plainText = CipherUtils.cipherDESForDec(cipherText, des_seed);
                break;
            case "3DES":
                plainText = CipherUtils.decryptFor3DEs(cipherText, three_des_seed);
                break;
            case "RSA":
                plainText = CipherUtils.cipherRSAPrivate(cipherText, reqDTO.getPrivateKey());
                break;
            default:
                throw new RuntimeException("不支持解密方式:"+encType);

        }

        return plainText;
    }

    /**
     * 加密
     * @param srcPlainText
     * @param reqDTO
     * @return
     * @throws Exception
     */
    public String encrypt(String srcPlainText ,PwdReqDTO reqDTO) throws Exception{
        String encType = reqDTO.getEncType().toUpperCase();
        String cipherText = "";
        switch (encType) {
            case "SM4":
                cipherText = SmCipherUtils.sm4CbcEnc(srcPlainText, sm4_seed);
                break;
            case "SM2":
                cipherText = SmCipherUtils.enc(srcPlainText, reqDTO.getPubKey());
                break;
            case "AES":
                cipherText = CipherUtils.cipherAESForEncCredit(srcPlainText, credit_seed);
                break;
            case "DES":
                cipherText = CipherUtils.cipherDESForEnc(srcPlainText, des_seed);
                break;
            case "3DES":
                cipherText = CipherUtils.encryptFor3DEs(srcPlainText, three_des_seed);
                break;
            case "RSA":
                cipherText =  CipherUtils.cipherRSAPublic(srcPlainText, reqDTO.getPubKey());
                break;
            default:
                throw new RuntimeException("不支持加密方式:"+encType);
        }
        return cipherText;
    }

    /**
     * 签名
     * @param reqDTO
     * @return
     * @throws Exception
     */
    public String sign(PwdReqDTO reqDTO) throws Exception{
        Map<String, Object> jsonMap = new LinkedHashMap<>();
        jsonMap.put("name", reqDTO.getName());
        jsonMap.put("signType", reqDTO.getSignType());

        String signType = reqDTO.getSignType().toUpperCase();
        log.info("签名方式:"+signType);
        String sign = JSON.toJSONString(jsonMap);
        switch (signType) {
            case MessageDigestUtils.MD5:
                sign = MessageDigestUtils.md5(sign);
                break;
            case MessageDigestUtils.SHA256:
                sign = MessageDigestUtils.SHA256(sign);
                break;
            case MessageDigestUtils.SHA512:
                sign = MessageDigestUtils.SHA512(sign);
                break;
            case "RSA":
                PrivateKey privateKey = (PrivateKey)SecretKeyUtils.transRSAKey(Boolean.FALSE, reqDTO.getPrivateKey());
                sign = SignatureUtils.signature(sign, privateKey);
                break;
            default:
                throw new RuntimeException("不支持签名方式:"+signType);
        }
        return sign;

    }

}
