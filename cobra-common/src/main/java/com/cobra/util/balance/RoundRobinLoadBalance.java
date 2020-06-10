package com.cobra.util.balance;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 轮询权重算法
 * 轮询调度算法的原理是每一次把来自用户的请求轮流分配给内部中的服务器，
 * 从1开始，直到N(内部服务器个数)，然后重新开始循环
 *
 * 这里的权重值就相当于改服务器加一
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance{

    private final ConcurrentMap<String, AtomicPositiveInteger> sequences = new ConcurrentHashMap<String, AtomicPositiveInteger>();

    @Override
    protected <T> StrategyWrapper<T> doSelect(List<StrategyWrapper<T>> strategies) {
        String key = strategies.get(0).getKey();
        int length = strategies.size();
        int maxWeight = 0;
        int minWeight = Integer.MAX_VALUE;
        final LinkedHashMap<StrategyWrapper<T>, IntegerWrapper> invokerToWeightMap = new LinkedHashMap<StrategyWrapper<T>, IntegerWrapper>();
        int weightSum = 0;
        for (int i = 0; i < length; i++) {
            int weight = getWeight(strategies.get(i));
            maxWeight = Math.max(maxWeight, weight);
            minWeight = Math.min(minWeight, weight);
            if (weight > 0) {
                invokerToWeightMap.put(strategies.get(i), new IntegerWrapper(weight));
                weightSum += weight;
            }
        }
        AtomicPositiveInteger sequence = sequences.get(key);
        if (sequence == null) {
            sequences.putIfAbsent(key, new AtomicPositiveInteger());
            sequence = sequences.get(key);
        }
        int currentSequence = sequence.getAndIncrement();
        if (maxWeight > 0 && minWeight < maxWeight) {
            int mod = currentSequence % weightSum;
            for (int i = 0; i < maxWeight; i++) {
                for (Map.Entry<StrategyWrapper<T>, IntegerWrapper> each : invokerToWeightMap.entrySet()) {
                    final StrategyWrapper<T> k = each.getKey();
                    final IntegerWrapper v = each.getValue();
                    if (mod == 0 && v.getValue() > 0) {
                        return k;
                    }
                    if (v.getValue() > 0) {
                        v.decrement();
                        mod--;
                    }
                }
            }
        }
        // Round robin
        return strategies.get(currentSequence % length);
    }



    private static final class IntegerWrapper {
        private int value;

        public IntegerWrapper(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @SuppressWarnings("unused")
        public void setValue(int value) {
            this.value = value;
        }

        public void decrement() {
            this.value--;
        }
    }
}


class AtomicPositiveInteger extends Number{
    private static final long serialVersionUID = -3038533876489105940L;

    private static final AtomicIntegerFieldUpdater<AtomicPositiveInteger> indexUpdater =
            AtomicIntegerFieldUpdater.newUpdater(AtomicPositiveInteger.class, "index");

    @SuppressWarnings("unused")
    private volatile int index = 0;

    public AtomicPositiveInteger() {
    }

    public AtomicPositiveInteger(int initialValue) {
        indexUpdater.set(this, initialValue);
    }

    public final int getAndIncrement() {
        return indexUpdater.getAndIncrement(this) & Integer.MAX_VALUE;
    }

    public final int getAndDecrement() {
        return indexUpdater.getAndDecrement(this) & Integer.MAX_VALUE;
    }

    public final int incrementAndGet() {
        return indexUpdater.incrementAndGet(this) & Integer.MAX_VALUE;
    }

    public final int decrementAndGet() {
        return indexUpdater.decrementAndGet(this) & Integer.MAX_VALUE;
    }

    public final int get() {
        return indexUpdater.get(this) & Integer.MAX_VALUE;
    }

    public final void set(int newValue) {
        if (newValue < 0) {
            throw new IllegalArgumentException("new value " + newValue + " < 0");
        }
        indexUpdater.set(this, newValue);
    }

    public final int getAndSet(int newValue) {
        if (newValue < 0) {
            throw new IllegalArgumentException("new value " + newValue + " < 0");
        }
        return indexUpdater.getAndSet(this, newValue) & Integer.MAX_VALUE;
    }

    public final int getAndAdd(int delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("delta " + delta + " < 0");
        }
        return indexUpdater.getAndAdd(this, delta) & Integer.MAX_VALUE;
    }

    public final int addAndGet(int delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("delta " + delta + " < 0");
        }
        return indexUpdater.addAndGet(this, delta) & Integer.MAX_VALUE;
    }

    public final boolean compareAndSet(int expect, int update) {
        if (update < 0) {
            throw new IllegalArgumentException("update value " + update + " < 0");
        }
        return indexUpdater.compareAndSet(this, expect, update);
    }

    public final boolean weakCompareAndSet(int expect, int update) {
        if (update < 0) {
            throw new IllegalArgumentException("update value " + update + " < 0");
        }
        return indexUpdater.weakCompareAndSet(this, expect, update);
    }

    @Override
    public byte byteValue() {
        return (byte) get();
    }

    @Override
    public short shortValue() {
        return (short) get();
    }

    @Override
    public int intValue() {
        return get();
    }

    @Override
    public long longValue() {
        return (long) get();
    }

    @Override
    public float floatValue() {
        return (float) get();
    }

    @Override
    public double doubleValue() {
        return (double) get();
    }

    @Override
    public String toString() {
        return Integer.toString(get());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + get();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AtomicPositiveInteger)) return false;
        AtomicPositiveInteger other = (AtomicPositiveInteger) obj;
        return intValue() == other.intValue();
    }
}
