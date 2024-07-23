package com.jing.common.log.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author : jing
 * @projectName : best-msc
 * @packageName : com.jing.common.log.aspect
 * @date : 2023/3/31 14:59
 * @description :
 */
@Aspect
@Component
//@Profile({"dev", "test"})
public class WebLogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 换行符
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * 以自定义的 @WebLog 注解为切点
     *
     * @author jing
     * @date 2023/3/31 15:10
     */
    @Pointcut("@annotation(com.jing.common.log.aspect.WebLog)")
    public void webLog() {
    }

    /**
     * 在切点之前织入
     *
     * @param joinPoint 切点
     * @author jing
     * @date 2023/3/31 15:12
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attributes) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        // 获取 @WebLog 注解的描述信息
        String methodDescription = getAspectLogDescription(joinPoint);
        // 打印请求相关参数
        logger.info("====================== Start =====================================================================");
        // 打印请求 url
        logger.info("URL                : {}", request.getRequestURL().toString());
        // 打印描述信息
        logger.info("Description        : {}", methodDescription);
        // 打印 Http method
        logger.info("HTTP Method        : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        logger.info("Class Method       : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // 打印请求的 IP
        logger.info("IP                 : {}", request.getRemoteAddr());
        // 打印请求参数
        logger.info("Request Args       : {}", Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 在切点之前织入
     *
     * @author jing
     * @date 2023/3/31 15:43
     */
    @After("webLog()")
    public void doAfter() throws Throwable {
        logger.info("====================== End =====================================================================" + LINE_SEPARATOR);
    }

    /**
     * 环绕
     *
     * @param proceedingJoinPoint number
     * @return 结果
     * @author jing
     * @date 2023/3/31 15:46
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        if(!Objects.isNull(result)){
            // 打印出参
            logger.info("Response Args      : {}", result);
        }
        // 执行耗时
        logger.info("Time-Consuming     : {}", System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 获取切面注解的描述
     *
     * @param joinPoint 切点
     * @return 描述信息
     * @author jing
     * @date 2023/3/31 15:17
     */
    private String getAspectLogDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuilder description = new StringBuilder();
        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            Class<?>[] clazzs = method.getParameterTypes();
            if (clazzs.length == args.length) {
                description.append(method.getAnnotation(WebLog.class).description());
            }
        }
        return description.toString();
    }

}
