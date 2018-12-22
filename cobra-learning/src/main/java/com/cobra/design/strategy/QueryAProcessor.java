package com.cobra.design.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by legwo on 2018/12/20.
 */
@Service
public class QueryAProcessor implements QueryProcessor
{
    @Override
    public String getName()
    {
        return QueryEnum.A.getName();
    }

    @Override
    public boolean check(Map<String, String> request, Map<String, String> result)
    {
        if(StringUtils.equals(request.get("type"),"a"))
        {
            return true;
        }

        return false;
    }

    @Override
    public void handle(Map<String, String> request, Map<String, String> result)
    {
        if(check(request,result))
        {
            result.put("a","this is AA");

            System.out.println("AAAAAAAAAAAAaa");

        }

    }
}
