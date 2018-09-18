package com.cobra.config;

import javax.servlet.ServletContext;

/**
 * 参数 类似 web.xml
 */
public interface WebParameter
{
    void loadOnStartup(ServletContext servletContext);
}
