package com.demo.annotations;

import java.lang.annotation.*;

/**
 * Java 11 特性标注
 * 用于标识Java 11中引入的特性
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Java11Feature {
    /**
     * 特性描述
     */
    String value() default "";
    
    /**
     * 详细说明
     */
    String desc() default "";
}