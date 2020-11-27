package com.cobra.spring.sprboot.tomcat;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 *
 */
@SpringBootApplication
public class SpringBootEmbedTomcatApplication
{
    public static void main(String[] args)
    {
        // 可以有多个配置
        new SpringApplicationBuilder()
                        .sources(SpringBootEmbedTomcatApplication.class, TomcatConfiguration.class)
                        .run(args);

    }
}
