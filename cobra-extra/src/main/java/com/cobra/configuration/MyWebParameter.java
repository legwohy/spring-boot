package com.cobra.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * 配置Servlet映射
 */
public class MyWebParameter implements WebParameter
{
    /**
     * 每个基于servlet的web应用 均有一个s servletContext 对象保存上下文信息
     * 包括初始化参数 filter listener 以及容器信息
     * @param servletContext
     */
    @Override
    public void loadOnStartup(ServletContext servletContext)
    {
        // 动态添加
        ServletRegistration.Dynamic testServlet = servletContext.addServlet("test","com.cobra.servlet.MyServlet");
        // 执行顺序
        testServlet.setLoadOnStartup(1);
        testServlet.addMapping("/test");


    }
}
