package com.cobra.param;


import com.cobra.constants.CobraCode;

public class BaseResponse
{
    private String code;
    private String msg;
    private String data;

    public BaseResponse() {}

    public BaseResponse(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(String code, String msg, String data)
    {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 请求成功
     * @param data
     */
    public BaseResponse(String data){
        this.code = CobraCode.SUCCESS.getCode();
        this.msg = CobraCode.SUCCESS.getMsg();
        this.data = data;
    }

    public String getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }

    public String getData()
    {
        return data;
    }
}
