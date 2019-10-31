package com.cobra.param;


import com.cobra.constants.CobraCode;

/**
 * @author admin
 */
public class BaseResponse
{
    private String code;
    private String msg;
    private Object data;

    public BaseResponse() {}

    public BaseResponse(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(String code, String msg, Object data)
    {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 请求成功
     * @param data
     */
    public BaseResponse(Object data){
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

    public void setData(Object data)
    {
        this.data = data;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public boolean isSuccess(){
        return CobraCode.SUCCESS.getCode().equals(code);
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
