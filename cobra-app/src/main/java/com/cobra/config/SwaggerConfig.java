package com.cobra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger 配置类
 * 注解@configuration-->注解@bean(类似于xml中的beans-->bean)
 * 注解EnableSwagger2 启动swagger注解
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())// api的基本信息
                .select()// 选择那些路径和api会生成document
                // 此处需标注出RestController类
                .apis(RequestHandlerSelectors.basePackage("com.shiro.controller")) // 对所有api进行监控
                .paths(PathSelectors.any())// 对所有路径进行监控
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("我的swagger API文档")
                .description("springboot 整合swagger API")
                .termsOfServiceUrl("")
                .version("1.0.0")// 版本
                .build();
    }




}
