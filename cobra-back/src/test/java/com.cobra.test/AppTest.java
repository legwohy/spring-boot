package com.cobra.test;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppTest {
    /**
     * mvn clean install 命令会启动测试用例
     * 解决办法
     * 1、mvn clean install -Dmaven.test.skip=true 会跳过
     * 2、使用插件 maven-surefire-plugin 跳过test阶段
     */
    @Test
    public void test(){
        if(true){
            throw new RuntimeException("项目启动 本测试用例运行了");
        }
    }
}
