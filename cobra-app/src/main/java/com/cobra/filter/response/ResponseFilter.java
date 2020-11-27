package com.cobra.filter.response;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cobra.param.BaseResponse;
import com.cobra.bean.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Slf4j
@Component
public class ResponseFilter implements Filter {
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
            log.info("测试自动注入 autoBeanId=[{}]:", testFilterAutoBean.getId());

            filterChain.doFilter(request, wrapper);

            String respStr = new String(wrapper.toByteArray(), response.getCharacterEncoding());

            JSONObject jsonObject = JSON.parseObject(respStr);
            if ("true".equalsIgnoreCase(jsonObject.getString("success"))) {
                Object result = jsonObject.get("data");

                // 解析
                this.parseJson(result);
            }

            response.setContentType("application/json;charset=utf-8");
            //将buffer重置，因为我们要重新写入流进去
            response.resetBuffer();
            response.setContentLength(JSON.toJSONBytes(jsonObject).length);
            response.getOutputStream().write(JSON.toJSONBytes(jsonObject));
        } catch (Exception e) {
            log.error("数据包装器执行出错....{}", e);
            response.setContentType("application/json;charset=utf-8");
            //将buffer重置，因为我们要重新写入流进去
            response.resetBuffer();

            response.setContentLength(JSON.toJSONBytes(new BaseResponse("301","响应拦截异常")).length);
            response.getOutputStream().write(JSON.toJSONBytes(new BaseResponse("301","响应拦截异常")));
        }
    }

    @Override
    public void init(FilterConfig arg0)
            throws ServletException {

    }

    @Override
    public void destroy() {

    }


    /**
     * 解析json
     * @param result 必须是转换为 JSONObject 或 JSONArray对象
     * @return
     */
    private Object parseJson(Object result) {
        if (result instanceof JSONObject) {
            JSONObject resultJson = (JSONObject) result;
            for (Map.Entry<String, Object> en : resultJson.entrySet()) {
                Object vue = en.getValue();
                if (key.equals(en.getKey())) {
                    // 替换 所有的去处
                    en.setValue("J_Q_K_" + en.getValue());
                }else{
                    if (vue instanceof JSONObject) {
                        this.parseJson(vue);
                    } else if (vue instanceof JSONArray) {
                        // 数组 递归到外层数组
                        this.parseJson(vue);
                    }
                }
            }

        } else if (result instanceof JSONArray) {
            JSONArray array = (JSONArray) result;
            for (int i = 0; i < array.size(); i++) {
                Object arrObj =  array.get(i);

                this.parseJson(arrObj);
            }
        }
        return result;
    }


}


