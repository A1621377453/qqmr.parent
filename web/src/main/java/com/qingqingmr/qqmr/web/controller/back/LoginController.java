package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.util.BaseHttpUtil;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.StrUtil;
import com.qingqingmr.qqmr.domain.entity.EventSupervisor;
import com.qingqingmr.qqmr.service.EventSupervisorService;
import com.qingqingmr.qqmr.service.SupervisorService;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台登录控制器
 *
 * @author crn
 * @datetime 2018-07-04 15:49:43
 */
@Controller
@ResponseBody
@RequestMapping(value = "/back")
public class LoginController {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private EventSupervisorService eventSupervisorService;

    /**
     * 登录
     *
     * @param name     用户名
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String login(String name, String password, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        if (StringUtils.isBlank(name) || StringUtils.isBlank(password)) {
            json.put("code", -1);
            json.put("msg", "用户名或密码不能为空");
            return FastJsonUtil.toJsonString(json);
        }

        if (!StrUtil.isValidUsername(name, 4, 16)) {
            json.put("code", -1);
            json.put("msg", "用户名输入有误");
            return FastJsonUtil.toJsonString(json);
        }

        if (!StrUtil.isValidPassword(password)) {
            json.put("code", -1);
            json.put("msg", "密码输入有误");
            return FastJsonUtil.toJsonString(json);
        }

        // 登录操作
        ResultInfo resultInfo = null;
        try {
            resultInfo = supervisorService.login(name, password, BaseHttpUtil.getIp(request), request);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("用户登录异常--【{}】", e.getMessage());
            json.put("code", -1);
            json.put("msg", "登录失败");
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 用户退出
     *
     * @param request
     * @return
     */
    @CheckLogin(isCheck = true)
    @RequestMapping(value = "/dropout", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String dropOut(HttpServletRequest request) {
        JSONObject json = new JSONObject();

        // 添加管理员操作事件
        try {
            eventSupervisorService.saveEventSupervisor(request, EventSupervisor.SupervisorEventEnum.LOGOUT);
        } catch (Exception e) {
            LOG.error("添加管理员操作事件异常--【{}】", e.getMessage());
        }

        request.getSession().invalidate();
        json.put("code", 1);
        json.put("msg", "退出成功");
        return FastJsonUtil.toJsonString(json);
    }
}
