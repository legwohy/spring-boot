package com.cobra.http;

import com.cobra.test.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class RestTemplateUtilTest extends BaseTest {
    String url = "https://www.baidu.com";

    @Test
    public void post() throws Exception{
        log.info("response:{}", RestTemplateUtil.post(url));
    }

}
