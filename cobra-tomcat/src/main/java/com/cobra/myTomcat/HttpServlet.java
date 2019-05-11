package com.cobra.myTomcat;

import java.io.IOException;

/**
 * 所有servlet的根类
 */
public interface HttpServlet
{
    void server(HttpRequest request,HttpResponse response)throws IOException;
}
