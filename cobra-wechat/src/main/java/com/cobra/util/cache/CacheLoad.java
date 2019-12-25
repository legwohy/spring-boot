package com.cobra.util.cache;


import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Type;

/**
 * <T> 表示定义泛型
 * T 表示泛型的类型
 * DB查询
 *
 */
public abstract class CacheLoad<T> {
    public TypeReference<T> rawType() {
        return new TypeReference<T>() {};
    }

    public Type getType(){
        return rawType().getType();
    }

    public abstract T load();

}
