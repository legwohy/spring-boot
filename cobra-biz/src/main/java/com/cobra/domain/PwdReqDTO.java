package com.cobra.domain;

import lombok.Data;

/**
 * @author admin
 * @date 2021/1/14 15:28
 * @desc
 */
@Data
public class PwdReqDTO {
    private String signType;
    private String name;
    /**
     * 私钥 可选参数 参与签名
     */
    private String privateKey;
    private String sign;

    /**
     * 加密方式
     */
    private String encType;

    /**
     * 公钥加密
     */
    private String pubKey;

    /**
     * 加密的内容
     */
    private String content;

    /**
     * 加密参数
     */
    private String k1;
    private String k2;
}
