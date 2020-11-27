package com.cobra.spring.myMVC.annotation;

import java.lang.annotation.*;


@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface XService {
     String value();
}
