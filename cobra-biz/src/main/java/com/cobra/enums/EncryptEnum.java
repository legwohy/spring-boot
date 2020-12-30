package com.cobra.enums;

import lombok.Getter;

/**
 * @author admin
 * @date 2020/12/30 15:39
 * @desc
 */
@Getter
public enum  EncryptEnum {
    MD5("MD5","MD5签名"),
    HEX("HEX","十六进制"),
    BASE64_ENCODE("BASE64_ENCODE","编码"),
    BASE64_DECODE("BASE64_DECODE","解码"),
    ;
    private String code;
    private String desc;
    EncryptEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }
}
