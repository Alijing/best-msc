package com.jing.common.log.aspect;

import java.lang.annotation.*;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.log.aspect
 * @date : 2023/3/31 14:56
 * @description :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface WebLog {
    /**
     * 日志描述信息
     *
     * @return 日志描述信息
     * @author jing
     * @date 2023/3/31 14:58
     */
    String description() default "";

}
