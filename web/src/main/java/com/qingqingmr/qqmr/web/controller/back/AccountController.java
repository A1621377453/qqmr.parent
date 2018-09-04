package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.common.ResultInfo;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.common.util.Security;
import com.qingqingmr.qqmr.domain.entity.WithdrawalUser;
import com.qingqingmr.qqmr.service.UserFundsService;
import com.qingqingmr.qqmr.service.WithdrawalUserService;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 提现等账户处理
 *
 * @author ztl
 * @datetime 2018-07-09 16:50:18
 */
@Controller
@ResponseBody
@CheckLogin(isCheck = true)
@RequestMapping("/back/account")
public class AccountController {
    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private WithdrawalUserService withdrawalUserService;
    @Autowired
    private UserFundsService userFundsService;

    /**
     * 后台-提现列表
     *
     * @author ztl
     */
    @RequestMapping(value = "/showwithdrawuser", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showWithdrawUser(Integer currPage, Integer pageSize, @RequestParam Map<String, String> parameters, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        if (parameters != null && StringUtils.isNotBlank(parameters.get("nickName"))) {
            String nickName = Security.encodeHexString(parameters.get("nickName"));
            parameters.put("nickName", nickName);
        }

        ResultInfo resultInfo = withdrawalUserService.listWithdrawUser(currPage, pageSize, parameters, request);

        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 后台-提现详情
     *
     * @author ztl
     */
    @RequestMapping(value = "/showwithdrawuserdetail", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String showWithdrawUserDetail(Long id, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (id == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }

        ResultInfo resultInfo = withdrawalUserService.getWithdrawUserVOById(id, request);

        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        json.put("data", resultInfo.getObj());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 后台-提现审核
     *
     * @author ztl
     */
    @RequestMapping(value = "/auditwithdrawuser", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String auditWithdrawUser(WithdrawalUser withdrawalUser, HttpServletRequest request) {
        JSONObject json = new JSONObject();
        ResultInfo resultInfo = new ResultInfo();

        if (withdrawalUser == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }

        try {
            resultInfo = withdrawalUserService.updateWithdrawUserById(withdrawalUser, request);
            if (resultInfo.getCode() < 1) {
                json.put("code", resultInfo.getCode());
                json.put("msg", resultInfo.getMsg());
                return FastJsonUtil.toJsonString(json);
            }
        } catch (Exception e) {
            LOG.error("提现审核--{}", e.getMessage());
            json.put("code", resultInfo.getCode());
            json.put("msg", e.getMessage());
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", resultInfo.getCode());
        json.put("msg", resultInfo.getMsg());
        return FastJsonUtil.toJsonString(json);
    }

    /**
     * 更新账户签名
     *
     * @param id
     * @return
     * @author ztl
     */
    @RequestMapping(value = "/editsign", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String editSign(Long id) {
        JSONObject json = new JSONObject();

        if (id == null) {
            json.put("code", -1);
            json.put("msg", ResultInfo.STR_NULL_OBJ);
            return FastJsonUtil.toJsonString(json);
        }

        if (userFundsService.updateFundsSign(id) < 0) {
            json.put("code", -1);
            json.put("msg", "更新签名失败");
            return FastJsonUtil.toJsonString(json);
        }

        json.put("code", 1);
        json.put("msg", "更新签名成功");
        return FastJsonUtil.toJsonString(json);
    }

}
