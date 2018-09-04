package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.service.PlatformOverviewService;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 平台概况控制器
 *
 * @author crn
 * @datetime 2018-07-11 15:47:40
 */
@Controller
@ResponseBody
@CheckLogin(isCheck = true)
@RequestMapping(value = "/back/overview")
public class PlatformOverviewController {

    @Autowired
    private PlatformOverviewService platformOverviewService;

    /**
     * 获取平台概况
     *
     * @param timeBegin
     * @param timeEnd
     * @return java.lang.String
     * @datetime 2018/7/13 15:52
     * @author crn
     */
    @RequestMapping(value = "/getplatformoverview", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String getPlatformOverview(String timeBegin, String timeEnd, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        ResultInfo resultInfo = platformOverviewService.getPlatformOverview(timeBegin, timeEnd, request);
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 获取平台当日概况
     *
     * @param
     * @return java.lang.String
     * @datetime 2018/7/12 9:49
     * @author crn
     */
    @RequestMapping(value = "/getdayplatformoverview", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String getDayPlatformOverview() {
        JSONObject json = new JSONObject();

        ResultInfo resultInfo = platformOverviewService.getDayPlatformOverview();
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

}
