package com.cobra.design.behavior;

import com.sun.istack.internal.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器模式
 *
 * cglib使用类做代理而jdk只能使用接口做动态代理，
 * 因此 spring是使用cglib和jdk的动态代理实现aop
 *
 * springBoot 2.0 --spring framework5.0--java8 拦截器是有默认实现的 本例子使用1.5.9.RELEASE
 * 拦截器 ：前置 后置 异常
 * {@link org.springframework.aop.BeforeAdvice}
 */
public class InterceptorDemo implements HandlerInterceptor// handler相当于url 最后要映射到相应的方法上 方法的前后以及异常
{

    /**
     * 前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        return false;
    }

    /**
     * 后置
     * @param modelAndView 试图
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                    ModelAndView modelAndView) throws Exception
    {

    }

    /**
     * 完成后 这里有可能产生异常
     * @param ex
     * @throws Exception @Nullable 异常可以有可以无
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                    @Nullable Exception ex) throws Exception
    {

    }
}
