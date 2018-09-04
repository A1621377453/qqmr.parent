package com.qingqingmr.qqmr.base.gateway;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 网关接口
 *
 * @author ythu
 * @datetime 2018-07-12 11:19:12
 */
public interface Gateway {

    /**
     * 处理请求
     *
     * @param request
     * @param response
     * @param parameters
     * @return
     */
    String processingRequest(HttpServletRequest request, HttpServletResponse response, Map<String, String> parameters);

}
