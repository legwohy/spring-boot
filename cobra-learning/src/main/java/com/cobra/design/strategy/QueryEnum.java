package com.cobra.design.strategy;

/**
 * Created by legwo on 2018/12/20.
 */
public enum  QueryEnum
{
    A("a","AA"),
    B("b","BB"),
    C("c","CC");
    private String name;
    private String desc;
     QueryEnum(String name,String desc){
         this.name = name;
         this.desc = desc;
     }

    public static String getDescByName(final String name)
    {
        for (final QueryEnum e : QueryEnum.values())
        {
            if (e.getName() == name)
            {
                return e.desc;
            }
        }
        return null;
    }

    public String getName()
    {
        return name;
    }

    public String getDesc()
    {
        return desc;
    }


}
