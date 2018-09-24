package com.cobra.controller;

import com.alibaba.fastjson.JSONObject;
import com.cobra.service.UserInfoService;

import com.cobra.util.VerifyCodeUtils;
import com.octo.captcha.service.CaptchaServiceException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String CAPTCHA_IMAGE_FORMAT = "jpeg";

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response)
    {
        logger.info("========>用户登陆访问了");

        String userPhone = request.getParameter("userPhone");
        String password = request.getParameter("password");

        System.out.println("password:"+password);
        boolean flag = userInfoService.login(userPhone,password);
        if(flag){
            return "zm/zmSuccess";
        }

        return "tg/index1";

    }

    @RequestMapping("/captcha.svl")
    protected void captcha(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] captchaChallengeAsJpeg = null;
        String RCaptchaKey=request.getParameter("RCaptchaKey");
        // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {

            String myRand = VerifyCodeUtils.genImgCode(response);
            redisTemplate.opsForValue().set(RCaptchaKey, myRand, 10 * 60L);//过期时间一个小时
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (CaptchaServiceException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

        // flush it in the response
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/" + CAPTCHA_IMAGE_FORMAT);

        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

}
