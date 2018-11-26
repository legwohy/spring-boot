package com.cobra.service;

import lombok.NonNull;

/**
 * 所有的定时任务需要实现此接口
 */
public interface MyTask
{
    /**
     * 必须指定任务名称 且所有任务唯一
     * 定时任务名称在配置文件中配置
     */
    void excute();
}
