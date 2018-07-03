package com.ihome.web;

import com.alibaba.fastjson.JSONObject;
import com.ihome.constants.TokenConstant;
import com.ihome.entirty.UserInfo;
import com.ihome.util.DateUtils;
import com.ihome.util.rsa.RSAEncrypt;
import com.ihome.util.rsa.RSASignature;
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
    public String login(@RequestBody UserInfo userInfo){
        logger.info("---------------------->有人登陆了");
       if(StringUtils.isEmpty(userInfo.getUserName())) {
           return "用户名不能为空";
       }

       if(StringUtils.isEmpty(userInfo.getPassword())){
           return "密码不能为空";
       }

       if("jack".equals(userInfo.getUserName()) && "rose".equals(userInfo.getPassword())){
           // 生成token
           JSONObject header = new JSONObject();
           header.put("alg","RSA");
           header.put("type","JWT");

           String encodeHeader = Base64.encodeBase64String(header.toJSONString().getBytes());

           JSONObject payLoad = new JSONObject();
           payLoad.put("iss","test_app");
           payLoad.put("aud","test_api");
           payLoad.put("claim","purpose");
           payLoad.put("exp", DateUtils.addMinute(new Date(),1).getTime()+"");

           String encodePayLoad = Base64.encodeBase64String(payLoad.toJSONString().getBytes());

           // 签名
           String privateKey = null;
           JSONObject signature = new JSONObject();
           try
           {
               privateKey = RSAEncrypt.loadPrivateKeyByFile(TokenConstant.keyPath);
               String sign = RSASignature.sign(encodeHeader.concat(".").concat(encodePayLoad),privateKey);
               signature.put("signature",sign);
           } catch (Exception e)
           {
               e.printStackTrace();
           }
           String encodeSignature = Base64.encodeBase64String(signature.toJSONString().getBytes());

          return encodeHeader.concat(".")
                             .concat(encodePayLoad).concat(".")
                             .concat(encodeSignature);

       }
       return null;


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
