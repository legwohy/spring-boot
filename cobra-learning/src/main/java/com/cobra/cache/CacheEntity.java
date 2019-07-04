package com.cobra.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 1、缓存实体
 * 2、key-value，如果value为空，就从db读取数据，并缓存
 * 3、一般缓存都是有时效的。超过失效会自动清空
 */
@Getter
@Setter
@AllArgsConstructor
public class CacheEntity implements Serializable
{
    /** 序列化和反序列化时，若版本不一致 将无法反序列化 读取对象*/
    private static final long serialVersionUID = -6821537108160494185L;

    /** 缓存的值*/
    private Object value;

    /** 当前时间戳 单位是秒 */
    private Long gmtModify;

    /** 过期时间*/
    private int expireTime;
}
