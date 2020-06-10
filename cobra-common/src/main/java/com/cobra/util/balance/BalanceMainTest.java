package com.cobra.util.balance;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试类
 */
public class BalanceMainTest {
    public static void main(String[] args) {
        // 轮询算法
        //AbstractLoadBalance balanceTest = new RoundRobinLoadBalance();
        // 随机算法
        AbstractLoadBalance balanceTest = new RandomLoadBalance();


        StrategyWrapper<String> s1 = new StrategyWrapper<>();
        s1.setKey("k1");
        s1.setNode("n1");
        s1.setWeight(1);
        StrategyWrapper<String> s2 = new StrategyWrapper<>();
        s2.setKey("k2");
        s2.setNode("n2");
        s2.setWeight(2);
        List<StrategyWrapper<String>> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);

        for (int i = 0;i<3;i++){
            new Thread(()->{
                StrategyWrapper strategyWrapper =  balanceTest.doSelect(list);
                System.out.println("策略:"+strategyWrapper);
            }).start();
        }



    }
}
