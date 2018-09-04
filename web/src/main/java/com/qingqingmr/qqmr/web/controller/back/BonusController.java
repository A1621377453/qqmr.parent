package com.qingqingmr.qqmr.web.controller.back;

import com.alibaba.fastjson.JSONObject;
import com.qingqingmr.qqmr.base.bo.UserBonusSearchBO;
import com.qingqingmr.qqmr.common.PageResult;
import com.qingqingmr.qqmr.common.util.FastJsonUtil;
import com.qingqingmr.qqmr.domain.bean.UserBonusVO;
import com.qingqingmr.qqmr.service.UserBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 佣金控制器
 *
 * @author crn
 * @datetime 2018-07-12 14:43:44
 */
@Controller
@ResponseBody
@RequestMapping(value = "/back/bonus")
public class BonusController {

    @Autowired
    private UserBonusService userBonusService;

    /**
     * 后台佣金列表
     *
     * @param bonusSearchBO
     * @return java.lang.String
     * @datetime 2018/7/13 16:43
     * @author crn
     */
    @RequestMapping(value = "/listbonus", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String listBonus(UserBonusSearchBO bonusSearchBO, HttpServletRequest request) {
        JSONObject json = new JSONObject();

        if (null == bonusSearchBO) {
            bonusSearchBO = new UserBonusSearchBO();
        }
        PageResult<UserBonusVO> listBonus = userBonusService.listBonus(bonusSearchBO, request);
        json.put("code", 1);
        json.put("msg", "查询成功");
        json.put("data", listBonus);
        return FastJsonUtil.toJsonString(json);
    }
}
