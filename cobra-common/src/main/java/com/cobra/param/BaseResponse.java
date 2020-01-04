package com.cobra.param;


import com.cobra.constants.CobraCode;

import java.io.Serializable;

/**
 * @author admin
 */
@SuppressWarnings("rawType")
public class BaseResponse<T> implements Serializable
{
    private static final long serialVersionUID = -7979641854729188260L;
    private Boolean success;
    private String code;
    private String msg;
    private T data;


    public BaseResponse(String code,String msg){
        success = false;
        this.code = code;
        this.msg = msg;
    }


    /**
     * 请求成功
     * @param data
     */
    public BaseResponse(T data){
        success = true;
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

    public Object getData()
    {
        return data;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean isSuccess(){
        return success;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
