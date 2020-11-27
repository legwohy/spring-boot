package com.cobra.spring.myMVC.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD) // 属性上
@Retention(RetentionPolicy.RUNTIME)
public @interface XQualifier {
    String value();
}
