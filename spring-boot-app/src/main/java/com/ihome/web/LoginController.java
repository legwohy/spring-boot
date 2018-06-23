package com.ihome.web;

import com.ihome.entirty.UserFile;
import com.ihome.util.FileUtilsBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


@Api(value = "登陆控制类",description = "登陆控制类")
@RestController
public class LoginController {
    @Autowired private FileUtilsBean fileUtilsBean;

    @ApiOperation(value = "登陆",notes = "登陆")
    @GetMapping("/login")
    public ModelAndView login(String username){
        ModelAndView mv = new ModelAndView("index");
        System.out.println("------------>username:"+username+"登陆");
        if("jack".equals(username)){
            mv.setViewName("upload");
        }
        return mv;
    }

    @ApiOperation(value = "上传文件",notes = "上传文件")
    @PostMapping("/upload")
    public String fileUpload(UserFile uf,MultipartHttpServletRequest request) {
        MultipartFile file = request.getFile("file");
        if(file != null){
             fileUtilsBean.fileUpload(file,"1234567890","img");
        }
        return "ok";
    }




}
