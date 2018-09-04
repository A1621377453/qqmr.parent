package com.qingqingmr.qqmr.web.annotation;

import java.lang.annotation.*;

/**
 * 检查登陆
 * 该注解针对方法和类
 *
 * @author crn
 * @datetime 2018-07-05 11:03:15
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckLogin {
    /**
     * 是否需要检查
     *
     * @return
     */
    boolean isCheck() default false;
}
