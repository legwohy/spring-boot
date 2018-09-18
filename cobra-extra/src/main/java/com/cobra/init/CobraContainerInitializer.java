package com.cobra.init;

import com.cobra.config.WebParameter;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Set;

/**
 * 自定义容器会扫
 * 使用 @HandlesTypes 将WebParameter 做为参数传递给onStartup
 */
@HandlesTypes({WebParameter.class})
public class CobraContainerInitializer implements ServletContainerInitializer
{

    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException
    {
        System.out.println("===============onStartup=======================");
        Iterator var4;
        if (set != null){
            var4 = set.iterator();
            while(var4.hasNext()){
                Class<?> clazz= (Class<?>) var4.next();
                // 非接口 非抽象类 实现类
                if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers()) && WebParameter.class.isAssignableFrom(clazz)){
                    try {
                        ((WebParameter) clazz.newInstance()).loadOnStartup(servletContext);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }



}
