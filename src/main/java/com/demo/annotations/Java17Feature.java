package com.demo.annotations;

import java.lang.annotation.*;

/**
 * Java 17 特性标注
 * 用于标识Java 17中引入的特性
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Java17Feature {
    /**
     * 特性描述
     */
    String value() default "";
    
    /**
     * 详细说明
     */
    String description() default "";
}