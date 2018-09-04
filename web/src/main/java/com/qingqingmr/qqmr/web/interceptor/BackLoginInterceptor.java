package com.qingqingmr.qqmr.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SystemConstant;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;


/**
 * 后台登录拦截器
 *
 * @author crn
 * @datetime 2018-07-04 15:49:43
 */
public class BackLoginInterceptor implements HandlerInterceptor {

    /**
     * 预处理回调方法，实现处理器的预处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod) {
            CheckLogin checkLogin = findAnnotation((HandlerMethod) handler, CheckLogin.class);
            if (checkLogin != null && checkLogin.isCheck()) {
                if (request.getSession().getAttribute(SystemConstant.SESSION_SUPERVISOR + request.getSession().getId()) == null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("code", ResultInfo.EXCEPTION_CODE_1);
                    map.put("msg", ResultInfo.EXCEPTION_MSG_1);
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Content-type", "text/html;charset=UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(map));
                    writer.flush();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取注解（首先获取类注解，然后获取方法注解）
     *
     * @param handler
     * @param annotationType
     * @datetime 2018/7/15 17:58
     * @author crn
     * @return T
     */
    private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {

        T annotation = handler.getBeanType().getAnnotation(annotationType);
        if (annotation != null)
            return annotation;
        return handler.getMethodAnnotation(annotationType);
    }

    /**
     * 后处理回调方法，实现处理器的后处理
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    /**
     * 整个请求处理完毕回调方法
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
