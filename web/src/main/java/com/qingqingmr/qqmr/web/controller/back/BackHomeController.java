package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.service.SupervisorService;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台首页控制器
 *
 * @author crn
 * @datetime 2018-07-05 10:52:28
 */
@Controller
@ResponseBody
@CheckLogin(isCheck = true)
@RequestMapping(value = "/back")
public class BackHomeController {
    private static final Logger LOG = LoggerFactory.getLogger(BackHomeController.class);

    @Autowired
    private SupervisorService supervisorService;

    /**
     * 首页
     *
     * @param request
     * @return java.lang.String
     * @datetime 2018/7/12 16:57
     * @author crn
     */
    @RequestMapping(value = "/home", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String home(HttpServletRequest request) {
        JSONObject json = new JSONObject();

        ResultInfo resultInfo = supervisorService.getSupervisorHomeInfo(request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }
}
