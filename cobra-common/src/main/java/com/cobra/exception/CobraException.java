package com.cobra.exception;


import com.cobra.constants.CobraCode;

public class CobraException extends RuntimeException
{
    private String code;

    public CobraException(String code,String msg){
        super(msg);
        this.code = code;
    }

    public CobraException(CobraCode cobraCode,String msg){
        super(cobraCode.getMsg()+"["+msg+"]");
        this.code = cobraCode.getCode();
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}
