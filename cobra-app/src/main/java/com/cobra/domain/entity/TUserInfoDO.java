package com.cobra.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@Getter
@Setter
@ToString
@TableName("t_user_info")
public class TUserInfoDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userId;

    private String userName;

    private Date createdAt;

    @TableField(exist=false)
   private String test;
}