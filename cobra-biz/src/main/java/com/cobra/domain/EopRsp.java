package com.cobra.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author admin
 * @date 2021/2/26 11:16
 * @desc
 */
@Data
public class EopRsp implements Serializable {
    private static final long serialVersionUID = -467855317519069445L;
    private String code;
    private String msg;
    private String data;

}
