package com.jing.msc.cobweb.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : jing
 * @since : 2025/4/29 16:17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelColumn {

    /**
     * 字段标题
     */
    String title() default "";

    /**
     * 字段描述
     */
    String description() default "";

    /**
     * 字段是否必填
     */
    boolean required() default true;

}
