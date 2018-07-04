package com.cobra.constants;


public enum  CobraCode {

    SUCCESS("success","操作成功"),
    ERROR("error","发生错误"),
    MISSING_REQUIED_PARAM("missing_required_param","缺少必要参数"),
    WRONG_NAME_PASSWORD("wrong_name_password","用户名或密码错误");


    private String code;
    private String msg;
    CobraCode(){}
    CobraCode(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public String getCode()
    {
        return code;
    }


    public String getMsg()
    {
        return msg;
    }


}
