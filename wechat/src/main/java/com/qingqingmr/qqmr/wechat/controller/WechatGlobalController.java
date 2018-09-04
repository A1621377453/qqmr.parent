package com.qingqingmr.qqmr.wechat.controller;

import com.qingqingmr.qqmr.base.gateway.GatewayWechatHandler;
import com.qingqingmr.qqmr.base.spring.SpringContextProperties;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.wechat.gateway.impl.GatewayWechatImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信接口
 *
 * @author liujingjing
 * @datetime 2018年7月11日 下午2:39:15
 */
@Controller
@RequestMapping("/wechat")
@ResponseBody
public class WechatGlobalController {
    private static final Logger LOG = LoggerFactory.getLogger(WechatGlobalController.class);
    @Autowired
    private GatewayWechatHandler gatewayWechatHandler;
    @Autowired
    private GatewayWechatImpl gatewayWechat;
    @Autowired
    private SpringContextProperties contextProperties;

    /**
     * 统一入口
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/index", method = {RequestMethod.POST, RequestMethod.GET}, produces = {"text/html; charset=UTF-8"})
    public void index(HttpServletRequest request, HttpServletResponse response) {
        ResultInfo resultInfo = gatewayWechatHandler.processingRequest(request, response, contextProperties.getAppMd5Key(), gatewayWechat);
        LOG.info("接口处理--code=【{}】--msg=【{}】", resultInfo.getCode(), resultInfo.getMsg());
    }
}
