package com.cobra.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author admin
 * @date 2021/2/26 11:16
 * @desc
 */
@Data
public class EopReq implements Serializable {
    private static final long serialVersionUID = 267434954573737192L;
    private String skey;
    private String token;
    private String timestamp;
    private String seqid;
    private String sign;
    private String user_id;
    private String key_id;
    private String service_id;
    private String month_id;
    private String accs_nbr;
    private String model;

}
