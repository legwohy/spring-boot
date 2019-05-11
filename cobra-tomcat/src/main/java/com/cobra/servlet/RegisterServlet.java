package com.cobra.servlet;

import com.cobra.myTomcat.HttpRequest;
import com.cobra.myTomcat.HttpResponse;

import java.io.IOException;

/**
 * 登陆请求
 */
public class RegisterServlet implements HttpServlet
{
    @Override
    public void server(HttpRequest request, HttpResponse response) throws IOException
    {

        response.writeFile("htmlfile/registerSuccess.html");

    }
}
