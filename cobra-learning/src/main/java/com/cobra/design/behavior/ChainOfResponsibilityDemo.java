package com.cobra.design.behavior;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 责任链模式  filter1-->filter2-->filter3-->...servlet
 * 当前节点的输入是上一节点的输出 阻塞的，有可能中途被阻断
 *
 * {@link javax.servlet.Filter}
 * @see java logging
 * 注意 多个filter是有顺序的 filter过滤的是url
 */
public class ChainOfResponsibilityDemo implements javax.servlet.Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException
    {
        if(true){return;}// 条件式阻断链条
        // 如果满足条件 直接返回i否则继续执行下一个链
        // before 前置 执行servlet之前
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "*");// 允许跨域
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request,response);// 执行servlet

        // after 后置 执行完servlet之后


    }

    @Override
    public void destroy()
    {

    }
}
