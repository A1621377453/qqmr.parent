package com.qingqingmr.qqmr.base.util;

import com.qingqingmr.qqmr.common.constant.SystemConstant;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * http请求基本功能
 *
 * @author crn
 * @datetime 2018-7-4 18:10:44
 */
public class BaseHttpUtil {

    /**
     * 获取当前请求IP
     *
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip;
        if (request != null) {
            ip = request.getHeader("X-Forwarded-For");
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = "127.0.0.1";
            }
        } else {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 获取当前请求根路径
     *
     * @param request
     * @return
     */
    public static String getBaseURL(HttpServletRequest request) {
        if (request != null) {

            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath();
        }
        return SystemConstant.APPLICATION_BASEURL;
    }

    /**
     * 获取完整的URI
     *
     * @param uri
     * @param request
     * @return
     */
    public static String getAbsoluteUri(String uri, HttpServletRequest request) {
        if (uri.startsWith(request.getContextPath())) {
            return uri;
        }
        return request.getContextPath() + uri;
    }

    /**
     * 获取相对的URI
     *
     * @param uri
     * @param request
     * @return
     */
    public static String getRelativeUri(String uri, HttpServletRequest request) {
        if (uri.startsWith(request.getContextPath()) && !"/".equals(request.getContextPath())) {
            return uri.substring(request.getContextPath().length());
        }
        return uri;
    }
}
