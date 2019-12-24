package com.cobra.design.behavior;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者模式
 * 观察者：包含一系列依赖
 * 自动通知观察者
 * {@link java.util.Observer} 观察者就是订阅者 被通知的对象
 * Observable是一个维护多个有序 Observer对象的集合 相当于subject publisher 发布者
 * Observer customize subscriber 订阅者
 */
public class ObserverDemo
{
    public static void main(String[] args)
    {
        // JDK存在观察者的实现 注意是倒叙的
        MyObservable observable = new MyObservable();
        // 注册观察者
        observable.addObserver(new Observer()
        {
            @Override
            public void update(Observable o, Object arg)
            {
                System.out.println("观察者一接收到通知:"+arg);
            }
        });
        observable.addObserver(new Observer()
        {
            @Override
            public void update(Observable o, Object arg)
            {
                System.out.println("观察者二接收到通知:"+arg);
            }
        });

        // 调整变化 先改变状态在通知
        observable.setChanged();

        // 通知变化到所有观察者
        observable.notifyObservers("打麻将");
    }

    private static class MyObservable extends Observable{
        @Override
        public synchronized void setChanged()
        {
            // 子类提升父类方法的访问权限 protected --  public
            super.setChanged();
        }
    }
}
