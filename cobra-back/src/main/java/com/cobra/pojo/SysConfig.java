package com.cobra.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SysConfig {
    private Integer id;

    private String sysKey;

    private String sysValue;

    private String sysType;

    private Date updateTime;

    private Date createTime;

}