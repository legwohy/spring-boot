package com.cobra.domain;

import lombok.Data;

/**
 * @author admin
 * @date 2021/1/14 15:28
 * @desc
 */
@Data
public class PwdReqDTO {
    private String id;
    private String name;
    private String sign;

    /**
     * 加密的内容
     */
    private String content;
}
