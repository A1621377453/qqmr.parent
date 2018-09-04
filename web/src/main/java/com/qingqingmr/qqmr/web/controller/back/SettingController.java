package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.bo.DistributionRuleBO;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.constant.SettingKeyConstant;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.service.SettingService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运营设置-分销规则 运营规则
 *
 * @author ztl
 * @datetime 2018-7-5 13:49:44
 */
@Controller
@ResponseBody
@CheckLogin(isCheck = true)
@RequestMapping("/back/setting")
public class SettingController {
    private static final Logger LOG = LoggerFactory.getLogger(SupervisorController.class);

    @Autowired
    private SettingService settingService;


    /**
     * 查询已经设置的运营规则
     *
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/showrunruledetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showRunRuleDetail() {
        JSONObject json = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();

        String withdraw_time = settingService.getSettingValueByKey(SettingKeyConstant.WITHDRAW_TIME);
        String service_award_fee = settingService.getSettingValueByKey(SettingKeyConstant.SERVICE_AWARD_FEE);
        String award_time = settingService.getSettingValueByKey(SettingKeyConstant.AWARD_TIME);

        map.put("withdraw_time", withdraw_time);
        map.put("service_award_fee", service_award_fee);
        map.put("award_time", award_time);

        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", map);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 修改运营规则
     *
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/editrunrule", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String editRunRule(HttpServletRequest request, String withdraw_time, String service_award_fee, String award_time) {
        JSONObject json = new JSONObject();
        ResultInfo resultInfo = new ResultInfo();

        if (StringUtils.isBlank(withdraw_time) || StringUtils.isBlank(service_award_fee) || StringUtils.isBlank(award_time)) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }

        Map<String, String> info = new HashMap<String, String>();
        info.put(SettingKeyConstant.WITHDRAW_TIME, withdraw_time);
        info.put(SettingKeyConstant.SERVICE_AWARD_FEE, service_award_fee);
        info.put(SettingKeyConstant.AWARD_TIME, award_time);

        try {
            resultInfo = settingService.updateSettings(info, request);
            if (resultInfo.getCode() < 1) {
                json.put("code", resultInfo.getCode());
                json.put("msg", resultInfo.getMsg());
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("修改运营规则--{}", e.getMessage());
            json.put("code", resultInfo.getCode());
            json.put("msg", resultInfo.getMsg());
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 查询要更改的分销规则
     *
     * @author ztl
     */
    @RequestMapping(value = "/showsaleruledetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showSaleRuleDetail() {
        JSONObject json = new JSONObject();
        Map<String, List<DistributionRuleBO>> distributionRules = settingService.listAllDistributionVORule();

        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", distributionRules);
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 编辑分销规则
     *
     * @author ztl
     */
    @RequestMapping(value = "/editsalerule", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String editSaleRule(HttpServletRequest request, String distributionRules) {
        JSONObject json = new JSONObject();
        ResultInfo resultInfo = new ResultInfo();

        if (distributionRules == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }

        try {
            resultInfo = settingService.updateDistributionRule(distributionRules, request);
            if (resultInfo.getCode() < 1) {
                json.put("code", resultInfo.getCode());
                json.put("msg", resultInfo.getMsg());
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("修改分销规则--{}", e.getMessage());
            json.put("code", resultInfo.getCode());
            json.put("msg", resultInfo.getMsg());
            return FastJsonUtil.toJsonString(json);
        }
        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }
}
