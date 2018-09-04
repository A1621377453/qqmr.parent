package com.qingqingmr.qqmr.web.interceptor;

import com.qingqingmr.qqmr.base.util.BaseHttpUtil;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.domain.bean.SupervisorCurrentVO;
import com.qingqingmr.qqmr.service.RightRoleService;
import com.qingqingmr.qqmr.service.SupervisorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台权限拦截器
 * </p>
 *
 * @author ztl
 * @datetime 2018-7-11 13:57:43
 */
public class BackRightInterceptor implements HandlerInterceptor {
    private final static Logger log = LoggerFactory.getLogger(BackRightInterceptor.class);
    @Autowired
    private SupervisorService supervisorService;
    @Autowired
    private RightRoleService rightRoleService;

    /**
     * 预处理回调方法，实现处理器的预处理
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        SupervisorCurrentVO supervisor = supervisorService.getSupervisorCurrent(request);
        if (supervisor == null)
            return true;
        List<Long> rightIds = supervisor.getRightIds();
        rightIds = rightIds == null ? new ArrayList<Long>() : rightIds;
        String resource = BaseHttpUtil.getRelativeUri(request.getRequestURI(), request);
        List<Long> id = rightRoleService.getRightByResource(resource);
        if (id != null && id.size() > 0) {
            for (Long rightId : rightIds) {
                for (Long right : id) {
                    if (right.longValue() == rightId.longValue()) {
                        return true;
                    }
                }
            }
        }
        log.info("【{}】该资源找不到对应的权限", resource);

        Map<String, Object> map = new HashMap<>();
        map.put("code", ResultInfo.EXCEPTION_CODE_2);
        map.put("msg", ResultInfo.EXCEPTION_MSG_2);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(FastJsonUtil.toJsonString(map));
        writer.flush();
        return false;
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
