package com.cobra.util.cryto.enums;

import lombok.Getter;

/**
 * 算法枚举
 */
@Getter
public enum AlgEnums
{
    AES("AES", 128, "AES算法"),
    DES("DES", 56, "DES算法"),
    DESede("DESede",168,"3DES算法"),
    RSA("RSA",1024,"RSA算法"),
    ;
    private String code;
    private int keyLength;
    private String desc;

    /**
     *
     * @param code 算法名称
     * @param keyLength 算法长度
     * @param desc 中文描述
     */
    AlgEnums(String code, int keyLength, String desc)
    {
        this.code = code;
        this.keyLength = keyLength;
        this.desc = desc;
    }

    public static int getLength(String code)
    {
        for (AlgEnums algEnums : AlgEnums.values())
        {
            if (algEnums.getCode().equalsIgnoreCase(code))
            {
                return algEnums.keyLength;
            }
        }
        return 0;
    }
}
