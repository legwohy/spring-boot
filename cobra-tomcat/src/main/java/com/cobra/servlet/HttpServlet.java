package com.cobra.servlet;

import com.cobra.myTomcat.HttpRequest;
import com.cobra.myTomcat.HttpResponse;

import java.io.IOException;

/**
 * 所有servlet的根类
 */
public interface HttpServlet
{
    void server(HttpRequest request,HttpResponse response)throws IOException;
}
