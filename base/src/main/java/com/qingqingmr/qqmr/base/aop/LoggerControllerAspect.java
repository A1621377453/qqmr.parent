package com.qingqingmr.qqmr.base.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * 控制器层切面
 *
 * @author ythu
 * @datetime 2018-07-14 13:55:42
 */
@Aspect
@Component
public class LoggerControllerAspect {
    /**
     * 统计时间
     */
    private ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    /**
     * 切入点
     */
    @Pointcut("execution(* com.qingqingmr.qqmr..controller..*.*(..))")
    public void doPointcut() {
    }

    /**
     * 前置通知
     *
     * @param joinPoint
     */
    @Before(value = "doPointcut()")
    public void before(JoinPoint joinPoint) {
        threadLocal.set(System.currentTimeMillis());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Class<?> clazz = joinPoint.getTarget().getClass();
        String method = joinPoint.getSignature().getName();
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.info("Request Class -- {}.{},args={}", clazz.getName(), method, Arrays.asList(joinPoint.getArgs()));
        Enumeration<String> attrs = request.getParameterNames();
        while (attrs.hasMoreElements()) {
            String param = (String) attrs.nextElement();
            logger.info("Request Param -- {}={}", param, request.getParameter(param));
        }
    }

    /**
     * 返回后通知
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(value = "doPointcut()", returning = "result")
    public void after(JoinPoint joinPoint, Object result) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        String method = joinPoint.getSignature().getName();
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.info("Request Class -- {}.{}，args={}", clazz.getName(), method, Arrays.asList(joinPoint.getArgs()));
        logger.info("Response Result -- {}", result);
        if (threadLocal.get() != null) {
            logger.info("Response Time -- {}s", +(System.currentTimeMillis() - threadLocal.get()) / 1000);
        }
    }

    /**
     * 异常通知
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "doPointcut()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Exception e) {
        Class<?> clazz = joinPoint.getTarget().getClass();
        String method = joinPoint.getSignature().getName();
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.info("Request Class -- {}.{}，args={}", clazz.getName(), method, Arrays.asList(joinPoint.getArgs()));
        logger.info("Response Exception -- {}", e.getMessage());
        if (threadLocal.get() != null) {
            logger.info("Response Time -- {}s", +(System.currentTimeMillis() - threadLocal.get()) / 1000);
        }
    }
}
