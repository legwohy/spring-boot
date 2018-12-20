package com.cobra.memory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by legwo on 2018/12/12.
 */
public class CollectionMemory
{
    public static void main(String[] args)
    {
        Set<MyObject> objects = new LinkedHashSet<>();
        objects.add(new MyObject());
        objects.add(new MyObject());
        objects.add(new MyObject());
        objects.add(new MyObject());

        System.out.println("size="+objects.size());

        // 死循环
        while (true){
            objects.add(new MyObject());
        }

    }
}
class MyObject{

    private List<String> list =new ArrayList<>(666);
}
