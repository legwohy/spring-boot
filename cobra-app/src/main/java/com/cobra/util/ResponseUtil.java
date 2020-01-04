package com.cobra.util;


import com.cobra.constants.CobraCode;
import com.cobra.param.BaseResponse;

/**
 * @author  admin
 */
public class ResponseUtil
{
    public static BaseResponse error(){
        return new BaseResponse(CobraCode.ERROR.getCode(),CobraCode.ERROR.getMsg());
    }

    public static BaseResponse error(String code,String msg){
        return new BaseResponse(code,msg);
    }

    public static BaseResponse success(){
        return new BaseResponse(CobraCode.SUCCESS.getCode(),CobraCode.SUCCESS.getMsg());
    }

    public static BaseResponse success(String data){
        return new BaseResponse(data);
    }
}
