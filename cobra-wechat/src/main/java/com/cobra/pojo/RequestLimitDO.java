package com.cobra.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * api请求限制模型
 */
@Data
public class RequestLimitDO implements Serializable
{
    private static final long serialVersionUID = -7423421740834049772L;

    /**
     * 最大请求次数
     */
    private long limit;

    /**
     * 请求时间内限制的次数
     */
    private long time;

    /**
     * 时间单位
     */
    private TimeUnit unit;
}
