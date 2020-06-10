package com.cobra.util.balance;

import lombok.Data;

@Data
public class StrategyWrapper<T> {

    private T node;
    /**
     * 权重
     */
    private int weight;
    /**
     * key
     */
    private String key;
}
