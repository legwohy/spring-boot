package com.cobra.jvm.inst;

import java.lang.instrument.Instrumentation;

/**
 * JDK6提供
 * 该代理
 * 1、必须在manifest中指定代理 Agent-class:代理类全类名
 * 2、必须提供agentmain方法
 * 3、可以在代理类启动之后在启动
 */
public class AgentForAgentmain {
    @SuppressWarnings("rawtypes")
    public static void agentmain(String args, Instrumentation inst) {
        Class[] classes = inst.getAllLoadedClasses();
        for(Class cls :classes){
            System.out.println(cls.getName());
        }

    }
}
