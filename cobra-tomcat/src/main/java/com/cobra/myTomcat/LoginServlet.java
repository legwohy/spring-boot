package com.cobra.myTomcat;

import java.io.IOException;

/**
 * 登陆请求
 */
public class LoginServlet implements HttpServlet
{
    @Override
    public void server(HttpRequest request, HttpResponse response) throws IOException
    {
        String userName = request.getParamter("userName");
        String pwd = request.getParamter("pwd");
        if("sam".equals(userName) && "456".equals(pwd))
        {
            response.writeFile("htmlfile/welcome.html");

        }else {
            response.writeFile("htmlfile/error.html");
        }

    }
}
