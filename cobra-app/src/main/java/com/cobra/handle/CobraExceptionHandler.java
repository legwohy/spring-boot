package com.cobra.handle;


import com.cobra.exception.CobraException;
import com.cobra.param.BaseResponse;
import com.cobra.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author admin
 */
@ControllerAdvice
public class CobraExceptionHandler
{
    private Logger logger = LoggerFactory.getLogger(CobraExceptionHandler.class);

    @ExceptionHandler(CobraException.class)
    @ResponseBody
    public BaseResponse handelCobraException(CobraException ce)
    {

        logger.info("--------->拦截到CobraException异常,errorMsg=[{}]",ce);
        return new BaseResponse(ce.getCode(),ce.getMessage());

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse handelException(Exception ce)
    {
        logger.info("--------->拦截到Exception异常 errorMsg=[{}]",ce);
        if(ce instanceof CobraException){
            CobraException ex = (CobraException) ce;


            return new BaseResponse(ex.getCode(),ex.getMessage());

        }

        return new BaseResponse("404","系统异常");

    }
}
