package com.cobra.interceptor;


import com.cobra.pojo.RequestLimitDO;
import com.cobra.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
public class AccessLimitInterceptor implements HandlerInterceptor
{
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 1、第一次 设置有效期
     * 2、查过限制次数，跑出错误信息
     * 3、未超过限制次数 剩余次数加一
     * @param request 请求
     * @param response 响应
     * @param o  其它对象
     * @return
     * @throws Exception 异常信息
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception
    {
        String url = request.getRequestURI();
        log.info("uri:{}",url);
        url = url.substring(1,url.length());
        String key_count = redisUtil.API_LIMIT_COUNT_PREFIX+url;
        long count = redisUtil.getIncrementValue(key_count);
        if(count == 1){
            // 设置有效期
            redisUtil.expire(key_count,redisUtil.API_LIMIT_EXPIRE);
            return true;
        }
        String key_value = redisUtil.API_LIMIT_VALUE_PREFIX+url;
        RequestLimitDO requestLimit = (RequestLimitDO)redisUtil.get(key_value);
        if(requestLimit.getLimit() > count){
            redisUtil.increment(key_count);
            return true;
        }else {
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write("服务器繁忙，稍后再试");
            return false;
        }


    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception
    {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception
    {

    }
}
