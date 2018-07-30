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
       BaseResponse response = new BaseResponse();
       response.setCode(ce.getCode());
       response.setMsg(ce.getMessage());

        logger.info("--------->拦截到CobraException异常");
        return response;

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse handelException(Exception ce)
    {
        BaseResponse response = new BaseResponse();
        if(ce instanceof CobraException){
            CobraException ex = (CobraException) ce;

            response.setCode(ex.getCode());
            response.setMsg(ex.getMessage());

            logger.info("--------->拦截到Exception异常");

        }

        return response;

    }
}
