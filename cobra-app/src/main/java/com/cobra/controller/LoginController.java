package com.cobra.controller;

import com.alibaba.fastjson.JSONObject;
import com.cobra.constants.CobraCode;
import com.cobra.domain.entirty.UserInfo;
import com.cobra.exception.CobraException;
import com.cobra.param.BaseResponse;
import com.cobra.util.DateUtils;
import com.cobra.util.FileUtils;
import com.cobra.util.ResponseUtil;
import com.cobra.util.rsa.RSAEncrypt;
import com.cobra.util.rsa.RSASignature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Date;

@Api(value = "登陆控制类",description = "登陆控制类")
@RestController
@RequestMapping(value = "/api/user")
public class LoginController
{
    Logger logger = LoggerFactory.getLogger(LoginController.class);


    @ApiOperation(value = "登陆",notes = "登陆")
    @PostMapping("/login")
    public BaseResponse login(@RequestBody UserInfo userInfo){

       if(StringUtils.isEmpty(userInfo.getUserName())) {
           throw new CobraException(CobraCode.MISSING_REQUIRED_PARAM,"userName");
       }

       if(StringUtils.isEmpty(userInfo.getPassword())){
           throw new CobraException(CobraCode.MISSING_REQUIRED_PARAM,"password");
       }

       if(!"jack".equals(userInfo.getUserName()) && !"rose".equals(userInfo.getPassword())){
           throw new CobraException(CobraCode.WRONG_NAME_PASSWORD,CobraCode.WRONG_NAME_PASSWORD.getMsg());
       }

           // 生成token
           JSONObject header = new JSONObject();
           header.put("alg","RSA");
           header.put("type","JWT");

           String encodeHeader = Base64.encodeBase64String(header.toJSONString().getBytes());

           JSONObject payLoad = new JSONObject();
           payLoad.put("iss","test_app");
           payLoad.put("aud","test_api");
           payLoad.put("claim","purpose");
           payLoad.put("exp", DateUtils.addMinute(new Date(),30).getTime()+"");

           String encodePayLoad = Base64.encodeBase64String(payLoad.toJSONString().getBytes());

           // 签名
           String privateKey = null;
           String sign = null;
           //JSONObject signature = new JSONObject();
           try
           {
               privateKey = RSAEncrypt.loadPrivateKeyByFile(FileUtils.getRootPath()+"/keys");
               sign = RSASignature.sign(encodeHeader.concat(".").concat(encodePayLoad),privateKey);
               //signature.put("signature",sign);
           } catch (Exception e)
           {
               e.printStackTrace();
           }
           String encodeSignature = Base64.encodeBase64String(sign.getBytes());

           String token = encodeHeader.concat(".").concat(encodePayLoad).concat(".").concat(encodeSignature);

           return ResponseUtil.success(token);




    }

    // 生成密钥
    public static void main(String[] args){
        String path = LoginController.class.getClassLoader().getResource("").getPath().concat("keys/");

        // windows
        if("\\".equals(File.separator)){
            path = path.replace("/","\\");
        }
        // linux
        if("/".equals(File.separator)){
            path = path.replace("\\","/");
        }

        path = new File(path).getPath();
        System.out.println("------->path:\t"+path);
        RSAEncrypt.genKeyPair(path);
    }
}
