package com.cobra.controller;

import com.cobra.param.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/mdc")
public class TestLogController {

    @RequestMapping("/obj")
    public BaseResponse<Object> obj() throws Exception {
        log.info("主线程 请求....................");

        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        new Thread(() -> {
            if(null !=copyOfContextMap ){
                MDC.setContextMap(copyOfContextMap);
            }
            log.info("子线程一");
        }).start();
        new Thread(() -> {
            if(null !=copyOfContextMap ){
                MDC.setContextMap(copyOfContextMap);
            }
            log.info("子线程二");
        }).start();
        new Thread(() -> {
            if(null !=copyOfContextMap ){
                MDC.setContextMap(copyOfContextMap);
            }
            log.info("子线程三");
        }).start();

        Thread.sleep(100);
        return new BaseResponse<>(Boolean.TRUE);

    }
}
