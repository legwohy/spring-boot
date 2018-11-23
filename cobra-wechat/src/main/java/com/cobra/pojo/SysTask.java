package com.cobra.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysTask {
    private Integer id;

    private String taskName;

    private String taskGroup;

    private String cron;

    private String className;

    private String methodName;

    private String parameter;

    private String description;

    private Integer status;

    private Date updateTime;

    private Date createTime;

}