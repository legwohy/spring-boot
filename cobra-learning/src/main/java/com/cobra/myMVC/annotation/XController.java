package com.cobra.myMVC.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE) // 类上
@Retention(RetentionPolicy.RUNTIME)
public @interface XController {
     String value() default "";
}
