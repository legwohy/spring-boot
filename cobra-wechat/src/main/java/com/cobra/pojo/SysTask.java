package com.cobra.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SysTask
{
    private Integer id;
    private String className;
    private String methodName;
    private String cron;
    private String taskName;
    private Date updateTime;
    private Date createTime;
}
