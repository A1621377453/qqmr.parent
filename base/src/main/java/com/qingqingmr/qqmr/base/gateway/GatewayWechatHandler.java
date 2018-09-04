package com.qingqingmr.qqmr.base.gateway;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.Encrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 微信网关核心处理器
 *
 * @author ythu
 * @datetime 2018-07-12 11:23:42
 */
@Component
public class GatewayWechatHandler {
    private final Logger LOG = LoggerFactory.getLogger(GatewayWechatHandler.class);

    /**
     * 处理请求
     *
     * @param request
     * @param response
     * @param md5Key
     * @param gateway
     * @return
     */
    public ResultInfo processingRequest(HttpServletRequest request, HttpServletResponse response, String md5Key, Gateway gateway) {
        ResultInfo resultInfo = new ResultInfo();
        if (request == null || response == null) {
            throw new IllegalStateException("该方法处理http或者https请求");
        }
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("编码异常--%s", e.getMessage());
            e.printStackTrace();
            resultInfo.setInfo(-1, "编码异常");
        }
        Map<String, String> params = resolvingParams(request, response, md5Key);
        if (params == null) {
            resultInfo.setInfo(-1, "解析参数有误");
            return resultInfo;
        }
        String result = gateway.processingRequest(request, response, params);
        if (result == null) {
            resultInfo.setInfo(-1, "业务处理有误");
            return resultInfo;
        }
        if ("image".equals(result)) {
            resultInfo.setInfo(-2, "返回图片（特殊业务处理）");
            return resultInfo;
        }
        render(response, result);
        resultInfo.setInfo(1, "处理成功");
        return resultInfo;
    }

    /**
     * 解析请求参数
     *
     * @param request
     * @param response
     * @return
     */
    private Map<String, String> resolvingParams(HttpServletRequest request, HttpServletResponse response, String md5Key) {
        Map<String, String> parameters = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        String nextElement = null;
        while (parameterNames.hasMoreElements()) {
            nextElement = parameterNames.nextElement();
            parameters.put(nextElement, request.getParameter(nextElement));
        }

        JSONObject json = new JSONObject();
        // 判断是否存在opt参数
        if (!parameters.containsKey("OPT")) {
            LOG.info("请求参数缺失opt参数");
            json.put("code", -1);
            json.put("msg", "请求参数非法");
            render(response, json.toJSONString());
            return null;
        }
        // 判断是否存在_s参数
        if (!parameters.containsKey("_s")) {
            LOG.info("请求参数缺失_s参数");
            json.put("code", -2);
            json.put("msg", "请求参数非法");
            render(response, json.toJSONString());
            return null;
        }

        final List<String> keys = new ArrayList<>();
        parameters.forEach((k, v) -> keys.add(k));
        List<String> _keys = keys.stream().filter(k -> !"_s".equals(k)).sorted().collect(Collectors.toList());

        final StringBuffer buffer = new StringBuffer();
        _keys.forEach(k -> buffer.append(k).append("=").append(parameters.get(k)).append("&"));
        buffer.deleteCharAt(buffer.length() - 1);

        String s = Encrypt.MD5(buffer.toString() + md5Key, Charset.forName("UTF-8"));
        // 判断_s参数和本次加密是否一致
        if (!s.equals(parameters.get("_s"))) {
            LOG.info("解密请求参数_s失败");
            json.put("code", -3);
            json.put("msg", "请求参数非法");
            render(response, json.toJSONString());
            return null;
        }
        return parameters;
    }

    /**
     * 数据返回
     *
     * @param response
     * @param info
     */
    private void render(HttpServletResponse response, String info) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.println(info);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("系统异常--%s", e.getMessage());
        }
    }
}
