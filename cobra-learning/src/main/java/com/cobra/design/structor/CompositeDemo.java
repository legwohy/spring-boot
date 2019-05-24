package com.cobra.design.structor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 组合模式
 *  1）执行组合对象如同执行其元素对象（多个元素迭代执行）
 *  2）组合对象和被组合对象可能是相同类型
 *  3）EL、Spring Cache、Spring MVC
 * {@link javax.el.CompositeELResolver}
 * {@link  org.springframework.cache.support.CompositeCacheManager}
 * {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurerComposite}
 */
public class CompositeDemo
{
    private static interface A{
        void save();
    }
    private static class AImpl implements A{
        @Override
        public void save(){System.out.println("save()");}
    }
    /** 组合模式 相同的对象 继承可有可无 主要是成员变量这块*/
    private static class CompositeA implements A{
        // 此对象需与当前对象一致 可以是数组 可以是集合
        private Collection<A> list = new ArrayList<>();
        /** 同一个入口 批量处理 具备相同动作的集合对象*/
        @Override public void save(){list.forEach(a->{a.save();});}
    }
}
