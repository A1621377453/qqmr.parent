package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.bo.FeeSearchBO;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.domain.bean.PlatformFeeVO;
import com.qingqingmr.qqmr.service.PlatformService;
import com.qingqingmr.qqmr.web.annotation.CheckLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 收取费用控制器
 *
 * @author crn
 * @datetime 2018-07-12 09:53:56
 */
@Controller
@ResponseBody
@CheckLogin(isCheck = true)
@RequestMapping(value = "/back/fee")
public class FeeController {

    @Autowired
    private PlatformService platformService;

    /**
     * 获取平台管理费列表
     *
     * @param feeSearchBO
     * @return String
     * @datetime 2018/7/12 11:45
     * @author crn
     */
    @RequestMapping(value = "/listplatformfee", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listPlatformFee(FeeSearchBO feeSearchBO, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == feeSearchBO) {
            feeSearchBO = new FeeSearchBO();
        }
        PageResult<PlatformFeeVO> listPlatformFee = platformService.listPlatformFee(feeSearchBO, request);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", listPlatformFee);
        return FastJsonUtil.toJsonString(json);
    }
}
