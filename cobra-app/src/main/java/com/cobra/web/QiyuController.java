package com.cobra.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cobra.constants.Constant;
import com.cobra.constants.QiyuConstant;
import com.cobra.dto.ApplyStaffDto;
import com.cobra.dto.EvaluationDto;
import com.cobra.dto.QiyuMessageDto;
import com.cobra.exception.CobraException;
import com.cobra.param.BaseResponse;
import com.cobra.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author  admin
 */
@Slf4j
@Api("客服系统")
@RestController
@RequestMapping("/api/kefu")
public class QiyuController
{
    @ApiOperation(value = "分配客服")
    @RequestMapping("/distribute")
    public BaseResponse distribute(@RequestBody ApplyStaffDto applyStaffDto){

        String content = JSON.toJSONString(applyStaffDto);

        long time = DateUtils.getUTCTime();
        String url = QiyuConstant.URL_APPLY_STAFF
                .concat("appKey="+QiyuConstant.APP_KEY)
                .concat("&time="+ time).concat("&checksum="+ QiyuPushCheckSum.encode(QiyuConstant.APP_SECRET, MD5.md5(content),time));
        String rs = OkHttpUtil.post(url,content);

        JSONObject jsonObject = JSON.parseObject(rs);
        if(!String.valueOf(HttpStatus.SC_OK).equals(jsonObject.getString(Constant.code)))
        {
            throw new CobraException(jsonObject.getString(Constant.code),jsonObject.getString(Constant.message));
        }

        return ResponseUtil.success(rs);
    }


    @ApiOperation(value = "发送消息")
    @RequestMapping("/sendMessage")
    public BaseResponse sendMessage(@RequestBody QiyuMessageDto qiyuMessageDto){

      String content = JSON.toJSONString(qiyuMessageDto);
      long time = DateUtils.getUTCTime();

        String url = QiyuConstant.URL_SEND_MESSAGE
                .concat("appKey="+ QiyuConstant.APP_KEY)
                .concat("&time="+ time)
                .concat("&checksum="+ QiyuPushCheckSum.encode(QiyuConstant.APP_SECRET, MD5.md5(content),time));
        String rs = OkHttpUtil.post(url,content);
        JSONObject jsonObject = JSON.parseObject(rs);

        if(!String.valueOf(HttpStatus.SC_OK).equals(jsonObject.getString(Constant.code)))
        {
            throw new  CobraException(jsonObject.getString(Constant.code),jsonObject.getString(Constant.message));
        }

        return ResponseUtil.success(content);

    }

    @ApiOperation(value = "评价客服")
    @RequestMapping("/evaluate")
    public BaseResponse evaluate(@RequestBody EvaluationDto evaluationDto){

        String content = JSON.toJSONString(evaluationDto);
        long time = DateUtils.getUTCTime();

        String url = QiyuConstant.URL_EVALUATE
                .concat("appKey="+ QiyuConstant.APP_KEY)
                .concat("&time="+ time)
                .concat("&checksum="+ QiyuPushCheckSum.encode(QiyuConstant.APP_SECRET, MD5.md5(content),time));
        String rs = OkHttpUtil.post(url,content);
        JSONObject jsonObject = JSON.parseObject(rs);

        if(!String.valueOf(HttpStatus.SC_OK).equals(jsonObject.getString(Constant.code)))
        {
            throw new  CobraException(jsonObject.getString(Constant.code),jsonObject.getString(Constant.message));
        }

        return ResponseUtil.success(content);

    }





}
