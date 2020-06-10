package com.cobra.util.balance;

import java.util.List;

/**
 * 负载均衡算法
 */
public abstract class AbstractLoadBalance {
    protected abstract <T> StrategyWrapper<T> doSelect(List<StrategyWrapper<T>> strategies);
    /**
     * 获取权重
     * @param strategy
     * @return
     */
    protected int getWeight(StrategyWrapper<?> strategy){
        return strategy.getWeight();
    }
}
