package com.cobra.spring.myMVC.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE,ElementType.METHOD}) // 方法上 类上
@Retention(RetentionPolicy.RUNTIME)
public @interface XRequestMapping {
    String value();
}
