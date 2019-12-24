package com.cobra.sprboot.strategy;

import java.util.Map;

/**
 * Created by legwo on 2018/12/20.
 */
public interface QueryProcessor
{
    String getName();
    boolean check(Map<String,String> request,Map<String,String> result);

    void handle(Map<String,String> request,Map<String,String> result);
}
