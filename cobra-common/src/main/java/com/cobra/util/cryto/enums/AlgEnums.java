package com.cobra.util.cryto.enums;

import lombok.Getter;

/**
 * 算法枚举
 */
@Getter
public enum AlgEnums
{
    AES("AES", 128, "USA","AES算法"),
    DES("DES", 56, "USA","DES算法"),
    DESede("DESede",168,"USA","3DES算法"),
    RSA("RSA",1024,"USA","RSA算法"),
    SM4("SM4",128,"CN","SM4国密算法"),
    SM2("SM2",1024,"CN","SM2国密算法"),
    ;
    private String code;
    private int keyLength;
    private String desc;
    private String nation;

    /**
     *
     * @param code 算法名称
     * @param keyLength 算法长度
     * @param nation 是否国产
     * @param desc 中文描述
     */
    AlgEnums(String code, int keyLength, String nation,String desc)
    {
        this.code = code;
        this.keyLength = keyLength;
        this.nation = nation;
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

    /**
     *
     * @param code
     * @return
     */
    public static String getNation(String code)
    {
        for (AlgEnums algEnums : AlgEnums.values())
        {
            if (algEnums.getCode().equalsIgnoreCase(code))
            {
                return algEnums.nation;
            }
        }
        return null;
    }
}
