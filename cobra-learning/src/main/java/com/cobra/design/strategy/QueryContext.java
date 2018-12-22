package com.cobra.design.strategy;

import com.cobra.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 上下文 作为中间层 加载bean
 */
@Service
public class QueryContext
{
    private Map<String,QueryProcessor> handleMap = new ConcurrentHashMap<>();

    /**
     * list 循环注入
     * @param queryProcessors
     */
    //@Autowired
    public void init(List<QueryProcessor> queryProcessors)
    {
        queryProcessors.forEach(queryProcessor -> handleMap.put(queryProcessor.getName(),queryProcessor));
    }

    /**
     * map 循环注入
     * @param map
     */
    @Autowired
    public void init(Map<String,QueryProcessor> map)
    {
        handleMap.clear();
        map.forEach((name,queryProcessor)->this.handleMap.put(queryProcessor.getName(),queryProcessor));

    }

    public Map<String,String> query(String type)
    {
        Map<String,String> request = new HashMap<>();
        Map<String,String> result = new HashMap<>();
        if(!StringUtils.isBlank(type))
        {
            request.put("type",type);
        }
        handleMap.get(type).handle(request,result);

        return result;


    }
}
