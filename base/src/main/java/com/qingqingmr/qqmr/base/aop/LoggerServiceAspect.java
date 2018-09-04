package com.qingqingmr.qqmr.base.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 业务方法切面
 *
 * @author ythu
 * @datetime 2018-07-14 13:56:07
 */
@Aspect
@Component
public class LoggerServiceAspect {
    /**
     * 统计时间
     */
    private ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

    /**
     * 切入点
     */
    @Pointcut("execution(* com.qingqingmr.qqmr..service..*.*(..))")
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
        Class<?> clazz = joinPoint.getTarget().getClass();
        String method = joinPoint.getSignature().getName();
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.info("Call Class -- {}.{},args={}", clazz.getName(), method, Arrays.asList(joinPoint.getArgs()));
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
        logger.info("Call Class -- {}.{},args={}", clazz.getName(), method, Arrays.asList(joinPoint.getArgs()));
        logger.info("Call Result -- {}", result);
        if (threadLocal.get() != null) {
            logger.info("Call Time -- {}s", +(System.currentTimeMillis() - threadLocal.get()) / 1000);
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
        logger.info("Call Class -- {}.{}，args={}", clazz.getName(), method, Arrays.asList(joinPoint.getArgs()));
        logger.info("Call Exception -- {}", e.getMessage());
        if (threadLocal.get() != null) {
            logger.info("Call Time -- {}s", +(System.currentTimeMillis() - threadLocal.get()) / 1000);
        }
    }
}
