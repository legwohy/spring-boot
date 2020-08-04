package com.cobra.controller;

import com.cobra.exception.CobraException;
import com.cobra.param.BaseResponse;
import com.cobra.constants.Constant;
import com.cobra.util.FileUtilsBean;
import com.cobra.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Api(value = "文件控制",description = "文件控制")
@RestController
@RequestMapping(value = "/api/img")
public class FileUploadController
{
    @Autowired private FileUtilsBean fileUtilsBean;


    @ApiOperation(value = "上传文件",notes = "上传文件")
    @PostMapping("/upload")
    public BaseResponse fileUpload(MultipartHttpServletRequest request) {
        String token = request.getHeader(Constant.token);
        if(StringUtils.isEmpty(token))
        {
           throw new CobraException("400","缺头");
        }
        MultipartFile file = request.getFile("file");
        if(file != null){
             //fileUtilsBean.fileUpload(file,"1234567890","img");
        }
        return ResponseUtil.success();
    }




}
