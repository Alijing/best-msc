package com.jing.common.swagger.annotation;

import com.jing.common.swagger.config.SwaggerConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.swagger.annotation
 * @date : 2021/4/22 15:19
 * @description :
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerConfig.class})
public @interface EnableCustomSwagger {

}
