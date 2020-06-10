package com.cobra.util.balance;

import java.util.List;
import java.util.Random;

/**
 * 负载均衡-随机算法
 */
public class RandomLoadBalance extends AbstractLoadBalance{
    /**
     * 随机数生成器
     */
    private final Random random = new Random();
    @Override
    protected <T> StrategyWrapper<T> doSelect(List<StrategyWrapper<T>> strategies) {
        int length = strategies.size();
        int totalWeight = 0;
        boolean sameWeight = true;
        for (int i = 0; i < length; i++) {
            int weight = getWeight(strategies.get(i));
            totalWeight += weight;
            if (sameWeight && i > 0 && weight != getWeight(strategies.get(i - 1))) {
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            int offset = random.nextInt(totalWeight);
            for (int i = 0; i < length; i++) {
                offset -= getWeight(strategies.get(i));
                if (offset < 0) {
                    return strategies.get(i);
                }
            }
        }
        return strategies.get(random.nextInt(length));
    }
}
