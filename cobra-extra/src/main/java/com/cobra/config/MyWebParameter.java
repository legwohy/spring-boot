package com.cobra.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * 配置Servlet映射
 */
public class MyWebParameter implements WebParameter
{
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
