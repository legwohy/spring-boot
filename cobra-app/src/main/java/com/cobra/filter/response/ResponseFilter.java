package com.cobra.filter.response;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cobra.param.BaseResponse;
import com.cobra.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * <a link='https://blog.csdn.net/hehuanchun0311/article/details/80513558'/>
 * 其实Spring中，web应用启动的顺序是：listener->filter->servlet，先初始化listener，然后再来就filter的初始化，再接着才到我们的dispathServlet的初始化，因此，当我们需要在filter里注入一个注解的bean时，
 * 就会注入失败，因为filter初始化时，注解的bean还没初始化，没法注入
 * 推荐 extend OncePerRequestFilter 该类 保证在不同的容器中只执行一次
 */
@Component
public class ResponseFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseFilter.class);
    private String key = "userName";

    @Autowired
    private TestFilterAutoBean testFilterAutoBean;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse) response);
        try {

            if(null == testFilterAutoBean){
                testFilterAutoBean = (TestFilterAutoBean) SpringContextHolder.getBean(TestFilterAutoBean.class);
            }
            System.out.println("测试自动注入:"+ testFilterAutoBean.getId());

            filterChain.doFilter(request, wrapper);

            String respStr = new String(wrapper.toByteArray(), response.getCharacterEncoding());

            JSONObject jsonObject = JSON.parseObject(respStr);
            if ("true".equalsIgnoreCase(jsonObject.getString("success"))) {
                Object result = jsonObject.get("data");

                // 解析
                this.replaceArr(result);
            }

            response.setContentType("application/json;charset=utf-8");
            //将buffer重置，因为我们要重新写入流进去
            response.resetBuffer();
            response.setContentLength(JSON.toJSONBytes(jsonObject).length);
            response.getOutputStream().write(JSON.toJSONBytes(jsonObject));
        } catch (Exception e) {
            LOGGER.error("数据包装器执行出错....{}", e);
            response.setContentType("application/json;charset=utf-8");
            //将buffer重置，因为我们要重新写入流进去
            response.resetBuffer();

            response.setContentLength(JSON.toJSONBytes(new BaseResponse("404","系统异常")).length);
            response.getOutputStream().write(JSON.toJSONBytes(new BaseResponse("404","系统异常")));
        }
    }

    @Override
    public void init(FilterConfig arg0)
            throws ServletException {

    }

    @Override
    public void destroy() {

    }


    private Object replaceArr(Object result) {
        if (result instanceof JSONObject) {
            JSONObject resultJson = (JSONObject) result;
            for (Map.Entry<String, Object> en : resultJson.entrySet()) {
                Object vue = en.getValue();
                if (key.equals(en.getKey())) {
                    // 替换
                    en.setValue("XXX_" + en.getValue());
                }else{
                    if (vue instanceof JSONObject) {
                        this.replaceArr(vue);
                    } else if (vue instanceof JSONArray) {
                        // 数组
                        JSONArray array = (JSONArray) vue;
                        for (int i = 0; i < array.size(); i++) {
                            Object arrObj = array.get(i);

                            this.replaceArr(arrObj);
                        }

                    }
                }
            }

        } else if (result instanceof JSONArray) {
            JSONArray array = (JSONArray) result;
            for (int i = 0; i < array.size(); i++) {
                JSONObject arrObj = (JSONObject) array.get(i);

                this.replaceArr(arrObj);
            }
        }
        return result;
    }


}


