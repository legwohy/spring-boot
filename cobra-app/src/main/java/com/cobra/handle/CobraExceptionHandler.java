package com.cobra.handle;


import com.cobra.exception.CobraException;
import com.cobra.param.BaseResponse;
import com.cobra.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CobraExceptionHandler
{
    private Logger logger = LoggerFactory.getLogger(CobraExceptionHandler.class);

    @ExceptionHandler(CobraException.class)
    @ResponseBody
    public BaseResponse handelCobraException(CobraException ce)
    {

        logger.info("--------->拦截到异常");
        return ResponseUtil.error(ce.getCode(),ce.getMessage());

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse handelException(CobraException ce)
    {

        logger.info("--------->拦截到异常");
        return ResponseUtil.error(ce.getCode(),ce.getMessage());

    }
}
