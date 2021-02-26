package com.cobra.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author admin
 * @date 2021/2/4 17:08
 * @desc
 */
@Data
public class TmpRspDTO implements Serializable{
    private static final long serialVersionUID = 3414475876844969819L;
    private String rsp1;
    private String rsp2;
}
