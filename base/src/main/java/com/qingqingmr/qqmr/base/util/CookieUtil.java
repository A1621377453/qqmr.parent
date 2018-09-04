package com.qingqingmr.qqmr.base.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Cookie工具类
 *
 * @author crn
 * @datetime 2018-07-04 19:34:48
 */
public class CookieUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(CookieUtil.class);

    /**
     * 获取cookie的值
     *
     * @param req
     * @param cookieName
     * @param isDecoder  是：utf-8编码 否：不编码使用默认的
     * @return
     * @author
     * @date
     */
    public static String getCookieValue(HttpServletRequest req, String cookieName, Boolean isDecoder) {
        Cookie[] cookieList = req.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValueString = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValueString = URLDecoder.decode(
                                cookieList[i].getValue(), "utf-8");
                    } else {
                        retValueString = cookieList[i].getValue();
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Cookie Decode Error : ", e);
        }
        return retValueString;

    }

    /**
     * 设置cookie
     *
     * @param req
     * @param res
     * @param cookieName
     * @param cookieValue
     * @param cookieMaxAge 设置cookie最大生存时间 单位秒
     * @param isDecoder    是：utf-8编码 否：不编码使用默认的
     * @author
     * @date
     */
    public static void setCookie(HttpServletRequest req,
                                 HttpServletResponse res, String cookieName, String cookieValue,
                                 int cookieMaxAge, Boolean isDecoder) {
        if (cookieValue == null) {
            LOG.info(cookieName + " 为 null");
            return;
        }
        try {
            if (isDecoder) {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxAge > 0) {
                cookie.setMaxAge(cookieMaxAge);
            }
            if (req != null) {
                // 设置cookie域名
                cookie.setDomain(getDomainName(req));
            }
            cookie.setPath("/");
            res.addCookie(cookie);
        } catch (Exception e) {
            LOG.error("Cookie Decode Error : ", e);
        }

    }

    /**
     * 删除cookie
     *
     * @param req
     * @param res
     * @param cookieName
     * @author
     * @date
     */
    public static void deleteCookie(HttpServletRequest req, HttpServletResponse res, String cookieName) {
        setCookie(req, res, cookieName, "", 0, false);
    }

    /**
     * 获取cookie作用域
     *
     * @param req
     * @return
     * @author gaobing
     * @date 2016年11月23日 上午11:13:37
     */
    private static final String getDomainName(HttpServletRequest req) {
        String domainName = "";
        String serverName = req.getRequestURL().toString();

        if (StringUtils.isNotBlank(serverName)) {
            serverName = serverName.toLowerCase().substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            // 表示转义
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                domainName = "." + domains[len - 3] + "." + domains[len - 2]
                        + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                domainName = "." + domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName != "" && domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        return domainName;
    }
}
