package com.cobra.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author admin
 * @date 2021/2/4 17:08
 * @desc
 */
@Data
public class TmpReqDTO implements Serializable{
    private static final long serialVersionUID = -6503840586884136765L;
    private String req1;
    private String req2;
}
