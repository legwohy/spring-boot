package com.ihome.web;

import com.ihome.constants.TokenConstant;
import com.ihome.entirty.UserFile;
import com.ihome.util.FileUtilsBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


@Api(value = "文件控制",description = "文件控制")
@RestController
@RequestMapping(value = "/api/img")
public class FileUploadController
{
    @Autowired private FileUtilsBean fileUtilsBean;


    @ApiOperation(value = "上传文件",notes = "上传文件")
    @PostMapping("/upload")
    public String fileUpload(MultipartHttpServletRequest request) {
        String token = request.getHeader(TokenConstant.token);
        if(StringUtils.isEmpty(token))
        {
            return "缺头";
        }
        MultipartFile file = request.getFile("file");
        if(file != null){
             //fileUtilsBean.fileUpload(file,"1234567890","img");
        }
        return "ok";
    }




}
