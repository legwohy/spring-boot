package com.cobra.design.listener;


/**
 * 事件监听器
 *
 * 监听Person事件源的eat和sleep方法
 */
public interface PersonListener{

    void doEat(Event event);
    void doSleep(Event event);
}
