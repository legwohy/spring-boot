package com.cobra.basic;

/**
 * 钩子实现
 */
public class MainTest extends AbstractCalc
{
    public static void main(String[] args)
    {
       MainTest test = new MainTest();
       test.calc();
    }

    @Override
    public void add()
    {

    }
}
interface Calc{
    void add();
    void minus();
    void multi();
    void div();
}

abstract class AbstractCalc implements Calc{
    /** 抽象方法必须实现*/
    public abstract void add();
    /** fina修饰 必须控制 钩子方法add流程 但不是具体实现*/
    public final void calc(){
        add();
    }

    /** 空实现*/
    @Override
    public void minus(){}

    @Override
    public void div(){}

    @Override
    public void multi(){}
}



