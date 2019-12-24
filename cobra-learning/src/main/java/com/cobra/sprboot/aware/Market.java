package com.cobra.sprboot.aware;


/**
 * 实体类Market实现接口AppleAware获取apple对象
 */
public class Market implements AppleAware
{
    private Apple apple;
    @Override
    public void setApple(Apple apple)
    {
        this.apple = apple;
    }

    public String getName()
    {
        return apple.getName();
    }

    public void initMarket(){
        // 初始化方法在 postProcessBeforeInitialization 之后 postProcessAfterInitialization 之前运行
        System.out.println("调用 initMarket 方法==========");
    }

}
