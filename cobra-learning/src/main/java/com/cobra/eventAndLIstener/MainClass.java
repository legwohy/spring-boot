package com.cobra.eventAndLIstener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

/**
 * Spring事件 ApplicationEvent
 * Spring监听器 ApplicationListener
 * Ctrl+F12 查找方法
 */
public class MainClass
{
    public static void main(String[] args)
    {
        // 广播器
        ApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();

        // 添加监听器
        multicaster.addApplicationListener(event->{
            if(event instanceof PayloadApplicationEvent){
                System.out.println("接收到payLoadApplicationEvent "+PayloadApplicationEvent.class.cast(event).getPayload());
            }else {
                System.out.println("接收到事件 "+event);
            }
        });

        // 发布广播事件
        multicaster.multicastEvent(new PayloadApplicationEvent<Object>(2,"hello"));
        multicaster.multicastEvent(new PayloadApplicationEvent<Object>(1,"world"));

    }

    /**
     * 自定义事件
     */
    private static class MyEvent extends ApplicationEvent{
        public MyEvent(Object source){super(source);}
    }


}
