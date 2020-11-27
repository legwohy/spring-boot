package com.cobra.spring.sprboot.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by legwo on 2018/12/20.
 */
@Service
public class QueryCProcessor implements QueryProcessor
{
    @Override
    public String getName()
    {
        return QueryEnum.C.getName();
    }

    @Override
    public boolean check(Map<String, String> request, Map<String, String> result)
    {
        if(StringUtils.equals(request.get("type"),"b"))
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
            result.put("b","this is CC");

            System.out.println("CCCCCCCCCCCCCCCC");

        }

    }
}
